package com.jilnash.courseservice.service.course;

import com.jilnash.courseaccessservice.CourseAccessServiceGrpc;
import com.jilnash.courseaccessservice.HasAccessRequest;
import com.jilnash.courserightsservice.HasRightsRequest;
import com.jilnash.courserightsservice.SetCourseOwnerRequest;
import com.jilnash.courserightsservice.TeacherRightsServiceGrpc;
import com.jilnash.courseservice.dto.course.CourseCreateDTO;
import com.jilnash.courseservice.dto.course.CourseUpdateDTO;
import com.jilnash.courseservice.mapper.CourseMapper;
import com.jilnash.courseservice.model.Course;
import com.jilnash.courseservice.repo.CourseRepo;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepo courseRepo;

    @GrpcClient("course-access-client")
    private CourseAccessServiceGrpc.CourseAccessServiceBlockingStub courseAccessServiceBlockingStub;

    @GrpcClient("course-rights-client")
    private TeacherRightsServiceGrpc.TeacherRightsServiceBlockingStub teacherRightsServiceBlockingStub;

    @Override
    public List<Course> getCourses(String name) {

        if (!name.isEmpty())
            return courseRepo.findAllByNameContaining(name);

        return courseRepo.findAll();
    }

    @Override
    public Course getCourse(String id) {
        return courseRepo
                .findById(id)
                .orElseThrow(() -> new NoSuchElementException("Course not found with id: " + id));
    }

    @Override
    public Course create(CourseCreateDTO courseDTO) {

        courseDTO.setId(UUID.randomUUID().toString());

        if (!teacherRightsServiceBlockingStub
                .setCourseOwner(SetCourseOwnerRequest.newBuilder()
                        .setTeacherId(courseDTO.getAuthorId())
                        .setCourseId(courseDTO.getId())
                        .build())
                .getSuccess()
        )
            throw new RuntimeException("Failed to set course owner");

        return courseRepo.save(CourseMapper.toNode(courseDTO));
    }

    @Override
    public Course update(CourseUpdateDTO courseDTO) {

        validateTeacherCourseRights(courseDTO.getId(), courseDTO.getTeacherId(), List.of("UPDATE"));

        //check if course exists
        if (!courseRepo.existsById(courseDTO.getId()))
            throw new NoSuchElementException("Course not found with id: " + courseDTO.getId());

        //save course then returning
        return courseRepo.save(CourseMapper.toNode(courseDTO));
    }

    @Override
    public Course delete(String id) {
        return null;
    }

    public void validateUserAccess(String courseId, String userId) {

        if (!getStudentCourseAccess(courseId, userId) && !getTeacherHasCourseRights(courseId, userId, List.of("READ")))
            throw new RuntimeException("Access validation failed");
    }

    public void validateTeacherCourseRights(String courseId, String teacherId, List<String> rights) {

        if (!getTeacherHasCourseRights(courseId, teacherId, rights))
            throw new UsernameNotFoundException("Teacher does not have rights: " + rights);
    }

    private boolean getTeacherHasCourseRights(String courseId, String teacherId, List<String> rights) {
        return teacherRightsServiceBlockingStub.hasRights(HasRightsRequest.newBuilder()
                .setCourseId(courseId)
                .setTeacherId(teacherId)
                .addAllRights(rights)
                .build()).getHasRights();
    }

    public void validateStudentCourseAccess(String courseId, String studentId) {

        if (!getStudentCourseAccess(courseId, studentId))
            throw new UsernameNotFoundException("Student does not have access to course");
    }

    private boolean getStudentCourseAccess(String courseId, String studentId) {
        return courseAccessServiceBlockingStub.hasAccess(HasAccessRequest.newBuilder()
                .setCourseId(courseId)
                .setUserId(studentId)
                .build()).getHasAccess();
    }
}
