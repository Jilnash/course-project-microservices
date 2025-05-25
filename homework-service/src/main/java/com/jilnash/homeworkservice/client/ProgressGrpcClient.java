package com.jilnash.homeworkservice.client;

import com.jilnash.progressservice.ProgressServiceGrpc;
import com.jilnash.progressservice.StudentTaskCompletedRequest;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * A gRPC client responsible for sending gRPC request to progress service.
 * This class uses the ProgressServiceGrpc.ProgressServiceBlockingStub
 * for making synchronous gRPC calls to the Progress service.
 */
@Component
public class ProgressGrpcClient {

    @GrpcClient(("progress-client"))
    private ProgressServiceGrpc.ProgressServiceBlockingStub progressServiceBlockingStub;

    /**
     * Validates whether the specified tasks for a given student have been completed.
     *
     * @param studentId the ID of the student whose task completion status is being validated
     * @param taskIds   the list of task IDs to be checked for completion
     * @return true if the student has completed all specified tasks, false otherwise
     */
    public boolean validateStudentCompletedTasks(String studentId, List<String> taskIds) {

        return progressServiceBlockingStub.areTasksCompleted(
                        StudentTaskCompletedRequest.newBuilder()
                                .setStudentId(studentId)
                                .addAllTaskIds(taskIds)
                                .build())
                .getIsCompleted();
    }
}