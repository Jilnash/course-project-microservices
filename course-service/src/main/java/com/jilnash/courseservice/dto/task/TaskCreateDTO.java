package com.jilnash.courseservice.dto.task;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskCreateDTO {

    @NotNull(message = "Task ID is required")
    private String taskId;

    private String courseId;

    private String moduleId;

    private String teacherId;

    @NotNull(message = "Title is required")
    @NotBlank(message = "Title is required")
    private String title;

    @NotNull(message = "Description is required")
    @NotBlank(message = "Description is required")
    private String description;

    private String videoFileName;

//    @NotNull(message = "Requirements are required")
//    private List<TaskFileReqDTO> fileRequirements;

    @NotNull(message = "Is public is required")
    private Boolean isPublic;

//    private Module module;

    private Set<String> prerequisiteTasksIds;

    private Set<String> successorTasksIds;

//    private Set<TaskLinkDTO> removeRelationshipIds;
}
