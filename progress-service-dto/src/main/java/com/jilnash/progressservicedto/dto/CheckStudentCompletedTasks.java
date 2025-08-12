package com.jilnash.progressservicedto.dto;

import java.util.List;

public record CheckStudentCompletedTasks(String transactionId, String studentId, List<String> taskIds) {
}
