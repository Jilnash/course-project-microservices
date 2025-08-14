package com.jilnash.progressservice.service.rollback;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface TaskCompleteServiceRollback {

    /**
     * Rollback the insertion of a task completion record.
     *
     * @param taskId The ID of the task to rollback.
     * @return true if the rollback was successful, false otherwise.
     */
    @Transactional
    Boolean insertTaskRollback(String taskId);

    /**
     * Rollback the soft deletion of tasks.
     *
     * @param taskIds The list of task IDs to rollback.
     * @return true if the rollback was successful, false otherwise.
     */
    Boolean softDeleteTasksRollback(List<String> taskIds);
}
