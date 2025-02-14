package com.jilnash.hwresponseservice.controller.v1;

import com.jilnash.hwresponseservice.dto.AppResponse;
import com.jilnash.hwresponseservice.dto.response.HwResponseDTO;
import com.jilnash.hwresponseservice.mapper.HwResponseMapper;
import com.jilnash.hwresponseservice.service.HwResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.UUID;

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

        return ResponseEntity.ok(
                new AppResponse(
                        200,
                        "Responses fetched successfully",
                        responseService.getResponses(tId, hwId, createdAfter, createdBefore)
                )
        );
    }

    @PutMapping
    public ResponseEntity<?> createResponse(@Validated @RequestBody HwResponseDTO responseDto,
                                            @RequestHeader("X-User-Sub") String teacherId) {

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

        return ResponseEntity.ok(
                new AppResponse(
                        200,
                        "Response fetched successfully",
                        responseService.getResponse(id)
                )
        );
    }

    @PostMapping("/{id}")
    public ResponseEntity<?> updateResponse(@PathVariable String id,
                                            @Validated @RequestBody HwResponseDTO responseDto,
                                            @RequestHeader("X-User-Sub") String teacherId) {

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
}
