package com.jilnash.homeworkservice.service;

import com.jilnash.courseaccessservice.CourseAccessServiceGrpc;
import com.jilnash.courseaccessservice.HasAccessRequest;
import com.jilnash.homeworkservice.client.CourseClient;
import com.jilnash.homeworkservice.client.FileClient;
import com.jilnash.homeworkservice.client.ProgressClient;
import com.jilnash.homeworkservice.model.Homework;
import com.jilnash.homeworkservice.repo.HomeworkRepo;
import com.jilnash.taskrequirementsservice.TaskRequirementsServiceGrpc;
import com.jilnash.taskrequirementsservice.ValidateRequirementsRequest;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HomeworkServiceImpl implements HomeworkService {

    private final HomeworkRepo homeworkRepo;

    private final FileClient fileClient;
    private final CourseClient courseClient;
    private final ProgressClient progressClient;

//    private final CourseAccessClient courseAccessClient;

    @GrpcClient("course-access-client")
    private CourseAccessServiceGrpc.CourseAccessServiceBlockingStub courseAccessServiceBlockingStub;

    @GrpcClient("task-requirements-client")
    private TaskRequirementsServiceGrpc.TaskRequirementsServiceBlockingStub taskRequirementsServiceBlockingStub;

    @Override
    public List<Homework> getHomeworks(String taskId, String studentId, Boolean checked, Date createdAfter) {

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

    @Override
    public Homework getHomework(Long id) {
        return homeworkRepo
                .findById(id)
                .orElseThrow(() -> new NoSuchElementException("Homework not found with id: " + id));
    }

    @Override
    public Homework saveHomework(Homework homework) {

        validateStudentHasAccessToCourse(homework.getStudentId(), homework.getTaskId());

        validateStudentCompletedTask(homework.getStudentId(), homework.getTaskId());

        //todo: validate if student completed prereqs

        validatePreviousHomeworksChecked(homework.getStudentId(), homework.getTaskId());

        validateAllTaskFilesProvided(homework);

        homework.setId(UUID.randomUUID());
        homework.setAttempt(1 + homeworkRepo.countByStudentIdAndTaskId(homework.getStudentId(), homework.getTaskId()));

        uploadHWFiles(homework);

        return homeworkRepo.save(homework);
    }

    private void validateStudentHasAccessToCourse(String studentId, String taskId) {
        //getting course_id by task_id, then verifying if student has access to course
        if (!courseAccessServiceBlockingStub.hasAccess(HasAccessRequest.newBuilder()
                .setCourseId(courseClient.getTaskCourseId(taskId))
                .setUserId(studentId)
                .build()).getHasAccess()
        )
            throw new IllegalArgumentException("Student does not have access to course");
    }

    private void validateStudentCompletedTask(String studentId, String taskId) {
        if (progressClient.isTaskCompletedByStudent(studentId, taskId))
            throw new IllegalArgumentException("Student already completed this task");
    }

    private void validatePreviousHomeworksChecked(String studentId, String taskId) {
        if (homeworkRepo.getHwUnchecked(studentId, taskId))
            throw new IllegalArgumentException("Previous homework is not checked yet");
    }

    private void validateAllTaskFilesProvided(Homework homework) {

        if (!taskRequirementsServiceBlockingStub.validateHomeworkFiles(
                        ValidateRequirementsRequest.newBuilder()
                                .setTaskId(homework.getTaskId())
                                .addAllRequirements(homework.getFiles().stream().map(MultipartFile::getContentType).toList())
                                .build())
                .getValid()
        )
            throw new IllegalArgumentException("Not all task files provided");
    }

    private void uploadHWFiles(Homework homework) {
        fileClient.uploadFile(
                "course-project-homeworks",
                "homework-" + homework.getId(),
                homework.getFiles()
        );
    }

    public String getHwTaskId(Long hwId) {
        return homeworkRepo
                .getHwTaskId(hwId)
                .orElseThrow(() -> new NoSuchElementException("Homework not found with id: " + hwId));
    }

    public String getHwStudentId(Long hwId) {
        return homeworkRepo
                .getHwStudentId(hwId)
                .orElseThrow(() -> new NoSuchElementException("Homework not found with id: " + hwId));
    }
}
