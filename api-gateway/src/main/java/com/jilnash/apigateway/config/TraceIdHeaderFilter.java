package com.jilnash.apigateway.config;

import org.springframework.web.servlet.function.ServerRequest;

import java.util.UUID;
import java.util.function.Function;

public class TraceIdHeaderFilter {

    public static Function<ServerRequest, ServerRequest> setTraceIdHeader() {
        return request -> ServerRequest.from(request)
                .header("X-Trace-Id", UUID.randomUUID().toString())
                .build();
    }
}
