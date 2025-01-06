package com.jilnash.taskfilerequirementsservice.repo;

import com.jilnash.taskfilerequirementsservice.model.TaskFileRequirement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TaskFileRequirementRepo extends JpaRepository<TaskFileRequirement, UUID> {

    List<TaskFileRequirement> findAllByTaskId(String taskId);

    void deleteAllByTaskId(String taskId);
}