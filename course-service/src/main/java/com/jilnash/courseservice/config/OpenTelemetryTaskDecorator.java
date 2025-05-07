package com.jilnash.courseservice.config;

import io.opentelemetry.context.Context;
import io.opentelemetry.context.Scope;
import org.springframework.core.task.TaskDecorator;

public class OpenTelemetryTaskDecorator implements TaskDecorator {
    @Override
    public Runnable decorate(Runnable runnable) {
        Context captured = Context.current(); // capture context before async
        return () -> {
            try (Scope scope = captured.makeCurrent()) {
                runnable.run(); // context is active here
            }
        };
    }
}

