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
import java.util.stream.Stream;

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
        List<String> requiredTypes = taskFileRequirementRepo.findAllByTaskId(taskId)
                .stream()
                .flatMap(req ->
                        Stream.of(req.getFileRequirement().getContentType()).limit(req.getCount()))
                .toList();
        System.out.println(contentTypes);
        System.out.println(requiredTypes);
        boolean res = requiredTypes.containsAll(contentTypes) && contentTypes.containsAll(requiredTypes);
        System.out.println(res);
        return res;
    }
}
