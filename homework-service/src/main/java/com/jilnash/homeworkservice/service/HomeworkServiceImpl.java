package com.jilnash.homeworkservice.service;

import com.jilnash.homeworkservice.client.*;
import com.jilnash.homeworkservice.dto.HomeworkResponseDTO;
import com.jilnash.homeworkservice.mapper.HomeworkMapper;
import com.jilnash.homeworkservice.model.Homework;
import com.jilnash.homeworkservice.repo.HomeworkRepo;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

/**
 * Implementation of the HomeworkService interface that provides services for managing homeworks.
 * This class handles CRUD operations and business logic related to homework entities.
 * <p>
 * Responsibilities include:
 * - Fetching homeworks based on filtering criteria such as taskId, studentId, and creation date.
 * - Retrieving specific homework details by ID.
 * - Saving new homework after performing validation and business logic.
 * - Validating prerequisites and ensuring task-specific rules are met.
 * - Uploading homework-related files to the configured file storage.
 * - Providing task and student IDs linked to a homework.
 * - Setting a homework as checked.
 * - Generating pre-signed URLs for accessing homework files.
 * <p>
 * This class interacts with supporting repositories, clients, and services to fulfill its responsibilities.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class HomeworkServiceImpl implements HomeworkService {

    private final HomeworkRepo homeworkRepo;

    private final FileClient fileClient;

    private final CourseClient courseClient;

    private static final String HW_BUCKET = "course-project-homeworks";

    private final HomeworkFileService homeworkFileService;

    private final HomeworkMapper homeworkMapper;

    private final ProgressGrpcClient progressGrpcClient;

    private final CourseAccessGrpcClient courseAccessGrpcClient;

    private final TaskReqsGrpcClient taskReqsGrpcClient;

    /**
     * Retrieves a list of Homework entities filtered by various criteria.
     *
     * @param taskId       the ID of the task to filter homeworks by; can be null to omit this filter.
     * @param studentId    the ID of the student to filter homeworks by; can be null to omit this filter.
     * @param checked      a flag indicating whether to filter homeworks by their checked status; can be null to omit this filter.
     * @param createdAfter a date specifying that only homeworks created on or after this date should be included in the result;
     *                     can be null to omit this filter.
     * @return a list of Homework entities matching the provided filtering criteria.
     */
    @Override
    public List<Homework> getHomeworks(String taskId, String studentId, Boolean checked, Date createdAfter) {

        log.info("[SERVICE] Fetching homeworks");
        log.debug("[SERVICE] Fetching homeworks with taskId: {}, studentId: {}, checked: {}, createdAfter: {}",
                taskId, studentId, checked, createdAfter);

        Specification<Homework> spec = (root, query, cb) -> {
            Predicate p = cb.conjunction();

            if (studentId != null)
                p = cb.and(p, cb.equal(root.get("studentId"), studentId));

            if (taskId != null)
                p = cb.and(p, cb.equal(root.get("taskId"), taskId));

            if (checked != null)
                p = cb.and(p, cb.equal(root.get("checked"), checked));

            if (createdAfter != null)
                p = cb.and(p, cb.greaterThanOrEqualTo(root.get("createdAt"), createdAfter));

            return p;
        };

        return homeworkRepo.findAll(spec);
    }

    /**
     * Retrieves a specific Homework entity by its unique identifier.
     *
     * @param id the unique identifier of the homework to retrieve
     * @return the Homework entity corresponding to the provided identifier
     * @throws NoSuchElementException if no homework is found with the given identifier
     */
    @Override
    public Homework getHomework(UUID id) {
        return homeworkRepo
                .findById(id)
                .orElseThrow(() -> new NoSuchElementException("Homework not found with id: " + id));
    }

    /**
     * Retrieves the HomeworkResponseDTO representation of a Homework entity by its unique identifier.
     *
     * @param id the unique identifier of the Homework entity to retrieve
     * @return the HomeworkResponseDTO representation of the Homework entity corresponding to the provided identifier
     */
    public HomeworkResponseDTO getHomeworkDTO(UUID id) {

        log.info("[SERVICE] Fetching homework");
        log.debug("[SERVICE] Fetching homework with id: {}", id);

        return homeworkMapper.toResponseDTO(getHomework(id));
    }

    /**
     * Saves a new homework entry after performing required validations and business logic.
     * Validates the student's access, ensures prerequisites are met, and handles file uploads
     * and attempt tracking for the homework.
     *
     * @param homework the Homework entity containing details such as task ID, student ID,
     *                 and associated files to be saved
     * @return a boolean value indicating whether the homework was successfully saved
     * @throws IllegalArgumentException if the student has already completed this task,
     *                                  has not completed all prerequisites, or if previous
     *                                  homework submissions are still unchecked
     */
    @Override
    public Boolean saveHomework(Homework homework) {

        log.info("[SERVICE] Creating homework");
        log.debug("[SERVICE] Creating homework to taskId {} by studentId: {}",
                homework.getTaskId(), homework.getStudentId());

        courseAccessGrpcClient.validateStudentHasAccessToCourse(homework.getStudentId(), homework.getTaskId());

        // checking if student already completed this task
        if (progressGrpcClient.validateStudentCompletedTasks(homework.getStudentId(), List.of(homework.getTaskId())))
            throw new IllegalArgumentException("Student already completed this task");

        // checking if student completed all prerequisites
        if (!progressGrpcClient.validateStudentCompletedTasks
                (homework.getStudentId(), courseClient.getTaskPreRequisites(homework.getTaskId())))
            throw new IllegalArgumentException("Student did not complete all prerequisites");

        validatePreviousHomeworksChecked(homework.getStudentId(), homework.getTaskId());

        taskReqsGrpcClient.validateAllTaskFilesProvided(homework);

        homework.setAttempt(1 + homeworkRepo.countByStudentIdAndTaskId(homework.getStudentId(), homework.getTaskId()));

        var newHw = homeworkRepo.save(homework);
        uploadHWFiles(newHw);
        homeworkFileService.createdHomeworkFiles(newHw);

        return true;
    }

    private void validatePreviousHomeworksChecked(String studentId, String taskId) {
        if (homeworkRepo.getHwUnchecked(studentId, taskId))
            throw new IllegalArgumentException("Previous homework is not checked yet");
    }

    @Async
    protected void uploadHWFiles(Homework homework) {
        fileClient.uploadFile(HW_BUCKET,
                "homework-" + homework.getId(),
                homework.getFiles()
        );
    }

    /**
     * Retrieves the task ID associated with a specific homework ID.
     *
     * @param hwId the unique identifier of the homework
     * @return the task ID linked to the given homework ID
     * @throws NoSuchElementException if no homework is found with the specified ID
     */
    public String getHwTaskId(UUID hwId) {

        log.info("[SERVICE] Fetching task id");
        log.debug("[SERVICE] Fetching task id with homework id: {}", hwId);

        return homeworkRepo
                .getHwTaskId(hwId)
                .orElseThrow(() -> new NoSuchElementException("Homework not found with id: " + hwId));
    }

    /**
     * Retrieves the student ID associated with a specific homework ID.
     *
     * @param hwId the unique identifier of the homework
     * @return the student ID linked to the given homework ID
     * @throws NoSuchElementException if no homework is found with the specified ID
     */
    public String getHwStudentId(UUID hwId) {

        log.info("[SERVICE] Fetching student id");
        log.debug("[SERVICE] Fetching student id with homework id: {}", hwId);

        return homeworkRepo
                .getHwStudentId(hwId)
                .orElseThrow(() -> new NoSuchElementException("Homework not found with id: " + hwId));
    }

    /**
     * Sets the specified homework as checked using its unique identifier (UUID).
     *
     * @param hwId the unique identifier of the homework to be set as checked
     * @return true if the operation completes successfully
     */
    public Boolean setChecked(UUID hwId) {

        log.info("[SERVICE] Checking homework");
        log.debug("[SERVICE] Checking homework with id: {}", hwId);

        var hw = getHomework(hwId);
        hw.setChecked(true);
        homeworkRepo.save(hw);

        return true;
    }

    /**
     * Retrieves the pre-signed URL for a homework file based on the given ID and file name.
     *
     * @param id the unique identifier of the homework file
     * @param fileName the name of the file to fetch
     * @return the pre-signed URL for the specified file
     */
    public String getFileURL(UUID id, String fileName) {

        log.info("[SERVICE] Fetching homework file");
        log.debug("[SERVICE] Fetching homework file with id: {}, fileName: {}", id, fileName);

        return fileClient.getFilePreSignedURL(HW_BUCKET, hwFileName(id, fileName));
    }

    /**
     * Generates a formatted homework file path using the provided UUID and file name.
     *
     * @param id the unique identifier associated with the homework
     * @param fileName the name of the homework file
     * @return a formatted string representing the homework file path
     */
    private String hwFileName(UUID id, String fileName) {
        return "homework-" + id + "/" + fileName;
    }
}
