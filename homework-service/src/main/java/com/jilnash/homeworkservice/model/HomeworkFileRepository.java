package com.jilnash.homeworkservice.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface HomeworkFileRepository extends JpaRepository<HomeworkFile, UUID> {
}