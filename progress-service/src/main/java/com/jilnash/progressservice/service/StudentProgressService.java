package com.jilnash.progressservice.service;

import java.util.List;

public interface StudentProgressService {

    List<String> getStudentCompletedTaskIds(String studentId);

    Boolean getIfStudentCompletedTasks(String studentId, List<String> taskIds);

    Boolean addStudentTaskComplete(String studentId, String taskId);
}
