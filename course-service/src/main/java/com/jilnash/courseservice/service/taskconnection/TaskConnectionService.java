package com.jilnash.courseservice.service.taskconnection;

import com.jilnash.courseservice.dto.task.TaskLinkDTO;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public interface TaskConnectionService {

    void changeTaskConnection(TaskLinkDTO before, TaskLinkDTO after);

    void createTaskConnections(Set<TaskLinkDTO> taskLinkDTOs);

    void softDeleteTaskConnections(Set<TaskLinkDTO> taskLinkDTOs);

    void hardDeleteTaskConnections(Set<TaskLinkDTO> taskLinkDTOs);
}