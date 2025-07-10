package com.jilnash.fileservice.controller;

import com.jilnash.fileservice.dto.AppResponse;
import com.jilnash.fileservice.dto.FileUploadDTO;
import com.jilnash.fileservice.service.MinIOService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for handling file-related operations using MinIO storage.
 * Provides endpoints for downloading files, generating pre-signed URLs, and uploading files.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/files")
@RequiredArgsConstructor
public class FileController {

    private final MinIOService storageService;

    /**
     * Retrieves a file from the specified bucket in MinIO storage.
     *
     * @param bucketName the name of the bucket from which the file is being retrieved
     * @param fileName   the name of the file to be fetched
     * @return a ResponseEntity containing the file data, or appropriate error details if the file cannot be retrieved
     * @throws Exception if an error occurs during the file retrieval process
     */
    @GetMapping("/{bucketName}")
    public ResponseEntity<?> getFile(@PathVariable String bucketName,
                                     @RequestParam String fileName) throws Exception {

        log.info("[CONTROLLER] Fetching file from bucket");
        log.debug("[CONTROLLER] Fetching file from bucket: {}, with name: {}", bucketName, fileName);

        return ResponseEntity.ok(
                storageService.getFile(bucketName, fileName)
        );
    }

    /**
     * Generates a pre-signed URL for accessing a specific file stored in a MinIO bucket.
     * The pre-signed URL provides temporary access to the file and allows HTTP GET requests.
     *
     * @param bucketName the name of the bucket containing the file for which the pre-signed URL is generated
     * @param fileName   the name of the file for which the pre-signed URL is generated
     * @return a ResponseEntity containing the generated pre-signed URL, or an appropriate error message if the URL cannot
     * be generated
     * @throws Exception if an error occurs during the pre-signed URL generation process
     */
    @GetMapping("/{bucketName}/presigned")
    public ResponseEntity<?> getPresignedUrl(@PathVariable String bucketName,
                                             @RequestParam String fileName) throws Exception {

        log.info("[CONTROLLER] Fetching presigned url");
        log.debug("[CONTROLLER] Fetching presigned url for file: {}, in bucket: {}", fileName, bucketName);

        return ResponseEntity.ok(storageService.getPreSignedUrl(bucketName, fileName));
    }


    /**
     * Uploads multiple files to the specified bucket in MinIO storage.
     * Files are stored under a directory named by the provided filename prefix.
     *
     * @param fileUploadDTO the data transfer object containing the bucket name,
     *                      the filename prefix, and the list of files to be uploaded
     * @return a ResponseEntity containing an AppResponse with the status, message,
     * and the result of the upload operation
     * @throws Exception if an error occurs during the file upload process, including issues
     *                   with bucket creation, file storage, or other related problems
     */
    @PostMapping
    public ResponseEntity<?> uploadFiles(@ModelAttribute @Validated FileUploadDTO fileUploadDTO) throws Exception {

        log.info("[CONTROLLER] Uploading files to bucket");
        log.debug("[CONTROLLER] Uploading files to bucket: {}, with name: {}",
                fileUploadDTO.bucket(), fileUploadDTO.fileName());

        return ResponseEntity.ok(
                new AppResponse(
                        200,
                        "File uploaded successfully",
                        storageService.uploadFiles(fileUploadDTO.bucket(), fileUploadDTO.fileName(), fileUploadDTO.files())
                )
        );
    }

    @DeleteMapping("{bucketName}/soft")
    public ResponseEntity<?> deleteFile(@PathVariable String bucketName,
                                        @RequestParam String fileName) {

        log.info("[CONTROLLER] Deleting file from bucket");
        log.debug("[CONTROLLER] Deleting file: {} from bucket: {}", fileName, bucketName);

        storageService.softDeleteFile(bucketName, fileName);

        return ResponseEntity.ok("File deleted successfully");
    }
}
