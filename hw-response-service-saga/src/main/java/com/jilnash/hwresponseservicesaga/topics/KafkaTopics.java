package com.jilnash.hwresponseservicesaga.topics;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaTopics {

    @Bean
    public NewTopic createResponseTopic() {
        return new NewTopic("hw-response-create-topic", 1, (short) 1);
    }

    @Bean
    public NewTopic updateResponseTopic() {
        return new NewTopic("hw-response-update-topic", 1, (short) 1);
    }

    @Bean
    public NewTopic softDeleteResponseTopic() {
        return new NewTopic("hw-response-soft-delete-topic", 1, (short) 1);
    }

    @Bean
    public NewTopic hardDeleteResponseTopic() {
        return new NewTopic("hw-response-hard-delete-topic", 1, (short) 1);
    }
}
