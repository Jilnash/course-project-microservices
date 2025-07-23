package com.jilnash.courseservicedto.dto.task;

import java.util.Set;

public class TaskCreateDTO {

    private String courseId;
    private String moduleId;
    private String taskId;
    private String authorId;
    private String title;
    private String description;
    private String videoFileName;
    private String isPublic;
    private Set<String> prerequisiteTasksIds;
    private Set<String> successorTasksIds;

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getModuleId() {
        return moduleId;
    }

    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVideoFileName() {
        return videoFileName;
    }

    public void setVideoFileName(String videoFileName) {
        this.videoFileName = videoFileName;
    }

    public String getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(String isPublic) {
        this.isPublic = isPublic;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public Set<String> getPrerequisiteTasksIds() {
        return prerequisiteTasksIds;
    }

    public void setPrerequisiteTasksIds(Set<String> prerequisiteTasksIds) {
        this.prerequisiteTasksIds = prerequisiteTasksIds;
    }

    public Set<String> getSuccessorTasksIds() {
        return successorTasksIds;
    }

    public void setSuccessorTasksIds(Set<String> successorTasksIds) {
        this.successorTasksIds = successorTasksIds;
    }
}
