package com.jilnash.hwresponseservice.controller.v1;

import com.jilnash.hwresponseservice.dto.AppResponse;
import com.jilnash.hwresponseservice.dto.HwResponseDTO;
import com.jilnash.hwresponseservice.mapper.HwResponseMapper;
import com.jilnash.hwresponseservice.service.HwResponseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/api/v1/hw-responses")
public class HwResponseController {

    @Autowired
    private HwResponseService responseService;

    @Autowired
    private HwResponseMapper responseMapper;

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
    public ResponseEntity<?> createResponse(@Validated @RequestBody HwResponseDTO responseDto) {

        return ResponseEntity.ok(
                new AppResponse(
                        200,
                        "Response created successfully",
                        responseService.createResponse(responseMapper.toEntity(responseDto))
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getResponse(@PathVariable Long id) {

        return ResponseEntity.ok(
                new AppResponse(
                        200,
                        "Response fetched successfully",
                        responseService.getResponse(id)
                )
        );
    }

    @PostMapping("/{id}")
    public ResponseEntity<?> updateResponse(@PathVariable Long id, @Validated @RequestBody HwResponseDTO responseDto) {

        responseDto.setId(id);

        return ResponseEntity.ok(
                new AppResponse(
                        200,
                        "Response updated successfully",
                        responseService.updateResponse(responseMapper.toEntity(responseDto))
                )
        );
    }
}
