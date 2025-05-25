package com.jilnash.homeworkservice.client;

import com.jilnash.courseaccessservice.CourseAccessServiceGrpc;
import com.jilnash.courseaccessservice.HasAccessRequest;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;

/**
 * This class is responsible for sending gRPC requests to course-access service.
 * It uses the CourseAccessServiceGrpc.CourseAccessServiceBlockingStub
 * to make synchronous gRPC calls to the Course Access service.
 */
@Component
@RequiredArgsConstructor
public class CourseAccessGrpcClient {

    private final CourseClient courseClient;

    @GrpcClient("course-access-client")
    private CourseAccessServiceGrpc.CourseAccessServiceBlockingStub courseAccessServiceBlockingStub;

    /**
     * Validates whether a student has access to a specific course.
     * This is determined by checking the access permissions for the course
     * associated with the provided task.
     *
     * @param studentId the unique identifier of the student whose access is being validated
     * @param taskId    the unique identifier of the task used to determine the associated course
     * @throws IllegalArgumentException if the student does not have access to the course
     */
    public void validateStudentHasAccessToCourse(String studentId, String taskId) {
        //getting course_id by task_id, then verifying if student has access to course
        if (!courseAccessServiceBlockingStub.hasAccess(HasAccessRequest.newBuilder()
                .setCourseId(courseClient.getTaskCourseId(taskId))
                .setUserId(studentId)
                .build()).getHasAccess()
        )
            throw new IllegalArgumentException("Student does not have access to course");
    }
}
