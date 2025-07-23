package com.jilnash.courseservicedto.dto.task;

import java.util.List;

public record TaskResponse(
        String id,
        String title,
        String description,
        String videoFileName,
        boolean isPublic,
        List<String> prerequisites,
        List<String> successors
) {
}
