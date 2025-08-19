package com.jilnash.progressservice.service.rollback;

import org.springframework.transaction.annotation.Transactional;

public interface StudentProgressServiceRollback {

    @Transactional
    Boolean addStudentTaskCompleteRollback(String studentId, String taskId);
}
