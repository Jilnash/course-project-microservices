package com.jilnash.hwresponseservice.clients;

import com.jilnash.progressservice.AddTaskToProgressRequest;
import com.jilnash.progressservice.ProgressServiceGrpc;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class ProgressGrpcClient {

    @GrpcClient("progress-grpc-client")
    private ProgressServiceGrpc.ProgressServiceBlockingStub progressServiceGrpc;


    @Async
    public void addTaskToStudentProgress(String studentId, String taskId) {
        progressServiceGrpc.addTaskToProgress(
                AddTaskToProgressRequest.newBuilder()
                        .setStudentId(studentId)
                        .setTaskId(taskId)
                        .build()
        );
    }
}
