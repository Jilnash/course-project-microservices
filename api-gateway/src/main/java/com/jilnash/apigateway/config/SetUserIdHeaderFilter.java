package com.jilnash.apigateway.config;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.servlet.function.ServerRequest;

import java.util.function.Function;

public class SetUserIdHeaderFilter {

    public static Function<ServerRequest, ServerRequest> setIdHeader() {
        return request -> ServerRequest.from(request).header("X-User-Sub", getIdFromToken()).build();
    }

    private static String getIdFromToken() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof Jwt jwt)
            return jwt.getClaimAsString("sub");
        return null;
    }
}
