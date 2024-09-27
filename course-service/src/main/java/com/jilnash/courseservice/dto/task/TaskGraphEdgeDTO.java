package com.jilnash.courseservice.dto.task;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskGraphEdgeDTO {

    private String from;

    private String to;
}
