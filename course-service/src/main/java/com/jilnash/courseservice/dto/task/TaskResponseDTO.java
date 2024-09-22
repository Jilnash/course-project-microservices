package com.jilnash.courseservice.dto.task;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskResponseDTO {

    private String id;

    private String title;

    private String description;

    private String videoLink;

    private Boolean audioRequired;

    private Boolean videoRequired;
}
