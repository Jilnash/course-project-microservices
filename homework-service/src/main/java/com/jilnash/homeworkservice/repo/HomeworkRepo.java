package com.jilnash.homeworkservice.repo;

import com.jilnash.homeworkservice.model.Homework;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface HomeworkRepo extends JpaRepository<Homework, Long>, JpaSpecificationExecutor<Homework> {
}
