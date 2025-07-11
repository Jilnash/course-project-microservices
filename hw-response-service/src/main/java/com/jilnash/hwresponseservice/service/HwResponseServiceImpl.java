package com.jilnash.hwresponseservice.service;

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
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class HwResponseServiceImpl implements HwResponseService {

    private final MongoTemplate mongoTemplate;

    private final HwResponseRepo hwResponseRepo;

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

        //checking if homework already checked
        if (hwResponseRepo.existsByHomeworkId(response.getHomeworkId()))
            throw new RuntimeException("Homework already checked");

        //creating response
        hwResponseRepo.save(response);

        return true;
    }

    @Override
    public Boolean updateResponse(HwResponse response) {

        log.info("[SERVICE] Updating response");
        log.debug("[SERVICE] Updating response with id: {} by teacherId: {}",
                response.getId(), response.getTeacherId());

        //checking if response exists, then soft deleting it
        HwResponse previousResponse = hwResponseRepo.findById(response.getId())
                .orElseThrow(() -> new NoSuchElementException("Response with id " + response.getId() + " not found"));
        previousResponse.setDeletedAt(new Date());
        hwResponseRepo.save(previousResponse);

        //creating new response
        response.setId(UUID.randomUUID().toString()); // Retain the same ID
        response.setUpdatedAt(new Date());
        response.setCreatedAt(previousResponse.getCreatedAt());
        hwResponseRepo.save(response);

        return true;
    }

    @Override
    public Boolean softDeleteResponse(String responseId) {
        log.info("[SERVICE] Soft deleting response");
        log.debug("[SERVICE] Soft deleting response with id: {}", responseId);

        HwResponse response = hwResponseRepo.findById(responseId)
                .orElseThrow(() -> new NoSuchElementException("Response with id " + responseId + " not found"));
        response.setDeletedAt(new Date());
        hwResponseRepo.save(response);

        return true;
    }

    @Override
    public Boolean hardDeleteResponse(String responseId) {
        return hwResponseRepo
                .findById(responseId)
                .map(response -> {
                    log.info("[SERVICE] Hard deleting response");
                    log.debug("[SERVICE] Hard deleting response with id: {}", responseId);
                    hwResponseRepo.delete(response);
                    return true;
                })
                .orElseThrow(() -> new NoSuchElementException("Response with id " + responseId + " not found"));
    }
}
