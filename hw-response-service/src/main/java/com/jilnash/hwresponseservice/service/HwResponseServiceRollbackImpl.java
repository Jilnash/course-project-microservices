package com.jilnash.hwresponseservice.service;

import com.jilnash.hwresponseservice.model.mongo.HwResponse;
import com.jilnash.hwresponseservice.repo.HwResponseRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;

@Slf4j
@Component
@RequiredArgsConstructor
public class HwResponseServiceRollbackImpl implements HwResponseServiceRollback {

    private final HwResponseRepo hwResponseRepo;

    @Override
    public void rollbackCreateResponse(String responseId) {

        log.info("[ROLLBACK] Rolling back response creation");
        log.debug("[ROLLBACK] Rolling back creation of response with id: {}", responseId);

        if (hwResponseRepo.existsById(responseId))
            hwResponseRepo.deleteById(responseId);
    }

    @Override
    public void rollbackUpdateResponse(String responseId) {

        log.info("[ROLLBACK] Rolling back response update");
        log.debug("[ROLLBACK] Rolling back update of response with id: {}", responseId);
//
//        HwResponse response = hwResponseRepo.findById(responseId)
//                .orElseThrow(() -> new NoSuchElementException("Response with id " + responseId + " not found"));
//        response.setUpdatedAt(null);
//        hwResponseRepo.save(response);
    }

    @Override
    public void rollbackSoftDeleteResponse(String responseId) {

        log.info("[ROLLBACK] Rolling back response soft delete");
        log.debug("[ROLLBACK] Rolling back soft deletion of response with id: {}", responseId);

        HwResponse response = hwResponseRepo.findById(responseId)
                .orElseThrow(() -> new NoSuchElementException("Response with id " + responseId + " not found"));
        response.setDeletedAt(null);
        hwResponseRepo.save(response);
    }
}
