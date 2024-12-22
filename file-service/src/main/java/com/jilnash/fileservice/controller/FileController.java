package com.jilnash.fileservice.controller;

import com.jilnash.fileservice.dto.AppResponse;
import com.jilnash.fileservice.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/files")
@RequiredArgsConstructor
public class FileController {

    private final S3Service s3Service;

    @GetMapping("/{bucketName}")
    public ResponseEntity<?> getFile(@PathVariable String bucketName,
                                     @RequestParam String fileName) throws IOException {

        return ResponseEntity.ok(
                s3Service.getFile(bucketName, fileName)
        );
    }

    @PostMapping
    public ResponseEntity<?> uploadFile(@RequestParam MultipartFile file,
                                        @RequestParam String fileName,
                                        @RequestParam String bucket) throws Exception {

        return ResponseEntity.ok(
                new AppResponse(
                        200,
                        "File uploaded successfully",
                        s3Service.putFile(bucket, fileName, file)
                )
        );
    }
}
