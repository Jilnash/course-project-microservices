package com.jilnash.courseservice.client;

import com.jilnash.courseservice.dto.task.TaskCreateDTO;
import com.jilnash.taskrequirementsservice.SetTaskRequirementsRequest;
import com.jilnash.taskrequirementsservice.TaskRequirementsServiceGrpc;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class TaskReqGrpcClient {

    @GrpcClient("task-requirements-client")
    private TaskRequirementsServiceGrpc.TaskRequirementsServiceBlockingStub taskReqGrpcClient;

    @Async
    public void setTaskRequirements(TaskCreateDTO task, String generatedTaskId) {
        taskReqGrpcClient.setTaskRequirements(
                SetTaskRequirementsRequest.newBuilder()
                        .setTaskId(generatedTaskId)
                        .addAllRequirements(task.getFileRequirements().stream()
                                .map(req -> com.jilnash.taskrequirementsservice.Requirement.newBuilder()
                                        .setContentType(req.contentType())
                                        .setCount(req.count())
                                        .build())
                                .toList())
                        .build()
        );
    }
}
