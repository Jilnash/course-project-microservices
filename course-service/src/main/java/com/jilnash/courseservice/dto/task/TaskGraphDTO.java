package com.jilnash.courseservice.dto.task;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskGraphDTO implements Serializable {

    private List<String> nodes;

    private List<TaskGraphEdgeDTO> edges;
}
