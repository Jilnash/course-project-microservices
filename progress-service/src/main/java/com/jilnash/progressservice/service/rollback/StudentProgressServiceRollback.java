package com.jilnash.progressservice.service.rollback;

public interface StudentProgressServiceRollback {

    Boolean addStudentTaskCompleteRollback(String studentId, String taskId);
}
