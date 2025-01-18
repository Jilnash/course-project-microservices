package com.jilnash.taskfilerequirementsservice.dto;

import jakarta.validation.constraints.Min;
import lombok.Builder;

@Builder
public record TaskFileReqDTO(String contentType,
                             @Min(value = 1, message = "Count should be more than 0") short count) {

}
