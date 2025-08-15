package com.jilnash.fileservicedto.dto;

public record FileUpdateRollbackDTO(String transactionId, String binBucket, String workingBucket, String fileName) {
}
