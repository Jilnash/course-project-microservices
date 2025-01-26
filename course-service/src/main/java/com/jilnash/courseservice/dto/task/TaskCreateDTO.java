package com.jilnash.courseservice.dto.task;

import com.jilnash.courseservice.model.Module;
import com.jilnash.taskfilerequirementsservice.dto.TaskFileReqDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskCreateDTO {

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

    @NotNull(message = "Requirements are required")
    private String videoLink;

    @NotNull(message = "Requirements are required")
    private List<TaskFileReqDTO> fileRequirements;

    @NotNull(message = "Video file is required")
    private MultipartFile videoFile;

    private Module module;

    private Set<String> prerequisiteTasksIds;

    private Set<String> successorTasksIds;

    private Set<TaskLinkDTO> removeRelationshipIds;
}
