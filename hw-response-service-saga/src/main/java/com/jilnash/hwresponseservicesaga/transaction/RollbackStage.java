package com.jilnash.hwresponseservicesaga.transaction;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RollbackStage {

    private String topic;
    private Object payload;

    public RollbackStage(String topic, Object payload) {
        this.topic = topic;
        this.payload = payload;
    }
}
