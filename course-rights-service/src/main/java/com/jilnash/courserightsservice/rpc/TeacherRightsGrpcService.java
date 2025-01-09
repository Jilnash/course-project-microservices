package com.jilnash.courserightsservice.rpc;

import com.jilnash.courserightservice.HasRightsRequest;
import com.jilnash.courserightservice.HasRightsResponse;
import com.jilnash.courserightservice.TeacherRightsServiceGrpc;
import com.jilnash.courserightsservice.service.TeacherRightsService;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.HashSet;

@GrpcService
@RequiredArgsConstructor
public class TeacherRightsGrpcService extends TeacherRightsServiceGrpc.TeacherRightsServiceImplBase {

    private final TeacherRightsService rightsService;

    @Override
    public void hasRights(HasRightsRequest request, StreamObserver<HasRightsResponse> responseObserver) {

        boolean hasRights = rightsService.hasRights(request.getCourseId(), request.getTeacherId(),
                new HashSet<>(request.getRightsList()));

        responseObserver.onNext(HasRightsResponse.newBuilder().setHasRights(hasRights).build());
        responseObserver.onCompleted();
    }
}
