package com.jilnash.studentservice.repo;

import com.jilnash.studentservice.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepo extends JpaRepository<Student, Long> {

    @Query("SELECT s FROM Student s WHERE s.userId IN (:ids)")
    List<Student> findAllByUserIds(@Param("ids") List<Long> ids);
}
