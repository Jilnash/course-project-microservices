package com.jilnash.courseservice.service.courseauthr;

import com.jilnash.courseaccessservice.CourseAccessServiceGrpc;
import com.jilnash.courseaccessservice.HasAccessRequest;
import com.jilnash.courserightsservice.HasRightsRequest;
import com.jilnash.courserightsservice.TeacherRightsServiceGrpc;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class CourseAuthorizationService {

    @GrpcClient("course-access-client")
    private CourseAccessServiceGrpc.CourseAccessServiceBlockingStub courseAccessServiceBlockingStub;

    @GrpcClient("course-rights-client")
    private TeacherRightsServiceGrpc.TeacherRightsServiceBlockingStub teacherRightsServiceBlockingStub;

    public void validateUserAccess(String courseId, String userId) {

        if (!getStudentCourseAccess(courseId, userId) && !getTeacherHasCourseRights(courseId, userId, List.of("READ")))
            throw new RuntimeException("Access validation failed");
    }

    public void validateTeacherCourseRights(String courseId, String teacherId, List<String> rights) {

        if (!getTeacherHasCourseRights(courseId, teacherId, rights))
            throw new UsernameNotFoundException("Teacher does not have rights: " + rights);
    }

    private boolean getTeacherHasCourseRights(String courseId, String teacherId, List<String> rights) {

        log.info("[EXTERNAL] Checking teacher rights with course-rights-service rpc");

        return teacherRightsServiceBlockingStub.hasRights(
                        HasRightsRequest.newBuilder()
                                .setCourseId(courseId)
                                .setTeacherId(teacherId)
                                .addAllRights(rights)
                                .build())
                .getHasRights();
    }

    public void validateStudentCourseAccess(String courseId, String studentId) {

        if (!getStudentCourseAccess(courseId, studentId))
            throw new UsernameNotFoundException("Student does not have access to course");
    }

    private boolean getStudentCourseAccess(String courseId, String studentId) {

        log.info("[EXTERNAL] Checking student access with course-access-service rpc");

        return courseAccessServiceBlockingStub.hasAccess(
                        HasAccessRequest.newBuilder()
                                .setCourseId(courseId)
                                .setUserId(studentId)
                                .build()).
                getHasAccess();
    }
}
