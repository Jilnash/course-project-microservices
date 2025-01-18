package com.jilnash.taskfilerequirementsservice.rpc;

import com.jilnash.taskfilerequirementsservice.dto.TaskFileReqDTO;
import com.jilnash.taskfilerequirementsservice.service.TaskFileRequirementsService;
import com.jilnash.taskrequirementsservice.*;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
public class TaskRequirementService extends TaskRequirementsServiceGrpc.TaskRequirementsServiceImplBase {

    private final TaskFileRequirementsService taskRequirementService;

    @Override
    public void validateHomeworkFiles(ValidateRequirementsRequest request, StreamObserver<ValidateRequirementsResponse> responseObserver) {

        // if the provided content types do not match the task
        boolean allFilesValid = request.getRequirementsList().stream().sorted().toList()
                .equals(
                        taskRequirementService.getTaskRequirements(request.getTaskId()).stream()
                                //mapping to a list of `content type` strings repeated `count` times
                                .flatMap(req -> java.util.Collections.nCopies(req.count(), req.contentType()).stream())
                                .sorted()
                                .toList());

        responseObserver.onNext(ValidateRequirementsResponse.newBuilder().setValid(allFilesValid).build());
        responseObserver.onCompleted();
    }

    @Override
    public void setTaskRequirements(SetTaskRequirementsRequest request, StreamObserver<SetTaskRequirementsResponse> responseObserver) {

        taskRequirementService.setTaskRequirements(
                request.getTaskId(),
                request.getRequirementsList().stream().map(req -> TaskFileReqDTO.builder()
                                .contentType(req.getContentType())
                                .count((short) req.getCount())
                                .build())
                        .toList()
        );

        responseObserver.onNext(SetTaskRequirementsResponse.newBuilder().build());
        responseObserver.onCompleted();
    }
}
