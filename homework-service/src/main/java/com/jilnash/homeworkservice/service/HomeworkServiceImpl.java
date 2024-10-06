package com.jilnash.homeworkservice.service;

import com.jilnash.homeworkservice.model.Homework;
import com.jilnash.homeworkservice.repo.HomeworkRepo;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class HomeworkServiceImpl implements HomeworkService {

    @Autowired
    private HomeworkRepo homeworkRepo;

    @Override
    public List<Homework> getHomeworks(Long taskId, Long studentId, Boolean checked, Date createdAfter) {

        Specification<Homework> spec = (root, query, cb) -> {
            Predicate p = cb.conjunction();

            if (studentId != null)
                p = cb.and(p, cb.equal(root.get("student").get("id"), studentId));

            if (taskId != null)
                p = cb.and(p, cb.equal(root.get("task").get("id"), taskId));

            if (checked != null)
                p = cb.and(p, cb.equal(root.get("checked"), checked));

            if (createdAfter != null)
                p = cb.and(p, cb.greaterThanOrEqualTo(root.get("createdAt"), createdAfter));

            return p;
        };

        return homeworkRepo.findAll(spec);
    }

    @Override
    public Homework getHomework(Long id) {
        return homeworkRepo
                .findById(id)
                .orElseThrow(() -> new NoSuchElementException("Homework not found with id: " + id));
    }

    @Override
    public Homework saveHomework(Homework homework) {
        return homeworkRepo.save(homework);
    }

    public String getHwTaskId(Long id) {
        return homeworkRepo
                .getHwTaskId(id)
                .orElseThrow(() -> new NoSuchElementException("Homework not found with id: " + id));
    }
}
