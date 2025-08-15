package com.jilnash.taskfilerequirementsservice.service;

import com.jilnash.taskfilerequirementsservice.mapper.TaskFileReqMapper;
import com.jilnash.taskfilerequirementsservice.model.TaskFileRequirement;
import com.jilnash.taskfilerequirementsservice.repo.FileRequirementsRepo;
import com.jilnash.taskfilerequirementsservice.repo.TaskFileRequirementRepo;
import com.jilnash.taskrequirementsservicedto.dto.FileReqirement;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskFileReqServiceImpl implements TaskFileReqService {

    private final TaskFileRequirementRepo taskFileRequirementRepo;

    private final FileRequirementsRepo fileRequirementsRepo;

    @Override
    public List<FileReqirement> getTaskRequirements(String taskId) {
        return taskFileRequirementRepo.findAllByTaskId(taskId)
                .stream()
                .map(TaskFileReqMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public Boolean setTaskRequirements(String taskId, List<FileReqirement> requirements) {

        taskFileRequirementRepo.updateDeleteAtByTaskId(taskId, new Date());
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

    @Override
    public Boolean checkOfRequirements(String taskId, List<String> contentTypes) {
        Map<String, Long> requiredCounts = taskFileRequirementRepo.findAllByTaskId(taskId)
                .stream()
                .collect(Collectors.groupingBy(
                        req -> req.getFileRequirement().getContentType(),
                        Collectors.summingLong(req -> (int) req.getCount())
                ));

        Map<String, Long> providedCounts = contentTypes.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        return requiredCounts.equals(providedCounts);
    }

}
