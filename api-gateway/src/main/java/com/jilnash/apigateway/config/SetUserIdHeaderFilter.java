package com.jilnash.apigateway.config;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.UUID;

public class SetUserIdHeaderFilter implements GlobalFilter, Ordered {

    private static Mono<String> getIdFromTokenReactive() {
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .filter(auth -> auth.getPrincipal() instanceof Jwt)
                .map(auth -> (Jwt) auth.getPrincipal())
                .map(jwt -> jwt.getClaimAsString("sub"));
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return getIdFromTokenReactive()
                .defaultIfEmpty("anonymous") // Optional fallback
                .flatMap(userId -> {

                    HttpHeaders mutableHeaders = new HttpHeaders();
                    mutableHeaders.putAll(exchange.getRequest().getHeaders());
                    mutableHeaders.put("X-User-Sub", Collections.singletonList(userId));
                    mutableHeaders.put("X-Transaction-Id", Collections.singletonList(UUID.randomUUID().toString()));

                    // Mutate the request
                    ServerHttpRequest mutatedRequest = exchange.getRequest()
                            .mutate()
                            .headers(httpHeaders -> httpHeaders.putAll(mutableHeaders))
                            .build();

                    // Mutate the exchange
                    ServerWebExchange mutatedExchange = exchange.mutate()
                            .request(mutatedRequest)
                            .build();

                    return chain.filter(mutatedExchange);
                });
    }

    @Override
    public int getOrder() {

        return SecurityWebFiltersOrder.AUTHENTICATION.getOrder() + 1;
    }
}