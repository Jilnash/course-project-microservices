package com.jilnash.studentservice.repo;

import com.jilnash.studentservice.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepo extends JpaRepository<Student, Long> {
}
