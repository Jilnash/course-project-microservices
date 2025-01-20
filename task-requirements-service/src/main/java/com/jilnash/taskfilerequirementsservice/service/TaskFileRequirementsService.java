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
                requirements.stream().map(requirement ->
                        TaskFileRequirement.builder()
                                .taskId(taskId)
                                .fileRequirement(fileRequirementsRepo
                                        .findByContentType(requirement.contentType())
                                        .orElseThrow(() -> new RuntimeException("File requirement not found")))
                                .count((byte) requirement.count())
                                .build()
                ).toList()
        );

        return true;
    }
}
