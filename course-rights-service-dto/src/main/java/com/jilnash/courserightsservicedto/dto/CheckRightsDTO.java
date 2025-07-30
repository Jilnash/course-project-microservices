package com.jilnash.courserightsservicedto.dto;

import java.util.Set;

public record CheckRightsDTO(
        String transactionId,
        String courseId,
        String teacherId, Set<
        String> rights) {
}
