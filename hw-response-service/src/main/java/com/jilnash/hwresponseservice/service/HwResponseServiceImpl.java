package com.jilnash.hwresponseservice.service;

import com.jilnash.hwresponseservice.clients.CourseClient;
import com.jilnash.hwresponseservice.clients.CourseRightsGrpcClient;
import com.jilnash.hwresponseservice.clients.HwClient;
import com.jilnash.hwresponseservice.clients.ProgressGrpcClient;
import com.jilnash.hwresponseservice.model.mongo.HwResponse;
import com.jilnash.hwresponseservice.repo.HwResponseRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@Service
@RequiredArgsConstructor
public class HwResponseServiceImpl implements HwResponseService {

    private final MongoTemplate mongoTemplate;

    private final HwResponseRepo hwResponseRepo;

    private final CourseClient courseClient;

    private final HwClient hwClient;

    private final CourseRightsGrpcClient courseRightsGrpcClient;

    private final ProgressGrpcClient progressGrpcClient;

    @Override
    public List<HwResponse> getResponses(String teacherId, Long homeworkId, Date createdAfter, Date createdBefore) {

        log.info("[SERVICE] Fetching homework responses");
        log.debug("[SERVICE] Fetching homework responses with " +
                        "teacherId: {}, homeworkId: {}, createdAfter: {}, createdBefore: {}",
                teacherId, homeworkId, createdAfter, createdBefore);

        Query query = new Query();

        if (teacherId != null)
            query.addCriteria(Criteria.where("teacherId").is(teacherId));

        if (homeworkId != null)
            query.addCriteria(Criteria.where("homeworkId").is(homeworkId));

        if (createdAfter != null)
            query.addCriteria(Criteria.where("createdDate").gte(createdAfter));

        if (createdBefore != null)
            query.addCriteria(Criteria.where("createdDate").lte(createdBefore));

        return mongoTemplate.find(query, HwResponse.class);

    }

    @Override
    public HwResponse getResponse(String id) {

        log.info("[SERVICE] Fetching response");
        log.debug("[SERVICE] Fetching response with id: {}", id);

        return hwResponseRepo
                .findById(id)
                .orElseThrow(() -> new NoSuchElementException("Response with id " + id + " not found"));
    }

    @Override
    public Boolean createResponse(HwResponse response) {

        log.info("[SERVICE] Creating response");
        log.debug("[SERVICE] Creating response with teacherId: {}", response.getTeacherId());

        String taskId = hwClient.getTaskId(response.getHomeworkId());

        //checking if homework already checked
        if (hwResponseRepo.existsByHomeworkId(response.getHomeworkId()))
            throw new RuntimeException("Homework already checked");

        courseRightsGrpcClient.
                validateTeacherAllowedToCheckHomework(courseClient.getTaskCourseId(taskId), response.getTeacherId());

        //creating response
        hwResponseRepo.save(response);

        if (response.getIsCorrect())
            progressGrpcClient.addTaskToStudentProgress(hwClient.getStudentId(response.getHomeworkId()), taskId);

        hwClient.setChecked(response.getHomeworkId());

        return true;
    }

    @Override
    public Boolean updateResponse(HwResponse response) {

        log.info("[SERVICE] Updating response");
        log.debug("[SERVICE] Updating response with id: {} by teacherId: {}",
                response.getId(), response.getTeacherId());

        //checking if response id is provided
        if (response.getId() == null)
            throw new IllegalArgumentException("Response with id not provided");

        //checking if response exists, then getting id
        String id = hwResponseRepo
                .findById(response.getId())
                .orElseThrow(() -> new NoSuchElementException("Response with id " + response.getId() + " not found"))
                .getId();

        //checking if teacher has permissions to check homework
        courseRightsGrpcClient.
                validateTeacherAllowedToCheckHomework(
                        //getting courseId by hwId
                        courseClient.getTaskCourseId(hwClient.getTaskId(response.getHomeworkId())),
                        response.getTeacherId()
                );

        // deleting previous response
        hwResponseRepo.deleteById(id);

        // saving new response
        hwResponseRepo.save(response);

        return true;
    }
}
