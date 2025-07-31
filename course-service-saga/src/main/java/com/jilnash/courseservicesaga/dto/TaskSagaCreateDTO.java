package com.jilnash.courseservicesaga.dto;

import com.jilnash.taskrequirementsservicedto.dto.FileReqirement;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskSagaCreateDTO {

    private String courseId;
    private String moduleId;
    private String taskId;
    private String authorId;

    @NotNull(message = "Title cannot be null")
    @NotEmpty(message = "Title cannot be empty")
    private String title;

    @NotNull(message = "Description cannot be null")
    @NotEmpty(message = "Description cannot be empty")
    private String description;

    @NotNull(message = "Video file cannot be null")
    private MultipartFile videoFile;

    @NotNull(message = "IsPublic cannot be null")
    private Boolean isPublic;

    @NotNull(message = "HwPostingInterval cannot be null")
    private Integer hwPostingInterval;

    private Set<String> prerequisiteTasksIds;

    private Set<String> successorTasksIds;

    @NotNull(message = "Requirements cannot be null")
    @NotEmpty(message = "Requirements cannot be empty")
    private List<FileReqirement> reqirements;
}
