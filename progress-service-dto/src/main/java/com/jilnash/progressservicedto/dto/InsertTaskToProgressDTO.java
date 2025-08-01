package com.jilnash.progressservicedto.dto;

import java.util.List;

public record InsertTaskToProgressDTO(String transactionId,
                                      String taskId,
                                      List<String> prerequisites) {
}
