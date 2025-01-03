package com.jilnash.courseservice.service.course;

import com.jilnash.courseservice.clients.CourseAccessClient;
import com.jilnash.courseservice.clients.CourseRightsClient;
import com.jilnash.courseservice.dto.course.CourseCreateDTO;
import com.jilnash.courseservice.dto.course.CourseUpdateDTO;
import com.jilnash.courseservice.mapper.CourseMapper;
import com.jilnash.courseservice.model.Course;
import com.jilnash.courseservice.repo.CourseRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepo courseRepo;

    private final CourseRightsClient courseRightsClient;

    private final CourseAccessClient courseAccessClient;

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
        return courseRepo.save(CourseMapper.toNode(courseDTO));
    }

    @Override
    public Course update(CourseUpdateDTO courseDTO) {

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

    public void validateTeacherCourseRights(String courseId, String teacherId, List<String> rights) {

        if (!courseRightsClient.hasRights(courseId, teacherId, rights))
            throw new UsernameNotFoundException("Teacher does not have rights: " + rights);
    }

    public void validateStudentCourseAccess(String courseId, String studentId) {

        if (!courseAccessClient.checkAccess(courseId, studentId))
            throw new UsernameNotFoundException("Student does not have access to course");
    }
}
