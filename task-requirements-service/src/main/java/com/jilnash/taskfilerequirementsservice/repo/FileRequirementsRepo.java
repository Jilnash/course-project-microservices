package com.jilnash.taskfilerequirementsservice.repo;

import com.jilnash.taskfilerequirementsservice.model.FileRequirement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FileRequirementsRepo extends JpaRepository<FileRequirement, UUID> {

    List<FileRequirement> findAllByContentTypeIn(List<String> names);
}
