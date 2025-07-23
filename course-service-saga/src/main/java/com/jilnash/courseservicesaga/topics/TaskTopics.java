package com.jilnash.courseservicesaga.topics;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TaskTopics {

    @Bean
    public NewTopic taskGetTasksTopic() {
        return new NewTopic("task-get-tasks-topic", 1, (short) 1);
    }

    @Bean
    public NewTopic taskGetTasksAsGraphTopic() {
        return new NewTopic("task-get-tasks-as-graph-topic", 1, (short) 1);
    }

    @Bean
    public NewTopic taskGetTaskByIdTopic() {
        return new NewTopic("task-get-task-by-id-topic", 1, (short) 1);
    }

    @Bean
    public NewTopic taskGetTaskByCourseModuleIdTopic() {
        return new NewTopic("task-get-task-by-course-module-id-topic", 1, (short) 1);
    }

    @Bean
    public NewTopic taskGetTitleTopic() {
        return new NewTopic("task-get-title-topic", 1, (short) 1);
    }

    @Bean
    public NewTopic taskGetDescriptionTopic() {
        return new NewTopic("task-get-description-topic", 1, (short) 1);
    }

    @Bean
    public NewTopic taskGetVideoUrlTopic() {
        return new NewTopic("task-get-video-url-topic", 1, (short) 1);
    }

    @Bean
    public NewTopic taskGetIsPublicTopic() {
        return new NewTopic("task-get-is-public-topic", 1, (short) 1);
    }

    @Bean
    public NewTopic taskGetPrerequisitesTopic() {
        return new NewTopic("task-get-prerequisites-topic", 1, (short) 1);
    }

    @Bean
    public NewTopic taskGetSuccessorsTopic() {
        return new NewTopic("task-get-successors-topic", 1, (short) 1);
    }


    @Bean
    public NewTopic taskCreateTopic() {
        return new NewTopic("task-create-topic", 1, (short) 1);
    }

    @Bean
    public NewTopic taskUpdateTitleTopic() {
        return new NewTopic("task-update-title-topic", 1, (short) 1);
    }

    @Bean
    public NewTopic taskUpdateDescriptionTopic() {
        return new NewTopic("task-update-description-topic", 1, (short) 1);
    }

    @Bean
    public NewTopic taskUpdateVideoFileTopic() {
        return new NewTopic("task-update-video-file-topic", 1, (short) 1);
    }

    @Bean
    public NewTopic taskUpdateIsPublicTopic() {
        return new NewTopic("task-update-is-public-topic", 1, (short) 1);
    }

    @Bean
    public NewTopic taskUpdateHwPostingIntervalTopic() {
        return new NewTopic("task-update-hw-posting-interval-topic", 1, (short) 1);
    }

    @Bean
    public NewTopic taskUpdatePrerequisitesTopic() {
        return new NewTopic("task-update-prerequisites-topic", 1, (short) 1);
    }

    @Bean
    public NewTopic taskUpdateSuccessorsTopic() {
        return new NewTopic("task-update-successors-topic", 1, (short) 1);
    }

    @Bean
    public NewTopic taskDeleteSoftTopic() {
        return new NewTopic("task-soft-delete-topic", 1, (short) 1);
    }

    @Bean
    public NewTopic taskDeleteHardTopic() {
        return new NewTopic("task-hard-delete-topic", 1, (short) 1);
    }
}