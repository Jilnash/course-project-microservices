package com.jilnash.hwservicedto.dto;

import java.util.List;

public record HomeworkCreateDTO(
        String transactionId,
        String homeworkid,
        String taskId,
        String studentId,
        List<String> fileNames) {
}

