package com.jilnash.courseservice.repo;

import com.jilnash.courseservice.dto.task.TaskCreateDTO;
import com.jilnash.courseservice.dto.task.TaskLinkDTO;
import com.jilnash.courseservice.model.Task;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface TaskRepo extends Neo4jRepository<Task, String> {

    List<Task> findAllByIdIn(List<String> ids);

    @Query("MATCH (c:Course) -[:CONTAINS]->(m:Module)-[:CONTAINS]->(t:Task)" +
            "WHERE t.title STARTS WITH $title AND m.id = $moduleId AND c.id = $courseId " +
            "RETURN t { .id, .title, .description, .videoLink, .audioRequired, .videoRequired }")
    List<Task> findAllByTitleStartingWithAndModule_IdAndModule_Course_Id(String title, String moduleId, String courseId);

    List<Task> findAllByModule_IdAndModule_Course_Id(String moduleId, String courseId);

    @Query("MATCH (c:Course) -[:CONTAINS]->(m:Module)-[:CONTAINS]->(t:Task)"
            + "WHERE t.id = $id AND m.id = $moduleId AND c.id = $courseId "
            + "RETURN t { .id, .title, .description, .videoLink, .audioRequired, .videoRequired }")
    Optional<Task> getTaskData(String id, String moduleId, String courseId);

    Optional<Task> findByIdAndModule_IdAndModule_Course_Id(String id, String moduleId, String courseId);

    Boolean existsByIdAndModuleIdAndModule_CourseId(String id, String moduleId, String courseId);

    @Query("MATCH (t:Task {id: $id}) SET t.title = $title, t.description = $description, t.videoLink = $videoLink, t.audioRequired = $audioRequired, t.videoRequired = $videoRequired RETURN t")
    Task updateTaskData(String id, String title, String description, String videoLink, Boolean audioRequired, Boolean videoRequired);

    @Query("MATCH (c:Course) -[:CONTAINS]->(m:Module)-[:CONTAINS]->(t:Task)" +
            "WHERE t.id = $taskId " +
            "RETURN c.id as courseId")
    Optional<String> getTaskCourseId(String taskId);

    @Query("MATCH (m:Module {id: $taskDTO.moduleId}) " +
            "CREATE (t:Task {id: $taskDTO.taskId, title: $taskDTO.title, description: $taskDTO.description, videoLink: $taskDTO.videoLink, audioRequired: $taskDTO.audioRequired, videoRequired: $taskDTO.videoRequired}) " +
            "CREATE (m)-[:CONTAINS]->(t) " +
            "WITH t " +
            "UNWIND $taskDTO.successorTasksIds AS suc " +
            "MATCH (to:Task {id: suc}) " +
            "CREATE (t)-[:IS_PREREQUISITE]->(to) " +
            "WITH t " +
            "UNWIND $taskDTO.prerequisiteTasksIds AS prereq " +
            "MATCH (from:Task {id: prereq}) " +
            "CREATE (from)-[:IS_PREREQUISITE]->(t)")
    void createTaskWithRelationships(TaskCreateDTO taskDTO);

    @Query("UNWIND $taskIdLinks AS link " +
            "MATCH (from:Task {id: link.fromTaskId})-[r:IS_PREREQUISITE]->(to:Task {id: link.toTaskId}) " +
            "DELETE r")
    void deleteTaskRelationshipsByTaskIdLinks(Set<TaskLinkDTO> taskIdLinks);

    @Query("MATCH (t:Task {id: $taskId}) RETURN [t.audioRequired, t.videoRequired, t.imageRequired]")
    Optional<List<Object>> getTaskRequirements(String taskId);
}
