package com.jilnash.hwservicesaga.transaction;

import lombok.Getter;

@Getter
public class RollbackStage {

    private final String topic;
    private final Object payload;

    public RollbackStage(String topic, Object payload) {
        this.topic = topic;
        this.payload = payload;
    }
}
