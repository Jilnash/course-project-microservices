package com.jilnash.hwresponseservicesaga.controller;

import com.jilnash.hwresponseservicedto.dto.ResponseCreateDTO;
import com.jilnash.hwresponseservicesaga.service.ResponseServiceSaga;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/hw-responses")
public class HwResponseSagaController {

    private final ResponseServiceSaga responseService;

    public HwResponseSagaController(ResponseServiceSaga responseService) {
        this.responseService = responseService;
    }

    @PostMapping
    public ResponseEntity<?> createResponse(@Validated @RequestBody ResponseCreateDTO responseDto,
                                            @RequestHeader("X-User-Sub") String teacherId) {

        log.info("[CONTROLLER] Creating response");
        log.debug("[CONTROLLER] Creating response with teacherId: {}", teacherId);

        responseDto.setTeacherId(teacherId);
        responseDto.setId(UUID.randomUUID().toString());
        responseService.createResponse(responseDto);

        return ResponseEntity.ok("Response created successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateResponse(@PathVariable String id,
                                            @Validated @RequestBody ResponseCreateDTO responseDto,
                                            @RequestHeader("X-User-Sub") String teacherId) {

        log.info("[CONTROLLER] Updating response");
        log.debug("[CONTROLLER] Updating response with id: {} by teacherId: {}", id, teacherId);

        responseDto.setId(id);
        responseDto.setTeacherId(teacherId);
        responseService.updateResponse(responseDto);

        return ResponseEntity.ok("Response updated successfully");

    }

    @DeleteMapping("/{id}/soft")
    public ResponseEntity<?> softDeleteResponse(@PathVariable String id,
                                                @RequestParam String courseId,
                                                @RequestHeader("X-User-Sub") String teacherId) {

        log.info("[CONTROLLER] Soft deleting response");
        log.debug("[CONTROLLER] Soft deleting response with id: {}", id);
        responseService.softDeleteResponse(courseId, teacherId, id);

        return ResponseEntity.ok("Response soft deleted successfully");

    }

    @DeleteMapping("/{id}/hard")
    public ResponseEntity<?> hardDeleteResponse(@PathVariable String id,
                                                @RequestParam String courseId,
                                                @RequestHeader("X-User-Sub") String teacherId) {

        log.info("[CONTROLLER] Hard deleting response");
        log.debug("[CONTROLLER] Hard deleting response with id: {}", id);

        responseService.hardDeleteResponse(courseId, teacherId, id);
        return ResponseEntity.ok("Response hard deleted successfully");
    }
}
