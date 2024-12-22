package com.jilnash.fileservice.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface StorageService {

    String putFile(String bucket, String filename, MultipartFile fileContent) throws IOException;

    byte[] getFile(String bucket, String fileName) throws IOException;
}
