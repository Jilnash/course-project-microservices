package com.jilnash.progressservice.repo;

import com.jilnash.progressservice.model.StudentTaskComplete;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentTaskCompleteRepository extends JpaRepository<StudentTaskComplete, Long> {

    Boolean existsByStudentIdAndTaskId(String studentId, String taskId);

    @Query("SELECT stc.taskId FROM student_task_complete stc WHERE stc.studentId = :studentId")
    List<String> findTaskIdsByStudentId(String studentId);

    @Query("SELECT stc.studentId FROM student_task_complete stc WHERE stc.taskId = :taskId")
    List<String> findStudentIdsByTaskId(String taskId);

    @Query("SELECT stc.studentId FROM student_task_complete stc WHERE stc.taskId IN :taskIds")
    List<String> findStudentIdsByTaskIds(List<String> studentId);

    void deleteAllByTaskId(String taskId);

    void deleteAllByTaskIdIn(List<String> taskIds);
}