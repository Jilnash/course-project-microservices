package com.jilnash.courseservice.service.courseauthr;

import com.jilnash.courseaccessservice.CourseAccessServiceGrpc;
import com.jilnash.courseaccessservice.HasAccessRequest;
import com.jilnash.courserightsservice.HasRightsRequest;
import com.jilnash.courserightsservice.TeacherRightsServiceGrpc;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
public class CourseAuthorizationService {

    @GrpcClient("course-access-client")
    private CourseAccessServiceGrpc.CourseAccessServiceBlockingStub courseAccessServiceBlockingStub;

    @GrpcClient("course-rights-client")
    private TeacherRightsServiceGrpc.TeacherRightsServiceBlockingStub teacherRightsServiceBlockingStub;

    public void validateUserAccess(String courseId, String userId) {

        try {
            if (!getStudentCourseAccess(courseId, userId).get() &&
                    !getTeacherHasCourseRights(courseId, userId, List.of("READ")).get())
                throw new RuntimeException("Access validation failed");
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Execution error while checking user access", e);
        }
    }

    public void validateTeacherCourseRights(String courseId, String teacherId, List<String> rights) {

        try {
            if (!getTeacherHasCourseRights(courseId, teacherId, rights).get())
                throw new UsernameNotFoundException("Teacher does not have rights: " + rights);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Execution error while checking teacher rights", e);
        }
    }

    @Async
    protected CompletableFuture<Boolean> getTeacherHasCourseRights(String courseId, String teacherId, List<String> rights) {

        log.info("[EXTERNAL] Checking teacher rights with course-rights-service rpc");

        return CompletableFuture.supplyAsync(() -> teacherRightsServiceBlockingStub.hasRights(
                        HasRightsRequest.newBuilder()
                                .setCourseId(courseId)
                                .setTeacherId(teacherId)
                                .addAllRights(rights)
                                .build())
                .getHasRights());
    }

    public void validateStudentCourseAccess(String courseId, String studentId) {

        try {
            if (!getStudentCourseAccess(courseId, studentId).get())
                throw new UsernameNotFoundException("Student does not have access to course");
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Execution error while checking student access", e);
        }
    }

    @Async
    protected CompletableFuture<Boolean> getStudentCourseAccess(String courseId, String studentId) {

        log.info("[EXTERNAL] Checking student access with course-access-service rpc");

        return CompletableFuture.supplyAsync(() -> courseAccessServiceBlockingStub.hasAccess(
                        HasAccessRequest.newBuilder()
                                .setCourseId(courseId)
                                .setUserId(studentId)
                                .build()).
                getHasAccess());
    }
}
