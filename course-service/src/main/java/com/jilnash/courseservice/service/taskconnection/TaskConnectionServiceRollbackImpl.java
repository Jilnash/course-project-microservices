package com.jilnash.courseservice.service.taskconnection;

import com.jilnash.courseservice.dto.task.TaskLinkDTO;
import com.jilnash.courseservice.repo.TaskConnectionRepo;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class TaskConnectionServiceRollbackImpl implements TaskConnectionServiceRollback {

    private final TaskConnectionRepo taskConnectionRepo;

    public TaskConnectionServiceRollbackImpl(TaskConnectionRepo taskConnectionRepo) {
        this.taskConnectionRepo = taskConnectionRepo;
    }

    @Override
    public Boolean rollbackChangeTaskConnection(TaskLinkDTO before, TaskLinkDTO after) {

        taskConnectionRepo.hardDeleteTaskConnections(Set.of(after));
        taskConnectionRepo.createTaskConnections(Set.of(before));

        return true;
    }

    @Override
    public Boolean rollbackCreateTaskConnections(Set<TaskLinkDTO> links) {

        taskConnectionRepo.hardDeleteTaskConnections(links);
        return true;
    }

    @Override
    public Boolean rollbackSoftDeleteTaskConnections(Set<TaskLinkDTO> links) {

        taskConnectionRepo.softDeleteTaskConnectionsRollback(links);
        return true;
    }
}
