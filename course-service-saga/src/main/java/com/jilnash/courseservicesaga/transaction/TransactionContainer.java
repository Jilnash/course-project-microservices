package com.jilnash.courseservicesaga.transaction;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
@Scope("singleton")
public class TransactionContainer {

    @Bean
    public Set<String> transactionIds() {
        return new HashSet<>();
    }
}
