package com.jilnash.courseservice.history;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Scope("singleton")
public class EntityHistory {

    @Bean
    public Map<EntityKey, EntityValue> taskHistoryContainer() {
        return new HashMap<>();
    }
}
