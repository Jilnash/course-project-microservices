package com.jilnash.homeworkservice.service;

import com.jilnash.homeworkservice.model.Homework;
import com.jilnash.homeworkservice.model.HomeworkFile;
import com.jilnash.homeworkservice.repo.HomeworkRepo;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
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

    private final HomeworkFileService homeworkFileService;

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
                .orElseThrow(() -> new EntityNotFoundException("Homework not found with id: " + id));
    }

    @Override
    public String getHomeworkStudentId(UUID id) {
        return homeworkRepo.getHwStudentId(id)
                .orElseThrow(() -> new EntityNotFoundException("Homework not found with id: " + id));
    }

    @Override
    public String getHomeworkTaskId(UUID id) {
        return homeworkRepo.getHwTaskId(id)
                .orElseThrow(() -> new EntityNotFoundException("Homework not found with id: " + id));
    }

    @Override
    public Boolean isHomeworkChecked(UUID id) {
        return getHomework(id).getChecked();
    }

    @Override
    public Integer getHomeworkAttempt(UUID id) {
        return getHomework(id).getAttempt();
    }

    @Override
    public Date getHomeworkCreatedAt(UUID id) {
        return getHomework(id).getCreatedAt();
    }

    @Override
    public List<String> getHomeworkFileNames(UUID id) {
        return getHomework(id)
                .getHwFiles()
                .stream()
                .map(HomeworkFile::getFileName)
                .toList();
    }

    @Override
    public Boolean setHomeworkChecked(UUID id) {
        homeworkRepo.updateIsChecked(id, true);
        return true;
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
    public Boolean createHomework(Homework homework) {

        log.info("[SERVICE] Creating homework");
        log.debug("[SERVICE] Creating homework to taskId {} by studentId: {}",
                homework.getTaskId(), homework.getStudentId());

        // checking if previously sent homework is checked
        if (homeworkRepo.getHwUnchecked(homework.getStudentId(), homework.getTaskId()))
            throw new IllegalArgumentException("Previous homework is not checked yet");

        homework.setAttempt(1 + homeworkRepo.countByStudentIdAndTaskId(homework.getStudentId(), homework.getTaskId()));

        homeworkFileService.createdHomeworkFiles(homeworkRepo.save(homework));

        return true;
    }

    @Override
    public Boolean softDeleteHomework(UUID id) {
        return homeworkRepo.findById(id)
                .map(hw -> {
                    hw.setDeletedAt(new Date(System.currentTimeMillis()));
                    homeworkRepo.save(hw);
                    return true;
                })
                .orElseThrow(() -> new EntityNotFoundException("Homework not found with id: " + id));
    }

    @Override
    public Boolean hardDeleteHomework(UUID id) {
        log.info("[SERVICE] Hard deleting homework with id: {}", id);
        homeworkRepo.deleteById(id);
        return true;
    }
}
