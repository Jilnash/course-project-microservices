package com.jilnash.hwservicesaga.topics;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaTopics {

    @Bean
    public NewTopic homeworkCreateTopic() {
        return new NewTopic("homework-create-topic", 1, (short) 1);
    }

    @Bean
    public NewTopic homeworkCheckedTopic() {
        return new NewTopic("homework-checked-topic", 1, (short) 1);
    }

    @Bean
    public NewTopic homeworkSoftDeleteTopic() {
        return new NewTopic("homework-soft-delete-topic", 1, (short) 1);
    }

    @Bean
    public NewTopic homeworkHardDeleteTopic() {
        return new NewTopic("homework-hard-delete-topic", 1, (short) 1);
    }
}
