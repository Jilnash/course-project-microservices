package com.jilnash.progressservice.topics;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaTopics {

    @Bean
    public NewTopic insertTaskProgressTopic() {
        return new NewTopic("insert-task-progress-topic", 1, (short) 1);
    }

    @Bean
    public NewTopic softDeleteProgressTopic() {
        return new NewTopic("soft-delete-progress-topic", 1, (short) 1);
    }

    @Bean
    public NewTopic hardDeleteProgressTopic() {
        return new NewTopic("hard-delete-progress-topic", 1, (short) 1);
    }
}
