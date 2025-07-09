package com.jilnash.courseservice.repo;

import com.jilnash.courseservice.model.Course;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepo extends Neo4jRepository<Course, String> {

    List<Course> findAllByDeletedAtIsNull();

    List<Course> findAllByNameContainingAndDeletedAtIsNull(String name);

    @Query("MATCH (c:Course) WHERE c.id = $courseId SET c.name = $name")
    void updateCourseName(String courseId, String name);

    @Query("MATCH (c:Course) WHERE c.id = $courseId SET c.description = $description")
    void updateCourseDescription(String courseId, String description);

    @Query("MATCH (c:Course) WHERE c.id = $courseId SET c.duration = $duration")
    void updateCourseDuration(String courseId, String duration);
}
