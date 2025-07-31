package com.jilnash.courseaccessservice.topics;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaTopics {

    @Bean
    public NewTopic studentCourseAccessTopic() {
        return new NewTopic("check-course-access-topic", 1, (short) 1);
    }
}
