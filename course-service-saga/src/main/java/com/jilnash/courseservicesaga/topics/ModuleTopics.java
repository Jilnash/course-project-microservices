package com.jilnash.courseservicesaga.topics;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class ModuleTopics {

    @Bean
    public NewTopic moduleCreateTopic() {
        return TopicBuilder.name("module-create-topic")
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic moduleUpdateNameTopic() {
        return TopicBuilder.name("module-update-name-topic")
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic moduleUpdateDescriptionTopic() {
        return TopicBuilder.name("module-update-description-topic")
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic moduleSoftDeleteTopic() {
        return TopicBuilder.name("module-soft-delete-topic")
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic moduleHardDeleteTopic() {
        return TopicBuilder.name("module-hard-delete-topic")
                .partitions(1)
                .replicas(1)
                .build();
    }
}
