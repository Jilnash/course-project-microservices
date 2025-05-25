package com.jilnash.homeworkservice.client;

import com.jilnash.homeworkservice.model.Homework;
import com.jilnash.taskrequirementsservice.TaskRequirementsServiceGrpc;
import com.jilnash.taskrequirementsservice.ValidateRequirementsRequest;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class TaskReqsGrpcClient {

    @GrpcClient("task-requirements-client")
    private TaskRequirementsServiceGrpc.TaskRequirementsServiceBlockingStub taskRequirementsServiceBlockingStub;


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
