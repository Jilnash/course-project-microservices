package com.jilnash.adminservice.repo;

import com.jilnash.adminservice.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepo extends JpaRepository<Admin, Long> {
}
