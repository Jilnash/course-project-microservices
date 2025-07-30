package com.jilnash.courserightsservice.topics;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Topics {

    @Bean
    public NewTopic checkRightsTopic() {
        return new NewTopic("check-course-rights-topic", 1, (short) 1);
    }

    @Bean
    public NewTopic setRightsTopic() {
        return new NewTopic("set-course-rights-topic", 1, (short) 1);
    }

    @Bean
    public NewTopic createOwnerTopic() {
        return new NewTopic("create-course-owner-topic", 1, (short) 1);
    }
}
