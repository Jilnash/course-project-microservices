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

    List<Task> findAllByIdIn(Set<String> ids);

    List<Task> findAllByTitleStartingWithAndModule_IdAndModule_Course_Id(String title, String moduleId, String courseId);

    List<Task> findAllByModule_IdAndModule_Course_Id(String moduleId, String courseId);

    Optional<Task> findByIdAndModule_IdAndModule_Course_Id(String id, String moduleId, String courseId);

    Boolean existsByIdAndModuleIdAndModule_CourseId(String id, String moduleId, String courseId);

    @Query("MATCH (t:Task {id: $id}) SET t.title = $title, t.description = $description, t.videoLink = $videoLink RETURN t")
    Task updateTaskData(String id, String title, String description, String videoLink);

    @Query("MATCH (c:Course) -[:CONTAINS]->(m:Module)-[:CONTAINS]->(t:Task)" +
            "WHERE t.id = $taskId " +
            "RETURN c.id as courseId")
    Optional<String> getTaskCourseId(String taskId);

    @Query("MATCH (m:Module {id: $taskDTO.moduleId}) " +
            "CREATE (t:Task {id: $taskDTO.taskId, title: $taskDTO.title, description: $taskDTO.description, videoLink: $taskDTO.videoLink}) " +
            "CREATE (m)-[:CONTAINS]->(t) ")
    void createTaskWithoutRelationships(TaskCreateDTO taskDTO);

    @Query("UNWIND $taskDTO.prerequisiteTasksIds AS prereq " +
            "MATCH (from:Task {id: prereq}), (to:Task {id: $taskDTO.taskId}) " +
            "CREATE (from)-[:IS_PREREQUISITE]->(to)")
    void connectTaskToPrerequisites(TaskCreateDTO taskDTO);

    @Query("UNWIND $taskDTO.successorTasksIds AS suc " +
            "MATCH (from:Task {id: $taskDTO.taskId}), (to:Task {id: suc}) " +
            "CREATE (from)-[:IS_PREREQUISITE]->(to)")
    void connectTaskToSuccessors(TaskCreateDTO taskDTO);

    @Query("UNWIND $taskIdLinks AS link " +
            "MATCH (from:Task {id: link.fromTaskId})-[r:IS_PREREQUISITE]->(to:Task {id: link.toTaskId}) " +
            "DELETE r")
    void deleteTaskRelationshipsByTaskIdLinks(Set<TaskLinkDTO> taskIdLinks);

    @Query("MATCH (c:Course) -[:CONTAINS]-> (m:Module) -[:CONTAINS]-> (t:Task) " +
            "WHERE c.id = $courseId and m.id = $moduleId and t.id = $taskId " +
            "SET t.title = $title")
    void updateTaskTitle(String courseId, String moduleId, String taskId, String title);

    @Query("MATCH (c:Course) -[:CONTAINS]-> (m:Module) -[:CONTAINS]-> (t:Task) " +
            "WHERE c.id = $courseId and m.id = $moduleId and t.id = $taskId " +
            "SET t.description = $description")
    void updateTaskDescription(String courseId, String moduleId, String taskId, String description);

    @Query("MATCH (c:Course) -[:CONTAINS]-> (m:Module) -[:CONTAINS]-> (t:Task) " +
            "WHERE c.id = $courseId and m.id = $moduleId and t.id = $taskId " +
            "SET t.videoLink = $videoLink")
    void updateTaskVideoLink(String courseId, String moduleId, String taskId, String videoLink);

    @Query("MATCH (c:Course) -[:CONTAINS]-> (m:Module) -[:CONTAINS]-> (t:Task) " +
            "WHERE c.id = $courseId and m.id = $moduleId and t.id = $taskId " +
            "SET t.isPublic = $isPublic")
    void updateTaskIsPublic(String courseId, String moduleId, String taskId, Boolean isPublic);
}
