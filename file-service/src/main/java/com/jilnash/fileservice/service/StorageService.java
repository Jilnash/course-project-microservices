package com.jilnash.fileservice.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface StorageService {

    Boolean putFiles(String bucket, String filename, List<MultipartFile> fileContent) throws Exception;

    byte[] getFile(String bucket, String fileName) throws Exception;

    String getPreSignedUrl(String bucket, String fileName) throws Exception;
}
