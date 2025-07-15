package com.jilnash.taskfilerequirementsservice.repo;

import com.jilnash.taskfilerequirementsservice.model.TaskFileRequirement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Repository
public interface TaskFileRequirementRepo extends JpaRepository<TaskFileRequirement, UUID> {

    List<TaskFileRequirement> findAllByTaskId(String taskId);

    void deleteAllByTaskId(String taskId);

    void deleteAllByTaskIdAndDeletedAtIsNotNull(String taskId);

    @Modifying
    @Query("UPDATE TaskFileRequirement t SET t.deletedAt = ?2 WHERE t.taskId = ?1")
    void updateDeleteAtByTaskId(String taskId, Date date);
}