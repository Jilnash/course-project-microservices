package com.jilnash.courseservice.repo;

import com.jilnash.courseservice.model.Module;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ModuleRepo extends Neo4jRepository<Module, String> {

    List<Module> findAllByNameContainingAndCourseId(String name, String courseId);

    List<Module> findAllByCourseId(String courseId);

    Optional<Module> findByIdAndCourseId(String id, String courseId);

    Boolean existsByIdAndCourseId(String id, String courseId);

    @Query("MATCH (m:Module {id: $id}) " +
            "SET m.name = $name, m.description = $description " +
            "RETURN m")
    Module updateModuleData(@Param("id") String id,
                            @Param("name") String name,
                            @Param("description") String description);
}
