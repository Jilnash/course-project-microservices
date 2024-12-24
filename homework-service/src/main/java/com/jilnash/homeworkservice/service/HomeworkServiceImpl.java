package com.jilnash.homeworkservice.service;

import com.jilnash.homeworkservice.client.CourseAccessClient;
import com.jilnash.homeworkservice.client.CourseClient;
import com.jilnash.homeworkservice.client.FileClient;
import com.jilnash.homeworkservice.client.ProgressClient;
import com.jilnash.homeworkservice.model.Homework;
import com.jilnash.homeworkservice.repo.HomeworkRepo;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Date;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class HomeworkServiceImpl implements HomeworkService {

    private final HomeworkRepo homeworkRepo;

    private final FileClient fileClient;
    private final CourseClient courseClient;
    private final ProgressClient progressClient;
    private final CourseAccessClient courseAccessClient;

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

        uploadFiles(homework);

        return homeworkRepo.save(homework);
    }

    private void validateStudentHasAccessToCourse(String studentId, String taskId) {
        //getting course_id by task_id, then verifying if student has access to course
        if (!courseAccessClient.getStudentHasAccess(studentId, courseClient.getTaskCourseId(taskId)))
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

        List<String> requiredFiles = courseClient.getTaskRequirements(homework.getTaskId());

        if (homework.getImageFile() == null && requiredFiles.contains("image"))
            throw new IllegalArgumentException("Image file is required");

        if (homework.getAudioFile() == null && requiredFiles.contains("audio"))
            throw new IllegalArgumentException("Audio file is required");

        if (homework.getVideoFile() == null && requiredFiles.contains("video"))
            throw new IllegalArgumentException("Video file is required");
    }

    private void uploadFiles(Homework homework) {

        String fileNamePrefix = String.format("student-%s/task-%s/attempt-%s/",
                homework.getStudentId(), homework.getTaskId(), 1); //todo: get attempt number

        String bucket = "course-project-homeworks";

        uploadFileIfPresent(homework.getImageFile(), bucket, fileNamePrefix + "images/");
        uploadFileIfPresent(homework.getAudioFile(), bucket, fileNamePrefix + "audios/");
        uploadFileIfPresent(homework.getVideoFile(), bucket, fileNamePrefix + "videos/");
    }

    private void uploadFileIfPresent(MultipartFile file, String bucket, String filePathPrefix) {
        if (file != null)
            fileClient.uploadFile(
                    bucket,
                    filePathPrefix + file.getOriginalFilename(),
                    file
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
