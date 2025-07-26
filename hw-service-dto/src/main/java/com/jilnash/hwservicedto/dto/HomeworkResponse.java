package com.jilnash.hwservicedto.dto;

import java.util.List;

public record HomeworkResponse(String homeworkId, String taskId, String studentId, Boolean checked,
                               List<String> fileNames) {
}
