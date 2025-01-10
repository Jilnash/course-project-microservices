package com.jilnash.courseaccessservice.grpc;

import com.jilnash.courseaccessservice.CourseAccessServiceGrpc;
import com.jilnash.courseaccessservice.HasAccessRequest;
import com.jilnash.courseaccessservice.HasAccessResponse;
import com.jilnash.courseaccessservice.service.StudentCourseAccessService;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
public class CourseAccessGrpcService extends CourseAccessServiceGrpc.CourseAccessServiceImplBase {

    private final StudentCourseAccessService studentCourseAccessService;

    @Override
    public void hasAccess(HasAccessRequest request, StreamObserver<HasAccessResponse> responseObserver) {
        HasAccessResponse response = HasAccessResponse.newBuilder()
                .setHasAccess(studentCourseAccessService.getStudentHasAccess(request.getUserId(), request.getCourseId()))
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
