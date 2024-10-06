package com.jilnash.homeworkservice.service;

import com.jilnash.homeworkservice.model.Homework;

import java.sql.Date;
import java.util.List;

public interface HomeworkService {

    List<Homework> getHomeworks(String taskId, String studentId, Boolean checked, Date createdAfter);

    Homework getHomework(Long id);

    Homework saveHomework(Homework homework);
}
