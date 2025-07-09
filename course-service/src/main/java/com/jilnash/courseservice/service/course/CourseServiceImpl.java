package com.jilnash.courseservice.service.course;

import com.jilnash.courseservice.dto.course.CourseCreateDTO;
import com.jilnash.courseservice.mapper.CourseMapper;
import com.jilnash.courseservice.model.Course;
import com.jilnash.courseservice.repo.CourseRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
            return courseRepo.findAllByNameContainingAndDeletedAtIsNull(name);

        return courseRepo.findAllByDeletedAtIsNull();
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

    @Override
    public String getCourseAuthor(String courseId) {
        return "";
    }

    @Override
    public String getCourseName(String courseId) {
        return getCourse(courseId).getName();
    }

    @Override
    public String getCourseDescription(String courseId) {
        return getCourse(courseId).getDescription();
    }

    @Override
    public String getCourseDuration(String courseId) {
        return getCourse(courseId).getDuration();
    }

    /**
     * Creates a new course with the provided information.
     * <p>
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
    public Course createCourse(CourseCreateDTO courseDTO) {

        log.info("[SERVICE] Creating course with name: {}", courseDTO.getName());

        courseDTO.setId(UUID.randomUUID().toString());

        return courseRepo.save(courseMapper.toNode(courseDTO));
    }

    @Override
    public Boolean updateCourseName(String courseId, String name) {

        log.info("[SERVICE] Updating course name with id: {}", courseId);

        courseRepo.updateCourseName(courseId, name);

        //save course then returning
        return true;
    }

    @Override
    public Boolean updateCourseDescription(String courseId, String description) {

        log.info("[SERVICE] Updating course name with id: {}", courseId);

        courseRepo.updateCourseDescription(courseId, description);

        //save course then returning
        return true;
    }

    @Override
    public Boolean updateCourseDuration(String courseId, String duration) {

        log.info("[SERVICE] Updating course name with id: {}", courseId);

        courseRepo.updateCourseDuration(courseId, duration);

        //save course then returning
        return true;
    }

    @Override
    public Boolean softDelete(String id) {
        return courseRepo
                .findById(id)
                .map(course -> {
                    log.info("[SERVICE] Soft deleting course with id: {}", id);
                    course.setDeletedAt(new java.util.Date());
                    courseRepo.save(course);
                    return true;
                })
                .orElseThrow(() -> new NoSuchElementException("Course not found with id: " + id));
    }

    @Override

    public Boolean hardDelete(String id) {

        log.info("[SERVICE] Hard deleting course with id: {}", id);

//        courseRepo.deleteById(id);

        return true;
    }
}
