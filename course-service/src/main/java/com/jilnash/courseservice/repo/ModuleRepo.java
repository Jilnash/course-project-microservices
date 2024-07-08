package com.jilnash.courseservice.repo;

import com.jilnash.courseservice.model.Module;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ModuleRepo extends JpaRepository<Module, Long> {

    Optional<Module> findByIdAndCourseId(Long id, Long courseId);

    Optional<Module> findByNameAndCourseId(String name, Long courseId);
}
