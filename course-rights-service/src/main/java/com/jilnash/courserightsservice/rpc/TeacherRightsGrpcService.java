package com.jilnash.courserightsservice.rpc;

import com.jilnash.courserightsservice.*;
import com.jilnash.courserightsservice.service.TeacherRightsService;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.HashSet;

@Slf4j
@GrpcService
@RequiredArgsConstructor
public class TeacherRightsGrpcService extends TeacherRightsServiceGrpc.TeacherRightsServiceImplBase {

    private final TeacherRightsService rightsService;

    @Override
    public void hasRights(HasRightsRequest request, StreamObserver<HasRightsResponse> responseObserver) {

        log.info("[RPC] Checking rights of teacher");
        log.debug("[RPC] Checking rights of teacher: {}, in course: {}, for rights: {}",
                request.getTeacherId(), request.getCourseId(), request.getRightsList());

        boolean hasRights = rightsService.hasRights(request.getCourseId(), request.getTeacherId(),
                new HashSet<>(request.getRightsList()));

        responseObserver.onNext(HasRightsResponse.newBuilder().setHasRights(hasRights).build());
        responseObserver.onCompleted();
    }

    @Override
    public void setCourseOwner(SetCourseOwnerRequest request, StreamObserver<SetCourseOwnerResponse> responseObserver) {

        log.info("[RPC] Setting course owner");
        log.debug("[RPC] Setting course owner for course: {}, teacher: {}",
                request.getCourseId(), request.getTeacherId());

        responseObserver.onNext(SetCourseOwnerResponse.newBuilder()
                .setSuccess(rightsService.createCourseOwner(request.getCourseId(), request.getTeacherId()))
                .build());
        responseObserver.onCompleted();
    }
}
