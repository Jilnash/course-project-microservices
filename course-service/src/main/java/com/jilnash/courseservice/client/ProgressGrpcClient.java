package com.jilnash.courseservice.client;

import com.jilnash.progressservice.InsertTaskToProgressRequest;
import com.jilnash.progressservice.ProgressServiceGrpc;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * The ProgressGrpcClient class is responsible for interacting with the ProgressService
 * gRPC server to manage and update student progress in certain course.
 * It provides functionality to update the progress of students by adding new tasks
 * to their progress records based on their completion of specified successor tasks.
 */
@Service
public class ProgressGrpcClient {

    @GrpcClient("progress-client")
    private ProgressServiceGrpc.ProgressServiceBlockingStub progressGrpcClient;

    /**
     * Updates the progress of students by including a new task for students
     * who have completed the specified successor tasks.
     *
     * @param successorTaskIds the set of task IDs representing the successors of the new task
     * @param newTaskId        the ID of the new task to be added to students' progress
     */
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
