package com.jilnash.homeworkservice.client;

import com.jilnash.homeworkservice.model.Homework;
import com.jilnash.taskrequirementsservice.TaskRequirementsServiceGrpc;
import com.jilnash.taskrequirementsservice.ValidateRequirementsRequest;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * TaskReqsGrpcClient is a gRPC client component responsible for sending requests to task-requirements service.
 * It connects to a remote gRPC service defined by TaskRequirementsServiceGrpc.
 */
@Component
public class TaskReqsGrpcClient {

    @GrpcClient("task-requirements-client")
    private TaskRequirementsServiceGrpc.TaskRequirementsServiceBlockingStub taskRequirementsServiceBlockingStub;


    /**
     * Validates whether all required files for the given homework task are provided.
     * Sends a request to the remote gRPC service to verify the file types against the task requirements.
     * If not all required files are provided, an IllegalArgumentException is thrown.
     *
     * @param homework the homework object containing the task ID and a list of uploaded files
     *                 corresponding to the required task files
     */
    public void validateAllTaskFilesProvided(Homework homework) {

        if (!taskRequirementsServiceBlockingStub.validateHomeworkFiles(
                        ValidateRequirementsRequest.newBuilder()
                                .setTaskId(homework.getTaskId())
                                .addAllRequirements(homework.getFiles().stream().map(MultipartFile::getContentType).toList())
                                .build())
                .getValid()
        )
            throw new IllegalArgumentException("Not all task files provided");
    }
}
