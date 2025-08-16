package com.jilnash.courseservice.history;

import java.util.Date;

public record EntityValue(Date createdAt, Object value) {

    public EntityValue(Object value) {
        this(new Date(), value);
    }

    public EntityValue(Date createdAt, Object value) {
        this.createdAt = createdAt;
        this.value = value;
    }
}
