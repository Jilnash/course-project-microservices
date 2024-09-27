package com.jilnash.courseservice.dto.task;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskGraphDTO {

    private List<String> nodes;

    private List<TaskGraphEdgeDTO> edges;
}
