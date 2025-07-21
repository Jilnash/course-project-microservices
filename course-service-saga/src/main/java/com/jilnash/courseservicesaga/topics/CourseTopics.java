package com.jilnash.courseservicesaga.topics;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class CourseTopics {

    @Bean
    public NewTopic courseCreateTopic() {
        return TopicBuilder
                .name("course-created-topic")
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic courseUpdateNameTopic() {
        return TopicBuilder
                .name("course-update-name-topic")
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic courseUpdateDescriptionTopic() {
        return TopicBuilder
                .name("course-update-description-topic")
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic courseUpdateDurationTopic() {
        return TopicBuilder
                .name("course-update-duration-topic")
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic courseSoftDeleteTopic() {
        return TopicBuilder
                .name("course-soft-delete-topic")
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic courseHardDeleteTopic() {
        return TopicBuilder
                .name("course-hard-delete-topic")
                .partitions(1)
                .replicas(1)
                .build();
    }
}
