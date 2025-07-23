package com.jilnash.courseservicedto.dto.task;

import java.util.Set;

public record TaskUpdateSuccessorsDTO(String courseId, String moduleId, String taskId, Set<String> successorTasksIds) {
}
