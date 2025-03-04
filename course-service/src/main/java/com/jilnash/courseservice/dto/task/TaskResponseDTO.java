package com.jilnash.courseservice.dto.task;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskResponseDTO implements Serializable {

    private String id;

    private String title;

    private String description;

    private String videoLink;

    private Boolean isPublic;

    private List<String> prerequisites;

    private List<String> successors;
}
