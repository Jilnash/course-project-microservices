package com.jilnash.taskfilerequirementsservice.service;

import com.jilnash.taskfilerequirementsservice.repo.TaskFileRequirementRepo;
import com.jilnash.taskrequirementsservicedto.dto.FileReqirement;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TaskFileReqRollbackImpl implements TaskFileReqServiceRollback {

    private final TaskFileRequirementRepo taskFileRequirementRepo;

    public TaskFileReqRollbackImpl(TaskFileRequirementRepo taskFileRequirementRepo) {
        this.taskFileRequirementRepo = taskFileRequirementRepo;
    }

    @Override
    public Boolean setTaskRequirementsRollback(String taskId, List<FileReqirement> requirements) {

        try {
            taskFileRequirementRepo.deleteAllByTaskIdAndDeletedAtIsNotNull(taskId);
            taskFileRequirementRepo.updateDeleteAtByTaskId(taskId, null);
            return true;
        } catch (Exception e) {
            // Log the exception or handle it as needed
            return false;
        }
    }

    @Override
    public void createTaskRequirementsRollback(String taskId) {
        taskFileRequirementRepo.deleteAllByTaskId(taskId);
    }
}
