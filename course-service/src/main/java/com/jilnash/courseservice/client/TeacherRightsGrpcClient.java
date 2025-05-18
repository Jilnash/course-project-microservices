package com.jilnash.courseservice.client;

import com.jilnash.courserightsservice.HasRightsRequest;
import com.jilnash.courserightsservice.SetCourseOwnerRequest;
import com.jilnash.courserightsservice.TeacherRightsServiceGrpc;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * A gRPC client component for interacting with the Teacher Rights microservice.
 * This client facilitates operations related to managing teacher rights within courses.
 */
@Component
@RequiredArgsConstructor
public class TeacherRightsGrpcClient {


    @GrpcClient("course-rights-client")
    private TeacherRightsServiceGrpc.TeacherRightsServiceBlockingStub teacherRightsServiceBlockingStub;

    /**
     * Assigns a teacher as the course owner for a specified course by invoking the Teacher Rights gRPC service.
     *
     * @param authorId the unique identifier of the teacher to be assigned as the course owner
     * @param courseId the unique identifier of the course for which the teacher will be set as the owner
     * @return a Boolean indicating whether the operation was successful
     */
    public Boolean createCourseOwner(String authorId, String courseId) {
        return teacherRightsServiceBlockingStub
                .setCourseOwner(SetCourseOwnerRequest.newBuilder()
                        .setTeacherId(authorId)
                        .setCourseId(courseId)
                        .build())
                .getSuccess();
    }

    /**
     * Checks if a teacher has specific rights within a course by invoking the Teacher Rights gRPC service.
     *
     * @param courseId  the unique identifier of the course to check rights for
     * @param teacherId the unique identifier of the teacher whose rights are to be checked
     * @param rights    a list of rights to be verified for the teacher within the course
     * @return a Boolean indicating whether the teacher possesses the specified rights in the course
     */
    public Boolean hasRightsInCourse(String courseId, String teacherId, List<String> rights) {
        return teacherRightsServiceBlockingStub.hasRights(
                        HasRightsRequest.newBuilder()
                                .setCourseId(courseId)
                                .setTeacherId(teacherId)
                                .addAllRights(rights)
                                .build())
                .getHasRights();
    }
}
