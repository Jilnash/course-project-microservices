package com.jilnash.fileservice.service;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface StorageServiceRollback {

    void rollbackFileUpload(String bucket, List<String> fileNames) throws Exception;

    void rollbackFileDeletion(String bucket, String fileName);

    void rollbackFileUpdate(String oldBucket, String oldFileName, String newBucket, String newFileName) throws Exception;
}
