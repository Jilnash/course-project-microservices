package com.jilnash.courseservice.repo;

import com.jilnash.courseservice.model.Course;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepo extends Neo4jRepository<Course, String> {

    List<Course> findAllByNameContaining(String name);
}
