package com.jilnash.courseaccessservice.grpc;

import com.jilnash.courseaccessservice.CourseAccessServiceGrpc;
import com.jilnash.courseaccessservice.HasAccessRequest;
import com.jilnash.courseaccessservice.HasAccessResponse;
import com.jilnash.courseaccessservice.service.StudentCourseAccessService;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;

@Slf4j
@GrpcService
@RequiredArgsConstructor
public class CourseAccessGrpcService extends CourseAccessServiceGrpc.CourseAccessServiceImplBase {

    private final StudentCourseAccessService studentCourseAccessService;

    @Override
    public void hasAccess(HasAccessRequest request, StreamObserver<HasAccessResponse> responseObserver) {

        log.info("[GRPC] Getting student has access to course");
        log.debug("[GRPC] Getting studentId: {}, has access to courseId: {}",
                request.getUserId(), request.getCourseId());

        HasAccessResponse response = HasAccessResponse.newBuilder()
                .setHasAccess(studentCourseAccessService.getStudentHasAccess(request.getUserId(), request.getCourseId()))
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
