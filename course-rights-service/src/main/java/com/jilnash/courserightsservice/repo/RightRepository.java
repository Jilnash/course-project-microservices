package com.jilnash.courserightsservice.repo;

import com.jilnash.courserightsservice.model.Right;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface RightRepository extends JpaRepository<Right, Long> {

    Optional<Set<Right>> findAllByNameIn(Set<String> names);
}