package com.jilnash.hwservicedto.dto;

import java.util.List;

public record HomeworkCreateDTO(
        String homeworkid,
        String taskId,
        String studentId,
        List<String> fileNames) {
}

