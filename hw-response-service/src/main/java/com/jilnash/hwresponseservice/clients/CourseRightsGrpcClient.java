package com.jilnash.hwresponseservice.clients;

import com.jilnash.courserightsservice.HasRightsRequest;
import com.jilnash.courserightsservice.TeacherRightsServiceGrpc;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;

@Component
public class CourseRightsGrpcClient {


    @GrpcClient("course-rights-grpc-client")
    private TeacherRightsServiceGrpc.TeacherRightsServiceBlockingStub courseRightsServiceGrpc;


    public void validateTeacherAllowedToCheckHomework(String courseId, String teacherId) {

        if (!courseRightsServiceGrpc.hasRights(HasRightsRequest.newBuilder()
                .setCourseId(courseId)
                .setTeacherId(teacherId)
                .addRights("RESPOND_HW")
                .build()).getHasRights()
        )
            throw new IllegalArgumentException("Teacher is not allowed to respond to homework");

    }
}
