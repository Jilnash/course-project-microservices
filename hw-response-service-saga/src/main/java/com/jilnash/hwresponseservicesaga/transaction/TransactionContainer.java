package com.jilnash.hwresponseservicesaga.transaction;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class TransactionContainer {

    @Bean
    public Map<String, Transaction> transactionMap() {
        return new HashMap<>();
    }
}
