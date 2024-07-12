package com.jilnash.hwresponseservice.service;

import com.jilnash.hwresponseservice.model.HwResponse;
import com.jilnash.hwresponseservice.repo.CommentRepo;
import com.jilnash.hwresponseservice.repo.HwResponseRepo;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class HwResponseServiceImpl implements HwResponseService {

    @Autowired
    private HwResponseRepo hwResponseRepo;

    @Autowired
    private CommentRepo commentRepo;

    @Override
    public List<HwResponse> getResponses(Long teacherId, Long homeworkId, Date createdAfter, Date createdBefore) {

        Specification<HwResponse> spec = (root, query, cb) -> {
            Predicate p = cb.conjunction();

            if (teacherId != null)
                p = cb.and(p, cb.equal(root.get("teacher").get("id"), teacherId));

            if (homeworkId != null)
                p = cb.and(p, cb.equal(root.get("homework").get("id"), homeworkId));

            if (createdBefore != null)
                p = cb.and(p, cb.lessThanOrEqualTo(root.get("createdAt"), createdBefore));

            if (createdAfter != null)
                p = cb.and(p, cb.greaterThanOrEqualTo(root.get("createdAt"), createdAfter));

            return p;
        };

        return hwResponseRepo.findAll(spec);
    }

    @Override
    public HwResponse getResponse(Long id) {
        return hwResponseRepo
                .findById(id)
                .orElseThrow(() -> new NoSuchElementException("Response with id " + id + " not found"));
    }

    @Override
    public HwResponse saveResponse(HwResponse response) {

        //if response id is not null, remove all the comments
        if (response.getId() != null)
            commentRepo.deleteAll(getResponse(response.getId()).getComments());

        //setting comments' response id, then saving them
        HwResponse savedResponse = hwResponseRepo.save(response);

        response.getComments().forEach(comment -> comment.setHwResponse(savedResponse));

        commentRepo.saveAll(response.getComments());

        return savedResponse;
    }
}
