package com.jilnash.courseservice.repo;

import com.jilnash.courseservice.model.Task;
import com.jilnash.courseservicedto.dto.task.TaskCreateDTO;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface TaskRepo extends Neo4jRepository<Task, String> {

    List<Task> findAllByIdIn(Set<String> ids);

    List<Task> findAllByTitleStartingWithAndModule_IdAndModule_Course_IdAndDeletedAtIsNull(String title, String moduleId, String courseId);

    List<Task> findAllByModule_IdAndModule_Course_Id(String moduleId, String courseId);

    Optional<Task> findByIdAndModule_IdAndModule_Course_Id(String id, String moduleId, String courseId);

    @Query("MATCH (c:Course) -[:CONTAINS]->(m:Module)-[:CONTAINS]->(t:Task)" +
            "WHERE t.id = $taskId " +
            "RETURN c.id as courseId")
    Optional<String> getTaskCourseId(String taskId);

    @Query("MATCH (c:Course) -[:CONTAINS]->(m:Module)-[:CONTAINS]->(t:Task)" +
            "WHERE t.id = $taskId " +
            "RETURN m.id as moduleId")
    Optional<String> getTaskModuleId(String taskId);

    @Query("MATCH (m:Module {id: $taskDTO.moduleId}) " +
            "CREATE (t:Task {" +
            "id: $taskDTO.taskId, title: $taskDTO.title, description: $taskDTO.description, " +
            "authorId: $taskDTO.authorId, hwPostingInterval: $taskDTO.hwPostingInterval, " +
            "videoFileName: $taskDTO.videoFileName, isPublic: $taskDTO.isPublic}) " +
            "CREATE (m)-[:CONTAINS]->(t) " +
            "RETURN t")
    Optional<Task> createTaskWithoutRelationships(TaskCreateDTO taskDTO);

    @Query("MATCH (t:Task {id: $taskId}) -[r:IS_PREREQUISITE]-> () DELETE r")
    void disconnectTaskFromSuccessors(String taskId);

    @Query("MATCH () -[r:IS_PREREQUISITE]-> (t:Task {id: $taskId}) DELETE r")
    void disconnectTaskFromPrerequisites(String taskId);

    @Query("UNWIND $prerequisiteTaskIds AS prereqId " +
            "MATCH (from:Task {id: prereqId}), (to:Task {id: $taskId}) " +
            "CREATE (from)-[:IS_PREREQUISITE]->(to)")
    void connectTaskToPrerequisites(String taskId, Set<String> prerequisiteTaskIds);

    @Query("UNWIND $successorTaskIds AS sucId " +
            "MATCH (from:Task {id: $taskId}), (to:Task {id: sucId}) " +
            "CREATE (from)-[:IS_PREREQUISITE]->(to)")
    void connectTaskToSuccessors(String taskId, Set<String> successorTaskIds);

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
            "SET t.videoFileName = $videoFileName")
    void updateTaskVideoLink(String courseId, String moduleId, String taskId, String videoFileName);

    @Query("MATCH (c:Course) -[:CONTAINS]-> (m:Module) -[:CONTAINS]-> (t:Task) " +
            "WHERE c.id = $courseId and m.id = $moduleId and t.id = $taskId " +
            "SET t.isPublic = $isPublic")
    void updateTaskIsPublic(String courseId, String moduleId, String taskId, Boolean isPublic);

    @Query("MATCH (c:Course) -[:CONTAINS]-> (m:Module) -[:CONTAINS]-> (t:Task) " +
            "WHERE c.id = $courseId and m.id = $moduleId and t.id = $id " +
            "SET t.hwPostingInterval = $hwPostingInterval")
    void updateTaskHwPostingInterval(String courseId, String moduleId, String id, Integer hwPostingInterval);

    @Query("MATCH (c:Course) -[:CONTAINS]-> (m:Module) -[:CONTAINS]-> (t:Task) " +
            "WHERE c.id = $courseId and m.id = $moduleId and t.id = $taskId " +
            "DETACH DELETE t")
    void detachDeleteTask(String courseId, String moduleId, String taskId);
}
