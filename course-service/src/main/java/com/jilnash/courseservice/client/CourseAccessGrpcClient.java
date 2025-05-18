package com.jilnash.courseservice.client;

import com.jilnash.courseaccessservice.CourseAccessServiceGrpc;
import com.jilnash.courseaccessservice.HasAccessRequest;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;

/**
 * Client for interacting with the Course Access gRPC service.
 * This client is responsible for checking access permissions for a user over a specific course.
 * </p>
 * The client communicates with the gRPC server configured with the name
 * "course-access-client". It utilizes a blocking stub to perform
 * synchronous calls to the server.
 */
@Component
public class CourseAccessGrpcClient {

    @GrpcClient("course-access-client")
    private CourseAccessServiceGrpc.CourseAccessServiceBlockingStub courseAccessServiceBlockingStub;

    /**
     * Checks if a student has access to a specified course.
     * <p>
     * This method contacts the Course Access gRPC service to verify
     * whether the provided user has the appropriate permissions to access the given course.
     *
     * @param courseId the unique identifier of the course to check access for
     * @param userId   the unique identifier of the user whose access is being verified
     * @return a Boolean indicating whether the user has access to the course;
     * true if access is granted, false otherwise
     */
    public Boolean hasAccess(String courseId, String userId) {
        return courseAccessServiceBlockingStub
                .hasAccess(HasAccessRequest.newBuilder()
                        .setCourseId(courseId)
                        .setUserId(userId)
                        .build())
                .getHasAccess();
    }
}
