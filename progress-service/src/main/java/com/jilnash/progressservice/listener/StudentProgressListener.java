package com.jilnash.progressservice.listener;

import com.jilnash.progressservice.service.StudentProgressService;
import com.jilnash.progressservicedto.dto.AddStudentTaskCompleteDTO;
import com.jilnash.progressservicedto.dto.CheckStudentCompletedTasks;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class StudentProgressListener {

    private final StudentProgressService studentProgressService;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public StudentProgressListener(StudentProgressService studentProgressService, KafkaTemplate<String, String> kafkaTemplate) {
        this.studentProgressService = studentProgressService;
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = "add-student-task-complete-topic", groupId = "progress-service")
    public void handleInsertProgress(AddStudentTaskCompleteDTO dto) {
        studentProgressService.addStudentTaskComplete(dto.studentId(), dto.taskId());
    }

    @KafkaListener(topics = "check-student-already-completed-tasks-topic", groupId = "progress-service")
    public void handleCheckStudentCompletedTasks(CheckStudentCompletedTasks dto) {
        Boolean isCompleted = studentProgressService.getIfStudentCompletedTasks(dto.studentId(), dto.taskIds());
        if (isCompleted)
            kafkaTemplate.send("rollback-topic", dto.transactionId());
    }
}
