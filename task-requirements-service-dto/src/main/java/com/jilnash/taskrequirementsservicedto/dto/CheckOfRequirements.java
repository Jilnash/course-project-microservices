package com.jilnash.taskrequirementsservicedto.dto;

import java.util.List;

public record CheckOfRequirements(String transactionId, String taskId, List<String> contentTypes) {
}
