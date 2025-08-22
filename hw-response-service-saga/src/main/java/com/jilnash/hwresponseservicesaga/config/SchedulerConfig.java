package com.jilnash.hwresponseservicesaga.config;

import com.jilnash.hwresponseservicesaga.transaction.Transaction;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableScheduling
public class SchedulerConfig {

    private final Map<String, Transaction> transactionMap;

    public SchedulerConfig(Map<String, Transaction> transactionMap) {
        this.transactionMap = transactionMap;
    }

    @Scheduled(fixedDelay = 60, timeUnit = TimeUnit.SECONDS)
    public void cleanTransactionMap() {

        if (transactionMap.isEmpty()) return;

        for (Map.Entry<String, Transaction> entry : transactionMap.entrySet())
            if (new Date().getTime() - entry.getValue().getCreatedAt().getTime() > 60_000)
                transactionMap.remove(entry.getKey());
    }
}
