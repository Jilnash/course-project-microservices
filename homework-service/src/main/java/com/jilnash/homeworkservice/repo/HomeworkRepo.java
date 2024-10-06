package com.jilnash.homeworkservice.repo;

import com.jilnash.homeworkservice.model.Homework;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HomeworkRepo extends JpaRepository<Homework, Long>, JpaSpecificationExecutor<Homework> {

    @Query("SELECT h.taskId FROM Homework h WHERE h.id = :id")
    Optional<String> getHwTaskId(@Param("id") Long id);
}
