package com.jilnash.progressservice.listener;

import com.jilnash.progressservice.service.rollback.StudentProgressServiceRollback;
import com.jilnash.progressservicedto.dto.AddStudentTaskCompleteDTO;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class StudentProgressRollbackListener {

    private final StudentProgressServiceRollback studentProgressServiceRollback;

    public StudentProgressRollbackListener(StudentProgressServiceRollback studentProgressServiceRollback) {
        this.studentProgressServiceRollback = studentProgressServiceRollback;
    }

    @KafkaListener(topics = "add-student-task-complete-rollback-topic", groupId = "progress-service")
    public void handleInsertProgressRollback(AddStudentTaskCompleteDTO dto) {
        studentProgressServiceRollback.addStudentTaskCompleteRollback(dto.studentId(), dto.taskId());
    }
}
