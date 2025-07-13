package com.jilnash.progressservice.repo;

import com.jilnash.progressservice.model.StudentTaskComplete;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;
import java.util.Set;

@Repository
public interface StudentTaskCompleteRepository extends JpaRepository<StudentTaskComplete, Long> {

    @Query("SELECT COUNT(stc) FROM student_task_complete stc " +
            "WHERE stc.studentId = :studentId " +
            "AND stc.taskId IN :taskIds " +
            "AND stc.deletedAt IS NULL")
    Long countByStudentIdAndTaskIdsAndDeletedAtIsNull(String studentId, List<String> taskIds);

    @Query("SELECT stc.taskId FROM student_task_complete stc WHERE stc.studentId = :studentId")
    List<String> findTaskIdsByStudentId(String studentId);

    @Query("SELECT stc.studentId FROM student_task_complete stc WHERE stc.taskId IN :taskIds")
    Set<String> findStudentIdsByTaskIds(List<String> taskIds);

    void deleteAllByTaskIdIn(List<String> taskIds);

    @Modifying
    @Query("UPDATE student_task_complete stc SET stc.deletedAt = :date WHERE stc.taskId IN :taskIds")
    void updateDeletedAtByTaskIdIn(Date date, List<String> taskIds);
}