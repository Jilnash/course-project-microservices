package com.jilnash.courseservice.service.course;

import com.jilnash.courserightsservice.SetCourseOwnerRequest;
import com.jilnash.courserightsservice.TeacherRightsServiceGrpc;
import com.jilnash.courseservice.dto.course.CourseCreateDTO;
import com.jilnash.courseservice.dto.course.CourseUpdateDTO;
import com.jilnash.courseservice.mapper.CourseMapper;
import com.jilnash.courseservice.model.Course;
import com.jilnash.courseservice.repo.CourseRepo;
import com.jilnash.courseservice.service.courseauthr.CourseAuthorizationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepo courseRepo;

    private final CourseAuthorizationService courseAuthrService;

    @GrpcClient("course-rights-client")
    private TeacherRightsServiceGrpc.TeacherRightsServiceBlockingStub teacherRightsServiceBlockingStub;

    @Override
    public List<Course> getCourses(String name) {

        log.info("[SERVICE] Fetching courses with name: {}", name);

        if (!name.isEmpty())
            return courseRepo.findAllByNameContaining(name);

        return courseRepo.findAll();
    }

    @Override
    public Course getCourse(String id) {

        log.info("[SERVICE] Fetching course with id: {}", id);

        return courseRepo
                .findById(id)
                .orElseThrow(() -> new NoSuchElementException("Course not found with id: " + id));
    }

    @Override
    public Course create(CourseCreateDTO courseDTO) {

        log.info("[SERVICE] Creating course with name: {}", courseDTO.getName());

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

        log.info("[SERVICE] Updating course with id: {}", courseDTO.getId());

        courseAuthrService.validateTeacherCourseRights(courseDTO.getId(), courseDTO.getTeacherId(), List.of("UPDATE"));

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
}
