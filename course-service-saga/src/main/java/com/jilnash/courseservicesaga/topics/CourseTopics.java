package com.jilnash.courseservicesaga.topics;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CourseTopics {

    @Bean
    public NewTopic courseCreateTopic() {
        return new NewTopic("course-created-topic", 1, (short) 1);
    }

    @Bean
    public NewTopic courseUpdateNameTopic() {
        return new NewTopic("course-update-name-topic", 1, (short) 1);
    }

    @Bean
    public NewTopic courseUpdateDescriptionTopic() {
        return new NewTopic("course-update-description-topic", 1, (short) 1);
    }

    @Bean
    public NewTopic courseUpdateDurationTopic() {
        return new NewTopic("course-update-duration-topic", 1, (short) 1);
    }

    @Bean
    public NewTopic courseSoftDeleteTopic() {
        return new NewTopic("course-soft-delete-topic", 1, (short) 1);
    }

    @Bean
    public NewTopic courseHardDeleteTopic() {
        return new NewTopic("course-hard-delete-topic", 1, (short) 1);
    }

    @Bean
    public NewTopic courseCreateRollbackTopic() {
        return new NewTopic("course-create-rollback-topic", 1, (short) 1);
    }

    @Bean
    public NewTopic courseNameUpdateRollbackTopic() {
        return new NewTopic("course-name-update-rollback-topic", 1, (short) 1);
    }

    @Bean
    public NewTopic courseDescriptionUpdateRollbackTopic() {
        return new NewTopic("course-description-update-rollback-topic", 1, (short) 1);
    }

    @Bean
    public NewTopic courseDurationUpdateRollbackTopic() {
        return new NewTopic("course-duration-update-rollback-topic", 1, (short) 1);
    }

    @Bean
    public NewTopic courseSoftDeleteRollbackTopic() {
        return new NewTopic("course-soft-delete-rollback-topic", 1, (short) 1);
    }
}
