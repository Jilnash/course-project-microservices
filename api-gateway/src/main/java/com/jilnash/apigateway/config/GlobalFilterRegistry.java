package com.jilnash.apigateway.config;

import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GlobalFilterRegistry {

    @Bean
    public GlobalFilter setUserIdHeaderFilter() {
        return new SetUserIdHeaderFilter();
    }
}
