package com.jilnash.hwservicesaga.transaction;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Scope("singleton")
public class TransactionContainer {

    @Bean
    public Map<String, Transaction> transactionMap() {
        return new HashMap<>();
    }
}
