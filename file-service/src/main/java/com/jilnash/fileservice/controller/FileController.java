package com.jilnash.fileservice.controller;

import com.jilnash.fileservice.dto.AppResponse;
import com.jilnash.fileservice.dto.FileUploadDTO;
import com.jilnash.fileservice.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/{bucketName}/presigned")
    public ResponseEntity<?> getPresignedUrl(@PathVariable String bucketName,
                                             @RequestParam String fileName) {

        return ResponseEntity.ok(
                new AppResponse(
                        200,
                        "Presigned URL generated successfully",
                        s3Service.getPreSignedUrl(bucketName, fileName)
                )
        );
    }


    @PostMapping
    public ResponseEntity<?> uploadFiles(@ModelAttribute @Validated FileUploadDTO fileUploadDTO) throws Exception {

        return ResponseEntity.ok(
                new AppResponse(
                        200,
                        "File uploaded successfully",
                        s3Service.putFiles(fileUploadDTO.bucket(), fileUploadDTO.fileName(), fileUploadDTO.files())
                )
        );
    }
}
