package com.jilnash.hwresponseservice.service;

import com.jilnash.courserightsservice.HasRightsRequest;
import com.jilnash.courserightsservice.TeacherRightsServiceGrpc;
import com.jilnash.hwresponseservice.clients.CourseClient;
import com.jilnash.hwresponseservice.clients.HwClient;
import com.jilnash.hwresponseservice.model.HwResponse;
import com.jilnash.hwresponseservice.repo.CommentRepo;
import com.jilnash.hwresponseservice.repo.HwResponseRepo;
import com.jilnash.progressservice.AddTaskToProgressRequest;
import com.jilnash.progressservice.ProgressServiceGrpc;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.client.inject.GrpcClient;
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

    private final CourseClient courseClient;
    private final HwClient hwClient;

    @GrpcClient("course-rights-grpc-client")
    private TeacherRightsServiceGrpc.TeacherRightsServiceBlockingStub courseRightsServiceGrpc;

    @GrpcClient("progress-grpc-client")
    private ProgressServiceGrpc.ProgressServiceBlockingStub progressServiceGrpc;

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
        //getting courseId by hwId
        validateTeacherAllowedToCheckHomework(courseClient.getTaskCourseId(taskId), response.getTeacherId());

        //creating response, then saving it for comments' response id
        HwResponse savedResponse = hwResponseRepo.save(response);

        //setting comments' response id
        response.getComments().forEach(comment -> comment.setHwResponse(savedResponse));

        //saving all comments
        commentRepo.saveAll(response.getComments());

        if (response.getIsCorrect())
            addTaskToStudentProgress(hwClient.getStudentId(response.getHomeworkId()), taskId);

        //todo: change hw status to `checked`

        return savedResponse;
    }

    private void validateTeacherAllowedToCheckHomework(String courseId, String teacherId) {

        if (!courseRightsServiceGrpc.hasRights(HasRightsRequest.newBuilder()
                .setCourseId(courseId)
                .setTeacherId(teacherId)
                .addRights("RESPOND_HW")
                .build()).getHasRights()
        )
            throw new IllegalArgumentException("Teacher is not allowed to respond to homework");

    }

    private void addTaskToStudentProgress(String studentId, String taskId) {
        progressServiceGrpc.addTaskToProgress(
                AddTaskToProgressRequest.newBuilder()
                        .setStudentId(studentId)
                        .setTaskId(taskId)
                        .build()
        );
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
