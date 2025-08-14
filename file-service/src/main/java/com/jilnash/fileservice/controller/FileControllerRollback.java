package com.jilnash.fileservice.controller;

import com.jilnash.fileservice.service.MinIOServiceRollback;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/files/rollback")
@RequiredArgsConstructor
public class FileControllerRollback {

    private final MinIOServiceRollback storageServiceRollback;

    @PostMapping
    public ResponseEntity<?> rollbackFileUpload(@RequestParam String bucket,
                                                @RequestParam String fileName) throws Exception {
        System.out.println(bucket);

//        storageServiceRollback.rollbackFileUpload(bucket, fileName);

        return ResponseEntity.ok("File upload rollback successful");
    }

    @PutMapping
    public ResponseEntity<?> rollbackFileUpdate(@RequestParam String oldBucket,
                                                @RequestParam String oldFileName,
                                                @RequestParam String newBucket,
                                                @RequestParam String newFileName) throws Exception {

        storageServiceRollback.rollbackFileUpdate(oldBucket, oldFileName, newBucket, newFileName);

        return ResponseEntity.ok("File update rollback successful");
    }

    @DeleteMapping
    public ResponseEntity<?> rollbackFileDeletion(@RequestParam String bucket,
                                                  @RequestParam String fileName) {

        storageServiceRollback.rollbackFileDeletion(bucket, fileName);

        return ResponseEntity.ok("File deletion rollback successful");
    }
}
