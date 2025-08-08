package com.jilnash.homeworkservice.service;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
public interface HomeworkServiceRollback {

    @Transactional
    Boolean homeworkCreateRollback(UUID id);

    @Transactional
    Boolean homeworkCheckedRollback(UUID hwId);

    @Transactional
    void softDeleteHomeworkRollack(UUID id);
}
