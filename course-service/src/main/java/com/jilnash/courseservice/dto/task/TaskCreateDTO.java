package com.jilnash.courseservice.dto.task;

import com.jilnash.courseservice.model.Module;
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

    private String taskId;

    private String courseId;

    private String moduleId;

//    @NotNull(message = "Author id is required")
//    @Min(value = 1, message = "Author id must be greater than 0")
//    private Long author;

    @NotNull(message = "Title is required")
    @NotBlank(message = "Title is required")
    private String title;

    @NotNull(message = "Description is required")
    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "Audio link is required")
    private String videoLink;

    private Module module;

    private Set<String> prerequisiteTasksIds;

    private Set<String> successorTasksIds;

    private Set<TaskLinkDTO> removeRelationshipIds;
}
