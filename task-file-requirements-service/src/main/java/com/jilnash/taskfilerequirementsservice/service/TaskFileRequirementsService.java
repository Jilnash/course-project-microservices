package com.jilnash.taskfilerequirementsservice.service;

import com.jilnash.taskfilerequirementsservice.dto.TaskFileReqDTO;
import com.jilnash.taskfilerequirementsservice.mapper.TaskFileReqMapper;
import com.jilnash.taskfilerequirementsservice.model.TaskFileRequirement;
import com.jilnash.taskfilerequirementsservice.repo.FileRequirementsRepo;
import com.jilnash.taskfilerequirementsservice.repo.TaskFileRequirementRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskFileRequirementsService {

    private final TaskFileRequirementRepo taskFileRequirementRepo;

    private final FileRequirementsRepo fileRequirementsRepo;

    public List<TaskFileReqDTO> getTaskRequirements(String taskId) {
        return taskFileRequirementRepo.findAllByTaskId(taskId)
                .stream()
                .map(TaskFileReqMapper::toDto)
                .toList();
    }

    @Transactional
    public Boolean setTaskRequirements(String taskId, List<TaskFileReqDTO> requirements) {

        taskFileRequirementRepo.deleteAllByTaskId(taskId);
        taskFileRequirementRepo.saveAll(
                fileRequirementsRepo
                        .findAllByContentTypeIn(requirements.stream().map(TaskFileReqDTO::contentType).toList())
                        .stream().map(requirement -> {
                            TaskFileRequirement taskFileRequirement = new TaskFileRequirement();
                            taskFileRequirement.setTaskId(taskId);
                            //todo: setting the count of the requirement
                            taskFileRequirement.setFileRequirement(requirement);
                            return taskFileRequirement;
                        }).toList()
        );

        return true;
    }

    public Boolean validateTaskRequirements(String taskId, List<String> providedContentTypes) {

        if (!providedContentTypes.stream().sorted().toList().equals(
                getTaskRequirements(
                        taskId).stream()
                        //mapping to a list of `content type` strings repeated `count` times
                        .flatMap(req -> java.util.Collections.nCopies(req.count(), req.contentType()).stream())
                        .sorted()
                        .toList()
        ))
            throw new RuntimeException("Provided content types do not match the task requirements");

        return true;
    }
}
