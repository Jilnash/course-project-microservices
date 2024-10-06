package com.jilnash.hwresponseservice.service;

import com.jilnash.hwresponseservice.clients.CourseClient;
import com.jilnash.hwresponseservice.clients.CourseRightsClient;
import com.jilnash.hwresponseservice.clients.HwClient;
import com.jilnash.hwresponseservice.model.HwResponse;
import com.jilnash.hwresponseservice.repo.CommentRepo;
import com.jilnash.hwresponseservice.repo.HwResponseRepo;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class HwResponseServiceImpl implements HwResponseService {

    private final HwResponseRepo hwResponseRepo;

    private final CommentRepo commentRepo;
    private final CourseRightsClient courseRightsClient;
    private final CourseClient courseClient;
    private final HwClient hwClient;

    public HwResponseServiceImpl(HwResponseRepo hwResponseRepo, CommentRepo commentRepo, CourseRightsClient courseRightsClient, CourseClient courseClient, HwClient hwClient) {
        this.hwResponseRepo = hwResponseRepo;
        this.commentRepo = commentRepo;
        this.courseRightsClient = courseRightsClient;
        this.courseClient = courseClient;
        this.hwClient = hwClient;
    }

    @Override
    public List<HwResponse> getResponses(String teacherId, Long homeworkId, Date createdAfter, Date createdBefore) {

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
    public HwResponse createResponse(HwResponse response) {

        //checking if teacher is allowed to check homework
        if (
                courseRightsClient.hasRights(
                        //getting taskId by hwId, then getting courseId by taskId
                        courseClient.getTaskCourseId(hwClient.getTaskId(response.getHomeworkId())),
                        response.getTeacherId(),
                        List.of("check")
                )
        ) {
            throw new IllegalArgumentException("Teacher is not allowed to check homework");
        }

        //creating response, then saving it for comments' response id
        HwResponse savedResponse = hwResponseRepo.save(response);

        //setting comments' response id
        response.getComments().forEach(comment -> comment.setHwResponse(savedResponse));

        //saving all comments
        commentRepo.saveAll(response.getComments());

        return savedResponse;
    }

    @Override
    public HwResponse updateResponse(HwResponse response) {

        //checking if teacher is allowed to check homework
        if (
                courseRightsClient.hasRights(
                        //getting taskId by hwId, then getting courseId by taskId
                        courseClient.getTaskCourseId(hwClient.getTaskId(response.getHomeworkId())),
                        response.getTeacherId(),
                        List.of("check")
                )
        ) {
            throw new IllegalArgumentException("Teacher is not allowed to check homework");
        }

        //checking if response id is provided
        if (response.getId() == null)
            throw new NoSuchElementException("Response with id not provided");

        //deleting all previous comments of the response
        commentRepo.deleteAll(getResponse(response.getId()).getComments());

        //creating response, then saving it for comments' response id
        HwResponse savedResponse = hwResponseRepo.save(response);

        //setting comments' response id
        response.getComments().forEach(comment -> comment.setHwResponse(savedResponse));

        //saving all comments
        commentRepo.saveAll(response.getComments());

        return savedResponse;
    }
}
