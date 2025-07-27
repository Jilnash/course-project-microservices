package com.jilnash.hwresponseservice.controller.v1;

import com.jilnash.hwresponseservice.dto.AppResponse;
import com.jilnash.hwresponseservice.service.HwResponseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Slf4j
@RestController
@RequestMapping("/api/v1/hw-responses")
@RequiredArgsConstructor
public class HwResponseController {

    private final HwResponseService responseService;

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
}
