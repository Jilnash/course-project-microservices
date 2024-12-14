package com.jilnash.hwresponseservice.service;

import com.jilnash.hwresponseservice.clients.CourseClient;
import com.jilnash.hwresponseservice.clients.CourseRightsClient;
import com.jilnash.hwresponseservice.clients.HwClient;
import com.jilnash.hwresponseservice.clients.ProgressClient;
import com.jilnash.hwresponseservice.model.HwResponse;
import com.jilnash.hwresponseservice.repo.CommentRepo;
import com.jilnash.hwresponseservice.repo.HwResponseRepo;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class HwResponseServiceImpl implements HwResponseService {

    private final HwResponseRepo hwResponseRepo;
    private final CommentRepo commentRepo;
    private final CourseRightsClient courseRightsClient;
    private final CourseClient courseClient;
    private final HwClient hwClient;
    private final ProgressClient progressClient;

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

        String taskId = hwClient.getTaskId(response.getHomeworkId());
        System.out.println(taskId);

        validateTeacherAllowedToCheckHomework(
                //getting courseId by hwId
                courseClient.getTaskCourseId(taskId),
                response.getTeacherId()
        );

        //creating response, then saving it for comments' response id
        HwResponse savedResponse = hwResponseRepo.save(response);

        //setting comments' response id
        response.getComments().forEach(comment -> comment.setHwResponse(savedResponse));

        //saving all comments
        commentRepo.saveAll(response.getComments());

        if (response.getIsCorrect())
            progressClient.addTaskToStudentProgress(hwClient.getStudentId(response.getHomeworkId()), taskId);

        return savedResponse;
    }

    private void validateTeacherAllowedToCheckHomework(String courseId, String teacherId) {
        //todo: change to "check" right
        if (courseRightsClient.hasRights(courseId, teacherId, List.of("edit"))) {
            throw new IllegalArgumentException("Teacher is not allowed to check homework");
        }
    }

    @Override
    public HwResponse updateResponse(HwResponse response) {

        //checking if teacher is allowed to check homework
        validateTeacherAllowedToCheckHomework(
                //getting courseId by hwId
                courseClient.getTaskCourseId(hwClient.getTaskId(response.getHomeworkId())),
                response.getTeacherId()
        );

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
