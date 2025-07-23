package com.jilnash.courseservicedto.dto.task;

import java.util.Set;

public record TaskUpdatePrereqsDTO(String courseId, String moduleId, String taskId, Set<String> prerequisiteTaskIds) {
}
