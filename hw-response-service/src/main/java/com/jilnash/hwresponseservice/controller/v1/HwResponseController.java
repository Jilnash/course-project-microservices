package com.jilnash.hwresponseservice.controller.v1;

import com.jilnash.hwresponseservice.dto.AppResponse;
import com.jilnash.hwresponseservice.dto.response.HwResponseDTO;
import com.jilnash.hwresponseservice.mapper.HwResponseMapper;
import com.jilnash.hwresponseservice.service.HwResponseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/hw-responses")
@RequiredArgsConstructor
public class HwResponseController {

    private final HwResponseService responseService;

    private final HwResponseMapper responseMapper;

    @GetMapping
    public ResponseEntity<?> getResponses(
            @RequestParam(required = false) Long hwId,
            @RequestParam(required = false) String tId,
            @RequestParam(required = false) Date createdAfter,
            @RequestParam(required = false) Date createdBefore) {

        log.info("[CONTROLLER] Fetching homework responses");
        log.debug("[CONTROLLER] Fetching homework responses with hwId: {}, tId: {}, createdAfter: {}, createdBefore: {}"
                , hwId, tId, createdAfter, createdBefore);

        return ResponseEntity.ok(
                new AppResponse(
                        200,
                        "Responses fetched successfully",
                        responseService.getResponses(tId, hwId, createdAfter, createdBefore)
                )
        );
    }

    @PostMapping
    public ResponseEntity<?> createResponse(@Validated @RequestBody HwResponseDTO responseDto,
                                            @RequestHeader("X-User-Sub") String teacherId) {

        log.info("[CONTROLLER] Creating response");
        log.debug("[CONTROLLER] Creating response with teacherId: {}", teacherId);

        responseDto.setTeacherId(teacherId);
        responseDto.setId(UUID.randomUUID().toString());

        return ResponseEntity.ok(
                new AppResponse(
                        200,
                        "Response created successfully",
                        responseService.createResponse(responseMapper.toEntity(responseDto))
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getResponse(@PathVariable String id) {

        log.info("[CONTROLLER] Fetching response");
        log.debug("[CONTROLLER] Fetching response with id: {}", id);

        return ResponseEntity.ok(
                new AppResponse(
                        200,
                        "Response fetched successfully",
                        responseService.getResponse(id)
                )
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateResponse(@PathVariable String id,
                                            @Validated @RequestBody HwResponseDTO responseDto,
                                            @RequestHeader("X-User-Sub") String teacherId) {

        log.info("[CONTROLLER] Updating response");
        log.debug("[CONTROLLER] Updating response with id: {} by teacherId: {}", id, teacherId);

        responseDto.setId(id);
        responseDto.setTeacherId(teacherId);

        return ResponseEntity.ok(
                new AppResponse(
                        200,
                        "Response updated successfully",
                        responseService.updateResponse(responseMapper.toEntity(responseDto))
                )
        );
    }

    @DeleteMapping("/{id}/soft")
    public ResponseEntity<?> softDeleteResponse(@PathVariable String id) {

        log.info("[CONTROLLER] Soft deleting response");
        log.debug("[CONTROLLER] Soft deleting response with id: {}", id);

        return ResponseEntity.ok(
                new AppResponse(
                        200,
                        "Response soft deleted successfully",
                        responseService.softDeleteResponse(id)
                )
        );
    }

    @DeleteMapping("/{id}/hard")
    public ResponseEntity<?> hardDeleteResponse(@PathVariable String id) {

        log.info("[CONTROLLER] Hard deleting response");
        log.debug("[CONTROLLER] Hard deleting response with id: {}", id);

        return ResponseEntity.ok(
                new AppResponse(
                        200,
                        "Response hard deleted successfully",
                        responseService.hardDeleteResponse(id)
                )
        );
    }
}
