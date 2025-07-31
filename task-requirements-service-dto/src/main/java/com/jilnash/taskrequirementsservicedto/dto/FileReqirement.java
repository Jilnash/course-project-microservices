package com.jilnash.taskrequirementsservicedto.dto;

import lombok.Builder;

@Builder
public record FileReqirement(String contentType, int count) {
}
