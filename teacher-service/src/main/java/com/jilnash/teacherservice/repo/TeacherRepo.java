package com.jilnash.teacherservice.repo;

import com.jilnash.teacherservice.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeacherRepo extends JpaRepository<Teacher, Long> {

    @Query("SELECT t FROM Teacher t WHERE t.userId in :userIds")
    List<Teacher> findByUserIds(List<Long> userIds);
}
