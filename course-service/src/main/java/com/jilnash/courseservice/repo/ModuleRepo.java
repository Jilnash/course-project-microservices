package com.jilnash.courseservice.repo;

import com.jilnash.courseservice.model.Module;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface ModuleRepo extends Neo4jRepository<Module, String> {

    Optional<Module> findModuleById(String id);

    List<Module> findAllByCourseId(String courseId);

    Optional<Module> findByIdAndCourseId(String id, String courseId);

    Boolean existsByIdAndCourseId(String id, String courseId);

    @Query("MATCH (m:Module {id: $id}) SET m.name = $name")
    void updateModuleName(@Param("id") String id, @Param("name") String name);

    @Query("MATCH (m:Module {id: $id}) SET m.description = $description RETURN m")
    void updateModuleDescription(@Param("id") String id, @Param("description") String description);

    @Query("MATCH (m:Module)-[:CONTAINS]->(t:Task) WHERE m.id = $id RETURN count(t) > 0")
    Optional<Boolean> hasAtLeastOneTask(@Param("id") String id);

    @Query("MATCH (m:Module {id: $moduleId})-[:CONTAINS]->(t:Task) " +
            "WHERE t.id IN $taskIds " +
            "RETURN count(t) = size($taskIds)")
    Boolean containsTasks(String moduleId, Set<String> taskIds);
}
