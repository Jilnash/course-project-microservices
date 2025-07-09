package com.jilnash.courseservice.repo;

import com.jilnash.courseservice.dto.task.TaskLinkDTO;
import com.jilnash.courseservice.model.Task;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface TaskConnectionRepo extends Neo4jRepository<Task, String> {

//    @Query("MATCH (f:Task {id: $from})-[r:IS_PREREQUISITE]->(t:Task {id: $to}) " +
//            "RETURN COUNT(r) > 0")
//    Boolean getIfConnectionExists(String from, String to);

    @Query("UNWIND $taskConnections AS link " +
            "MATCH (f:Task {id: link.fromTaskId})-[r:IS_PREREQUISITE]->(t:Task {id: link.toTaskId}) " +
            "RETURN COUNT(r) > 0")
    Boolean getIfConnectionsExists(@Param("taskConnections") Set<TaskLinkDTO> taskConnections);

    @Query("UNWIND $taskConnections AS link " +
            "MATCH (f:Task {id: link.fromTaskId}), (t:Task {id: link.toTaskId}) " +
            "MERGE (f)-[:IS_PREREQUISITE]->(t)")
    void createTaskConnections(@Param("taskConnections") Set<TaskLinkDTO> taskConnections);

    @Query("UNWIND $taskConnections AS link " +
            "MATCH (f:Task {id: link.fromTaskId})-[r:IS_PREREQUISITE]->(t:Task {id: link.toTaskId}) " +
            "WITH f, r, t " +
            "CREATE (f)-[:IS_PREREQUISITE_DELETED]-> (t) " +
            "DELETE r")
    void softDeleteTaskConnections(@Param("taskConnections") Set<TaskLinkDTO> taskConnections);

    @Query("UNWIND $taskConnections AS link " +
            "MATCH (f:Task {id: link.fromTaskId})-[r:IS_PREREQUISITE]->(t:Task {id: link.toTaskId}) " +
            "WITH f, r, t " +
            "DELETE r")
    void hardDeleteTaskConnections(@Param("taskConnections") Set<TaskLinkDTO> taskConnections);
}
