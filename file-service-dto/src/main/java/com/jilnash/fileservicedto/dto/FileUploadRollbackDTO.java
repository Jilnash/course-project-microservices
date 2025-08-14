package com.jilnash.fileservicedto.dto;

import java.util.List;

public record FileUploadRollbackDTO(String transactionId, String bucketName, List<String> fileNames) {
}
