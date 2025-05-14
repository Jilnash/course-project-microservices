package com.jilnash.courseservice.client;

import com.jilnash.progressservice.InsertTaskToProgressRequest;
import com.jilnash.progressservice.ProgressServiceGrpc;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class ProgressGrpcClient {

    @GrpcClient("progress-client")
    private ProgressServiceGrpc.ProgressServiceBlockingStub progressGrpcClient;

    @Async
    public void updateProgresses(Set<String> successorTaskIds, String newTaskId) {
        // the new task should be included in progress
        // of all students who have completed successors the new task
        progressGrpcClient.insertTaskToProgress(InsertTaskToProgressRequest.newBuilder()
                .setNewTaskId(newTaskId)
                .addAllCompletedTaskIds(successorTaskIds)
                .build()
        );
    }

}
