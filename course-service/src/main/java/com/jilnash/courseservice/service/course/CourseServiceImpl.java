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

/**
 * Implementation of the CourseService interface.
 * Provides methods to manage courses, including creating, updating, and retrieving course information.
 * <p>
 * This service interacts with the CourseRepo for database operations and uses CourseMapper
 * to convert between entities and DTOs. It also integrates with an external service for managing
 * teacher-course rights.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepo courseRepo;

    private final CourseMapper courseMapper;

    private final CourseAuthorizationService courseAuthrService;

    @GrpcClient("course-rights-client")
    private TeacherRightsServiceGrpc.TeacherRightsServiceBlockingStub teacherRightsServiceBlockingStub;

    /**
     * Retrieves a list of courses based on the specified name. If the name is not provided or empty,
     * all courses are retrieved. The method utilizes the repository to perform a search for courses
     * matching the provided name or fetches all courses if no name is given.
     *
     * @param name the name or partial name of the courses to filter. If empty, all courses will be returned.
     * @return a list of courses matching the specified name or all courses if no name is provided.
     */
    @Override
    public List<Course> getCourses(String name) {

        log.info("[SERVICE] Fetching courses with name: {}", name);

        if (!name.isEmpty())
            return courseRepo.findAllByNameContaining(name);

        return courseRepo.findAll();
    }

    /**
     * Retrieves a course by its unique identifier.
     *
     * @param id the unique identifier of the course to retrieve
     * @return the course associated with the given identifier
     * @throws NoSuchElementException if no course is found with the specified identifier
     */
    @Override
    public Course getCourse(String id) {

        log.info("[SERVICE] Fetching course with id: {}", id);

        return courseRepo
                .findById(id)
                .orElseThrow(() -> new NoSuchElementException("Course not found with id: " + id));
    }

    /**
     * Creates a new course with the provided information.
     *
     * The method generates a unique identifier for the course, assigns the
     * ownership of the course to the specified teacher by communicating with
     * an external service, and saves the course instance in the repository.
     * If the ownership assignment fails, a runtime exception is thrown.
     *
     * @param courseDTO the Data Transfer Object (DTO) containing the details
     *                  of the course to be created, such as name, author ID,
     *                  description, duration, and homework posting interval.
     * @return the created Course entity after being saved in the repository.
     * @throws RuntimeException if the course ownership cannot be assigned.
     */
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

        return courseRepo.save(courseMapper.toNode(courseDTO));
    }

    /**
     * Updates an existing course with the details provided in the CourseUpdateDTO.
     *
     * The method validates the teacher's rights to update the course and checks if
     * the course exists in the repository. If the course is found, it saves the updated
     * course details and returns the updated entity.
     *
     * @param courseDTO the Data Transfer Object (DTO) containing the updated information
     *                  for the course, including id, teacherId, name, description,
     *                  duration, and homework posting interval.
     * @return the updated Course entity after being saved in the repository.
     * @throws NoSuchElementException if the course with the specified id does not exist.
     */
    @Override
    public Course update(CourseUpdateDTO courseDTO) {

        log.info("[SERVICE] Updating course with id: {}", courseDTO.getId());

        courseAuthrService.validateTeacherCourseRights(courseDTO.getId(), courseDTO.getTeacherId(), List.of("UPDATE"));

        //check if course exists
        if (!courseRepo.existsById(courseDTO.getId()))
            throw new NoSuchElementException("Course not found with id: " + courseDTO.getId());

        //save course then returning
        return courseRepo.save(courseMapper.toNode(courseDTO));
    }

    @Override
    public Course delete(String id) {
        return null;
    }
}
