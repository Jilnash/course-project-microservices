package com.jilnash.courseservice.repo;

import com.jilnash.courseservice.dto.task.TaskCreateDTO;
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

    @Query("MATCH (c:Course {id: $taskDTO.courseId}) - [r:CONTAINS] -> (m:Module {id: $taskDTO.moduleId}) " +
            "CREATE (t:Task {id: $taskDTO.taskId, title: $taskDTO.title, description: $taskDTO.description, videoLink: $taskDTO.videoLink, audioRequired: $taskDTO.audioRequired, videoRequired: $taskDTO.videoRequired}) " +
            "CREATE (m)-[:CONTAINS]->(t) " +
            "WITH t " +
            "UNWIND $taskDTO.prerequisiteTasksIds AS preReqId " +
            "MATCH (preReq:Task {id: preReqId}) " +
            "CREATE (preReq)-[:IS_PREREQUISITE]->(t) " +
            "WITH t " +
            "UNWIND $taskDTO.successorTasksIds AS postReqId " +
            "MATCH (postReq:Task {id: postReqId}) " +
            "CREATE (t)-[:IS_PREREQUISITE]->(postReq) " +
            "RETURN t")
    void createTaskWithRelationships(TaskCreateDTO taskDTO);

//    @Query("UNWIND $taskIdPairs AS pair " +
//            "MATCH (n1)-[r:IS_PREREQUISITE]-(n2) " +
//            "WHERE id(n1) = pair.first AND id(n2) = pair.second" +
//            "DELETE r")
//    void deleteTaskRelationshipsByTaskIdPairs(Set<Pair<String, String>> taskIdPairs);
}
