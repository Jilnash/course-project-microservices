package com.jilnash.homeworkservice.service;

import com.jilnash.homeworkservice.model.Homework;

import java.sql.Date;
import java.util.List;
import java.util.UUID;

public interface HomeworkService {

    List<Homework> getHomeworks(String taskId, String studentId, Boolean checked, Date createdAfter);

    Homework getHomework(UUID id);

    Boolean saveHomework(Homework homework);
}
