package com.jilnash.courseservice.client;

import com.jilnash.courseservice.dto.task.TaskCreateDTO;
import com.jilnash.taskrequirementsservice.SetTaskRequirementsRequest;
import com.jilnash.taskrequirementsservice.TaskRequirementsServiceGrpc;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * A gRPC client service responsible for interacting with the Task Requirements Service.
 * This service sends task requirements in an asynchronous manner to the Task Requirements Service.
 */
@Service
public class TaskReqGrpcClient {

    @GrpcClient("task-requirements-client")
    private TaskRequirementsServiceGrpc.TaskRequirementsServiceBlockingStub taskReqGrpcClient;

    /**
     * Sends task requirements asynchronously to the Task Requirements Service.
     *
     * @param task            the task details, including requirements, being created
     * @param generatedTaskId the unique identifier generated for the task
     */
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
