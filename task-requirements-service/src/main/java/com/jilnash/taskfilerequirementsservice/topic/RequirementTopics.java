package com.jilnash.taskfilerequirementsservice.topic;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RequirementTopics {

    @Bean
    public NewTopic setRequirementsTopic() {
        return new NewTopic("set-task-requirements-topic", 1, (short) 1);
    }
}
