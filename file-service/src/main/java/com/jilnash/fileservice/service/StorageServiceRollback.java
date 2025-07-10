package com.jilnash.fileservice.service;

import org.springframework.stereotype.Service;

@Service
public interface StorageServiceRollback {

    void rollbackFileUpload(String bucket, String fileName) throws Exception;

    void rollbackFileDeletion(String bucket, String fileName);

    void rollbackFileUpdate(String oldBucket, String oldFileName, String newBucket, String newFileName) throws Exception;
}
