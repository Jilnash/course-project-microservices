package com.jilnash.courseservice.service.taskconnection;

import com.jilnash.courseservice.dto.task.TaskLinkDTO;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public interface TaskConnectionServiceRollback {

    Boolean rollbackChangeTaskConnection(TaskLinkDTO before, TaskLinkDTO after);

    Boolean rollbackCreateTaskConnections(Set<TaskLinkDTO> links);

    Boolean rollbackSoftDeleteTaskConnections(Set<TaskLinkDTO> links);
}
