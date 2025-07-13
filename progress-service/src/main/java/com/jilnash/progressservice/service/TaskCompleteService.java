package com.jilnash.progressservice.service;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface TaskCompleteService {

    Boolean insertTask(String taskId, List<String> completedTaskIds);

    @Transactional
    Boolean softDeleteTasks(List<String> taskId);

    @Transactional
    Boolean hardDeleteTasks(List<String> taskId);
}
