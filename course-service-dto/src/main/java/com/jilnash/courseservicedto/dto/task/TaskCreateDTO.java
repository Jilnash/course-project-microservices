package com.jilnash.courseservicedto.dto.task;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskCreateDTO {

    private String courseId;
    private String moduleId;
    private String taskId;
    private String authorId;
    private String title;
    private String description;
    private String videoFileName;
    private Boolean isPublic;
    private Integer hwPostingInterval;
    private Set<String> prerequisiteTasksIds;
    private Set<String> successorTasksIds;
}
