package com.jilnash.taskrequirementsservicedto.dto;

import java.util.List;

public record SetRequirements(String transactionId, String taskId, List<FileReqirement> requirements) {
}
