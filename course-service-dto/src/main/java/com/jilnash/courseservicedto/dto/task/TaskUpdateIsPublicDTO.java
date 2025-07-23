package com.jilnash.courseservicedto.dto.task;

public record TaskUpdateIsPublicDTO(String courseId, String moduleId, String id, boolean isPublic) {
}
