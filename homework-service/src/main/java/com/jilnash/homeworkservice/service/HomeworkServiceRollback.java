package com.jilnash.homeworkservice.service;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
public interface HomeworkServiceRollback {

    @Transactional
    Boolean homeworkCreateRollback(String studentId, String taskId, Integer attempt);

    @Transactional
    Boolean homeworkCheckedRollback(UUID hwId);
}
