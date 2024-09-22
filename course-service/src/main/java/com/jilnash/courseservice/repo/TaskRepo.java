package com.jilnash.courseservice.repo;

import com.jilnash.courseservice.model.Task;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepo extends Neo4jRepository<Task, String> {

    List<Task> findAllByIdIn(List<String> ids);

    @Query("MATCH (c:Course) -[:CONTAINS]->(m:Module)-[:CONTAINS]->(t:Task)" +
            "WHERE t.title STARTS WITH $title AND m.id = $moduleId AND c.id = $courseId " +
            "RETURN t { .id, .title, .description, .videoLink, .audioRequired, .videoRequired }")
    List<Task> findAllByTitleStartingWithAndModule_IdAndModule_Course_Id(String title, String moduleId, String courseId);

    @Query("MATCH (c:Course) -[:CONTAINS]->(m:Module)-[:CONTAINS]->(t:Task)"
            + "WHERE t.id = $id AND m.id = $moduleId AND c.id = $courseId "
            + "RETURN t { .id, .title, .description, .videoLink, .audioRequired, .videoRequired }")
    Optional<Task> getTaskData(String id, String moduleId, String courseId);

    Optional<Task> findByIdAndModule_IdAndModule_Course_Id(String id, String moduleId, String courseId);

    Boolean existsByIdAndModuleIdAndModule_CourseId(String id, String moduleId, String courseId);

    @Query("MATCH (t:Task {id: $id}) SET t.title = $title, t.description = $description, t.videoLink = $videoLink, t.audioRequired = $audioRequired, t.videoRequired = $videoRequired RETURN t")
    Task updateTaskData(String id, String title, String description, String videoLink, Boolean audioRequired, Boolean videoRequired);
}
