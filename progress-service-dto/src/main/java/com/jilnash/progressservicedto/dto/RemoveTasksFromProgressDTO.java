package com.jilnash.progressservicedto.dto;

import java.util.List;

public record RemoveTasksFromProgressDTO(
        String transactionId,
        List<String> taskIds
) {
}
