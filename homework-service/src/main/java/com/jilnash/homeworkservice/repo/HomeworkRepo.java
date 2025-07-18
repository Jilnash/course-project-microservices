package com.jilnash.homeworkservice.repo;

import com.jilnash.homeworkservice.model.Homework;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface HomeworkRepo extends JpaRepository<Homework, UUID>, JpaSpecificationExecutor<Homework> {

    @Query("SELECT h.taskId FROM Homework h WHERE h.id = :id")
    Optional<String> getHwTaskId(@Param("id") UUID id);

    @Query("SELECT h.studentId FROM Homework h WHERE h.id = :id")
    Optional<String> getHwStudentId(@Param("id") UUID id);

    @Query("SELECT COUNT(h) > 0 FROM Homework h " +
            "WHERE h.studentId = :studentId AND h.taskId = :taskId AND h.checked = false")
    Boolean getHwUnchecked(@Param("studentId") String studentId, @Param("taskId") String taskId);

    Integer countByStudentIdAndTaskId(String studentId, String taskId);

    @Modifying
    @Query("UPDATE Homework h SET h.checked = :b WHERE h.id = :id")
    void updateIsChecked(UUID id, boolean b);

    @Modifying
    @Query("DELETE FROM Homework h WHERE h.studentId = :studentId AND h.taskId = :taskId AND h.attempt = :attempt")
    void deleteHomework(String studentId, String taskId, Integer attempt);
}
