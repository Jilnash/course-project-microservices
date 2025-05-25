package com.jilnash.homeworkservice.client;

import com.jilnash.progressservice.ProgressServiceGrpc;
import com.jilnash.progressservice.StudentTaskCompletedRequest;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProgressGrpcClient {

    @GrpcClient(("progress-client"))
    private ProgressServiceGrpc.ProgressServiceBlockingStub progressServiceBlockingStub;

    public boolean validateStudentCompletedTasks(String studentId, List<String> taskIds) {

        return progressServiceBlockingStub.areTasksCompleted(
                        StudentTaskCompletedRequest.newBuilder()
                                .setStudentId(studentId)
                                .addAllTaskIds(taskIds)
                                .build())
                .getIsCompleted();
    }
}