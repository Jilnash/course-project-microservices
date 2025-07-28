package com.jilnash.courseservicesaga.topics;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModuleTopics {

    @Bean
    public NewTopic moduleCreateTopic() {
        return new NewTopic("module-create-topic", 1, (short) 1);
    }

    @Bean
    public NewTopic moduleUpdateNameTopic() {
        return new NewTopic("module-update-name-topic", 1, (short) 1);
    }

    @Bean
    public NewTopic moduleUpdateDescriptionTopic() {
        return new NewTopic("module-update-description-topic", 1, (short) 1);
    }

    @Bean
    public NewTopic moduleSoftDeleteTopic() {
        return new NewTopic("module-soft-delete-topic", 1, (short) 1);
    }

    @Bean
    public NewTopic moduleHardDeleteTopic() {
        return new NewTopic("module-hard-delete-topic", 1, (short) 1);
    }

    @Bean
    public NewTopic moduleCreateRollbackTopic() {
        return new NewTopic("module-create-rollback-topic", 1, (short) 1);
    }

    @Bean
    public NewTopic moduleUpdateNameRollbackTopic() {
        return new NewTopic("module-update-name-rollback-topic", 1, (short) 1);
    }

    @Bean
    public NewTopic moduleUpdateDescriptionRollbackTopic() {
        return new NewTopic("module-update-description-rollback-topic", 1, (short) 1);
    }

    @Bean
    public NewTopic moduleSoftDeleteRollbackTopic() {
        return new NewTopic("module-soft-delete-rollback-topic", 1, (short) 1);
    }
}
