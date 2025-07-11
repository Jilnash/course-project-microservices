package com.jilnash.homeworkservice.repo;

import com.jilnash.homeworkservice.model.HomeworkFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface HomeworkFileRepository extends JpaRepository<HomeworkFile, UUID> {
}