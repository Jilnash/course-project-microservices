package com.jilnash.homeworkservice.client;

import com.jilnash.courseaccessservice.CourseAccessServiceGrpc;
import com.jilnash.courseaccessservice.HasAccessRequest;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CourseAccessGrpcClient {

    private final CourseClient courseClient;

    @GrpcClient("course-access-client")
    private CourseAccessServiceGrpc.CourseAccessServiceBlockingStub courseAccessServiceBlockingStub;

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
