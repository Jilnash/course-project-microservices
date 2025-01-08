package com.jilnash.fileservice.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface StorageService {

    String putFiles(String bucket, String filename, List<MultipartFile> fileContent) throws IOException;

    byte[] getFile(String bucket, String fileName) throws IOException;
}
