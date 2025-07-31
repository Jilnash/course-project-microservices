package com.jilnash.taskfilerequirementsservice.service;

import com.jilnash.taskrequirementsservicedto.dto.FileReqirement;

import java.util.List;

public interface TaskFileReqService {

    /**
     * Retrieves the file requirements for a specific task.
     *
     * @param taskId the ID of the task
     * @return a list of TaskFileReqDTO containing the file requirements
     */
    List<FileReqirement> getTaskRequirements(String taskId);

    /**
     * Sets the file requirements for a specific task.
     *
     * @param taskId       the ID of the task
     * @param requirements a list of FileRequirement containing the file requirements to set
     * @return true if the operation was successful, false otherwise
     */
    Boolean setTaskRequirements(String taskId, List<FileReqirement> requirements);
}
