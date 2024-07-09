package com.jilnash.fileservice.controller;

import com.jilnash.fileservice.dto.AppResponse;
import com.jilnash.fileservice.service.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/files")
public class FileController {

    @Autowired
    private S3Service s3Service;

    @GetMapping("/{bucketName}")
    public ResponseEntity<?> getFile(@PathVariable String bucketName) throws IOException {

        return ResponseEntity.ok(
                s3Service.getFileFromS3(bucketName).getContentAsByteArray()
        );
    }

    @PostMapping("/{bucketName}")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file,
                                        @PathVariable String bucketName) throws Exception {

        s3Service.putFileToS3(file, bucketName);

        return ResponseEntity.ok(
                new AppResponse(
                        200,
                        "File uploaded successfully",
                        null
                )
        );
    }
}
