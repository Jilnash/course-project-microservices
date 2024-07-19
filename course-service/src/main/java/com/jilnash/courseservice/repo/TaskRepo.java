package com.jilnash.courseservice.repo;

import com.jilnash.courseservice.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepo extends JpaRepository<Task, Long> {

    Optional<Task> findByIdAndModuleIdAndModuleCourseId(Long id, Long moduleId, Long courseId);

    List<Task> findAllByTitleAndModuleIdAndModuleCourseId(String title, Long moduleId, Long courseId);

    List<Task> findAllByModule_IdAndModule_Course_id(Long moduleId, Long courseId);
}
