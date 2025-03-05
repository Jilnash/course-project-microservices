package com.jilnash.homeworkservice.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
public class HomeworkResponseDTO {

    private UUID id;
    private String studentId;
    private String taskId;
    private Boolean checked;
    private Integer attempt;
    private List<String> fileNames;
}
