package com.jilnash.homeworkservice.service;

import com.jilnash.homeworkservice.repo.HomeworkRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class HomeworkServiceRollbackImpl implements HomeworkServiceRollback {

    private final HomeworkRepo homeworkRepo;

    @Override
    public Boolean homeworkCreateRollback(String studentId, String taskId, Integer attempt) {

        log.info("Rollback homework creation for studentId: {}, taskId: {}, attempt: {}", studentId, taskId, attempt);
        homeworkRepo.deleteHomework(studentId, taskId, attempt);

        return true;
    }

    @Override
    public Boolean homeworkCheckedRollback(UUID hwId) {

        log.info("Rollback homework checked for hwId: {}", hwId);
        homeworkRepo.updateIsChecked(hwId, false);

        return false;
    }
}
