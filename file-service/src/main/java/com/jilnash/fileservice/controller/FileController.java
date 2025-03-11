package com.jilnash.fileservice.controller;

import com.jilnash.fileservice.dto.AppResponse;
import com.jilnash.fileservice.dto.FileUploadDTO;
import com.jilnash.fileservice.service.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/api/v1/files")
@RequiredArgsConstructor
public class FileController {

    private final S3Service s3Service;

    @GetMapping("/{bucketName}")
    public ResponseEntity<?> getFile(@PathVariable String bucketName,
                                     @RequestParam String fileName) throws IOException {

        log.info("[CONTROLLER] Fetching file from bucket");
        log.debug("[CONTROLLER] Fetching file from bucket: {}, with name: {}", bucketName, fileName);

        return ResponseEntity.ok(
                s3Service.getFile(bucketName, fileName)
        );
    }

    @GetMapping("/{bucketName}/presigned")
    public ResponseEntity<?> getPresignedUrl(@PathVariable String bucketName,
                                             @RequestParam String fileName) {

        log.info("[CONTROLLER] Fetching presigned url");
        log.debug("[CONTROLLER] Fetching presigned url for file: {}, in bucket: {}", fileName, bucketName);

        return ResponseEntity.ok(s3Service.getPreSignedUrl(bucketName, fileName));
    }


    @PostMapping
    public ResponseEntity<?> uploadFiles(@ModelAttribute @Validated FileUploadDTO fileUploadDTO) throws Exception {

        log.info("[CONTROLLER] Uploading files to bucket");
        log.debug("[CONTROLLER] Uploading files to bucket: {}, with name: {}",
                fileUploadDTO.bucket(), fileUploadDTO.fileName());

        return ResponseEntity.ok(
                new AppResponse(
                        200,
                        "File uploaded successfully",
                        s3Service.putFiles(fileUploadDTO.bucket(), fileUploadDTO.fileName(), fileUploadDTO.files())
                )
        );
    }
}
