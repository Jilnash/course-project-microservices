package com.jilnash.taskfilerequirementsservice.service;

import com.jilnash.taskfilerequirementsservice.dto.TaskFileReqDTO;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public interface TaskFileReqServiceRollback {

    /**
     * Rollback method to handle task file requirement changes.
     *
     * @param taskId the ID of the task for which the rollback is being performed
     * @return true if the rollback was successful, false otherwise
     */
    @Transactional
    Boolean setTaskRequirementsRollback(String taskId, List<TaskFileReqDTO> requirements);
}
