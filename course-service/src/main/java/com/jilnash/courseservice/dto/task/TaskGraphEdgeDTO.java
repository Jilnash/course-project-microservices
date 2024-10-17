package com.jilnash.courseservice.dto.task;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskGraphEdgeDTO implements Serializable {

    private String from;

    private String to;
}
