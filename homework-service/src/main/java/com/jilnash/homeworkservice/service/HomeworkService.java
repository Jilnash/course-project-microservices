package com.jilnash.homeworkservice.service;

import com.jilnash.homeworkservice.model.Homework;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.List;
import java.util.UUID;

public interface HomeworkService {

    List<Homework> getHomeworks(String taskId, String studentId, Boolean checked, Date createdAfter);

    Homework getHomework(UUID id);

    String getHomeworkStudentId(UUID id);

    String getHomeworkTaskId(UUID id);

    Boolean isHomeworkChecked(UUID id);

    Integer getHomeworkAttempt(UUID id);

    Date getHomeworkCreatedAt(UUID id);

    List<String> getHomeworkFileNames(UUID id);

    @Transactional
    Boolean setHomeworkChecked(UUID id);

    Boolean createHomework(Homework homework);

    Boolean softDeleteHomework(UUID id);

    Boolean hardDeleteHomework(UUID id);
}
