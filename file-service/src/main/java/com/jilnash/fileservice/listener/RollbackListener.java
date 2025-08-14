package com.jilnash.fileservice.listener;

import com.jilnash.fileservice.service.StorageServiceRollback;
import com.jilnash.fileservicedto.dto.FileUploadRollbackDTO;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class RollbackListener {

    private final StorageServiceRollback storageServiceRollback;

    public RollbackListener(StorageServiceRollback storageServiceRollback) {
        this.storageServiceRollback = storageServiceRollback;
    }

    @KafkaListener(topics = "file-upload-rollback-topic", groupId = "file-service")
    public void rollbackFileUpload(FileUploadRollbackDTO dto) {
        try {
            storageServiceRollback.rollbackFileUpload(dto.bucketName(), dto.fileNames());
        } catch (Exception e) {
            throw new RuntimeException("Failed to rollback file upload for bucket: " + dto.bucketName(), e);
        }
    }
}
