package com.jilnash.progressservice.rpc;

import com.jilnash.progressservice.*;
import com.jilnash.progressservice.service.StudentTaskCompleteService;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
public class ProgressGrpcService extends ProgressServiceGrpc.ProgressServiceImplBase {

    private final StudentTaskCompleteService studentTaskCompleteService;

    @Override
    public void areTasksCompleted(StudentTaskCompletedRequest request, StreamObserver<StudentTaskCompletedResponse> response) {

        response.onNext(StudentTaskCompletedResponse.newBuilder()
                .setIsCompleted(studentTaskCompleteService.areTasksCompleted(
                        request.getStudentId(), request.getTaskIdsList().stream().toList())
                ).build());
        response.onCompleted();
    }

    @Override
    public void addTaskToProgress(AddTaskToProgressRequest request, StreamObserver<AddTaskToProgressResponse> response) {
        response.onNext(AddTaskToProgressResponse.newBuilder()
                .setAdded(studentTaskCompleteService.completeTask(request.getStudentId(), request.getTaskId()))
                .build());
        response.onCompleted();
    }
}
