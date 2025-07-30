package com.jilnash.courserightsservicedto.dto;

import java.util.Set;

public record SetRightsDTO(
        String transactionId,
        String courseId,
        String teacherId,
        Set<String> rights) {
}
