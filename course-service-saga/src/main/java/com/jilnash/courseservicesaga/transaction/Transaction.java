package com.jilnash.courseservicesaga.transaction;

import lombok.Getter;

import java.util.Date;
import java.util.List;

@Getter
public class Transaction {

    private final String transactionId;

    private final Date createdAt;

    private final List<RollbackStage> rollbackStages;

    public Transaction(String transactionId, List<RollbackStage> rollbackStages) {
        this.transactionId = transactionId;
        this.createdAt = new Date();
        this.rollbackStages = rollbackStages;
    }
}
