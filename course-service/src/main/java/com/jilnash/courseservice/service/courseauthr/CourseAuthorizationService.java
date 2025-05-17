package com.jilnash.courseservice.service.courseauthr;

import com.jilnash.courseaccessservice.CourseAccessServiceGrpc;
import com.jilnash.courseaccessservice.HasAccessRequest;
import com.jilnash.courserightsservice.HasRightsRequest;
import com.jilnash.courserightsservice.TeacherRightsServiceGrpc;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Context;
import io.opentelemetry.context.Scope;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * Service responsible for managing and validating user access and rights to courses.
 * Utilizes gRPC clients to communicate with external services for student access and teacher rights validation.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CourseAuthorizationService {

    private final Tracer tracer;

    @GrpcClient("course-access-client")
    private CourseAccessServiceGrpc.CourseAccessServiceBlockingStub courseAccessServiceBlockingStub;

    @GrpcClient("course-rights-client")
    private TeacherRightsServiceGrpc.TeacherRightsServiceBlockingStub teacherRightsServiceBlockingStub;


    /**
     * Validates the access rights of a user for the specified course. The method checks if the user
     * has student-level access to the course or teacher-level rights with at least "READ" permissions.
     * If neither condition is met, an exception is thrown.
     *
     * @param courseId the unique identifier of the course
     * @param userId   the unique identifier of the user
     * @throws RuntimeException if access validation fails or if an error occurs while checking access rights
     */
    public void validateUserAccess(String courseId, String userId) {

        try {
            if (!getStudentCourseAccess(courseId, userId).get() &&
                    !getTeacherHasCourseRights(courseId, userId, List.of("READ")).get())
                throw new RuntimeException("Access validation failed");
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Execution error while checking user access", e);
        }
    }

    /**
     * Validates whether a teacher has the specified rights for a given course. If the validation fails, a
     * {@link UsernameNotFoundException} is thrown. Handles any execution errors during the validation
     * process by wrapping them in a {@link RuntimeException}.
     *
     * @param courseId  the unique identifier of the course
     * @param teacherId the unique identifier of the teacher
     * @param rights    a list of rights to check for the teacher
     * @throws UsernameNotFoundException if the teacher does not have the specified rights for the course
     * @throws RuntimeException          if an error occurs during the execution of the rights validation
     */
    public void validateTeacherCourseRights
    (String courseId, String teacherId, List<String> rights) {

        try {
            if (!getTeacherHasCourseRights(courseId, teacherId, rights).get())
                throw new UsernameNotFoundException("Teacher does not have rights: " + rights);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Execution error while checking teacher rights", e);
        }
    }

    /**
     * Checks if a teacher has specific rights for a given course by interacting with the course-rights-service
     * through an asynchronous gRPC call.
     *
     * @param courseId the unique identifier of the course
     * @param teacherId the unique identifier of the teacher
     * @param rights a list of rights to check for the teacher
     * @return a CompletableFuture containing a Boolean value indicating whether the teacher has the specified rights
     */
    @Async
    protected CompletableFuture<Boolean> getTeacherHasCourseRights(String courseId, String teacherId, List<String> rights) {
        Span span = tracer.spanBuilder("getTeacherHasCourseRights").startSpan();

        // Attach current span to the new thread using OpenTelemetry Context
        try (Scope scope = span.makeCurrent()) {
            log.info("[EXTERNAL] Checking teacher rights with course-rights-service rpc");

            return CompletableFuture.supplyAsync(() -> {
                try (Scope innerScope = Context.current().makeCurrent()) {
                    return teacherRightsServiceBlockingStub.hasRights(
                                    HasRightsRequest.newBuilder()
                                            .setCourseId(courseId)
                                            .setTeacherId(teacherId)
                                            .addAllRights(rights)
                                            .build())
                            .getHasRights();
                }
            });
        } finally {
            span.end();
        }
    }

    /**
     * Validates if a student has access to a specific course. If the access check fails, a
     * {@link UsernameNotFoundException} is thrown. Handles any execution errors during the validation
     * process by wrapping them in a {@link RuntimeException}.
     *
     * @param courseId the unique identifier of the course
     * @param studentId the unique identifier of the student
     * @throws UsernameNotFoundException if the student does not have access to the course
     * @throws RuntimeException if an error occurs during the execution of the access validation
     */
    public void validateStudentCourseAccess(String courseId, String studentId) {

        try {
            if (!getStudentCourseAccess(courseId, studentId).get())
                throw new UsernameNotFoundException("Student does not have access to course");
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Execution error while checking student access", e);
        }
    }

    /**
     * Checks if a student has access to a specific course by communicating with an external course access service.
     *
     * @param courseId the unique identifier of the course
     * @param studentId the unique identifier of the student
     * @return a CompletableFuture containing a Boolean value indicating whether the student has access to the course
     */
    @Async
    protected CompletableFuture<Boolean> getStudentCourseAccess(String courseId, String studentId) {
        Span span = tracer.spanBuilder("getStudentCourseAccess").startSpan();

        log.info("[EXTERNAL] Checking student access with course-access-service rpc");

        try (Scope scope = span.makeCurrent()) {
            return CompletableFuture.supplyAsync(() -> {
                try (Scope innerScope = Context.current().makeCurrent()) {
                    return courseAccessServiceBlockingStub.hasAccess(
                                    HasAccessRequest.newBuilder()
                                            .setCourseId(courseId)
                                            .setUserId(studentId)
                                            .build())
                            .getHasAccess();
                }
            });
        } finally {
            span.end();
        }
    }
}