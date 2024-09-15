package com.jilnash.courseservice.service.course;

import com.jilnash.courseservice.dto.course.CourseCreateDTO;
import com.jilnash.courseservice.dto.course.CourseUpdateDTO;
import com.jilnash.courseservice.mapper.CourseMapper;
import com.jilnash.courseservice.model.Course;
import com.jilnash.courseservice.repo.CourseRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class CourseServiceImpl implements CourseService {

    private final CourseRepo courseRepo;

    private final CourseMapper courseMapper;

    public CourseServiceImpl(CourseRepo courseRepo, CourseMapper courseMapper) {
        this.courseRepo = courseRepo;
        this.courseMapper = courseMapper;
    }

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
        return courseRepo.save(courseMapper.toNode(courseDTO));
    }

    @Override
    public Course update(CourseUpdateDTO courseDTO) {

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
