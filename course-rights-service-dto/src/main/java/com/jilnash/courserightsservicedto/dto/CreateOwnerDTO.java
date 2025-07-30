package com.jilnash.courserightsservicedto.dto;

public record CreateOwnerDTO(
        String transactionId,
        String courseId,
        String teacherId) {
}
