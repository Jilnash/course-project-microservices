package com.jilnash.courseservice.service.taskconnection;

import com.jilnash.courseservice.dto.task.TaskLinkDTO;
import com.jilnash.courseservice.repo.TaskConnectionRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class TaskConnectionServiceImpl implements TaskConnectionService {

    private final TaskConnectionRepo taskConnectionRepo;

    @Override
    public void changeTaskConnection(TaskLinkDTO before, TaskLinkDTO after) {

        if (before.from().equals(before.to()) || after.from().equals(after.to()))
            throw new IllegalArgumentException("Cannot change connection to the same task");

        if (before.from().equals(after.from()) && before.to().equals(after.to()))
            throw new IllegalArgumentException("No change in task connection detected");

        if (!taskConnectionRepo.getIfConnectionsExists(Set.of(before)))
            throw new IllegalArgumentException("Connection does not exist between " +
                    before.from() + " and " + before.to());

        if (taskConnectionRepo.getIfConnectionsExists(Set.of(after)))
            throw new IllegalArgumentException("Connection already exists between " +
                    after.from() + " and " + after.to());

        taskConnectionRepo.hardDeleteTaskConnections(Set.of(before));
        taskConnectionRepo.createTaskConnections(Set.of(after));
    }

    @Override
    public void createTaskConnections(Set<TaskLinkDTO> taskLinks) {
        if (taskLinks.isEmpty())
            throw new IllegalArgumentException("No task connections to create");

        if (taskLinks.stream().anyMatch(link -> link.from().equals(link.to())))
            throw new IllegalArgumentException("Cannot create connection to the same task");

        taskConnectionRepo.createTaskConnections(taskLinks);
    }

    @Override
    public void softDeleteTaskConnections(Set<TaskLinkDTO> taskLinks) {
        if (taskLinks.isEmpty())
            throw new IllegalArgumentException("No task connections to soft delete");

        if (taskLinks.stream().anyMatch(link -> link.from().equals(link.to())))
            throw new IllegalArgumentException("Cannot soft delete connection to the same task");

        if (!taskConnectionRepo.getIfConnectionsExists(taskLinks))
            throw new IllegalArgumentException("No existing connections to soft delete");

        taskConnectionRepo.softDeleteTaskConnections(taskLinks);
    }

    @Override
    public void hardDeleteTaskConnections(Set<TaskLinkDTO> taskLinks) {
        if (taskLinks.isEmpty())
            throw new IllegalArgumentException("No task connections to delete");

        if (taskLinks.stream().anyMatch(link -> link.from().equals(link.to())))
            throw new IllegalArgumentException("Cannot delete connection to the same task");

        if (!taskConnectionRepo.getIfConnectionsExists(taskLinks))
            throw new IllegalArgumentException("No existing connections to delete");

        taskConnectionRepo.hardDeleteTaskConnections(taskLinks);
    }
}
