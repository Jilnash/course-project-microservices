package com.jilnash.apigateway.config;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.servlet.function.ServerRequest;

import java.util.function.Function;

public class SetEmailHeaderFilter {

    public static Function<ServerRequest, ServerRequest> setEmail() {
        return request -> ServerRequest.from(request).header("X-User-Email", getEmailFromToken()).build();
    }

    private static String getEmailFromToken() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof Jwt jwt)
            return jwt.getClaimAsString("email");
        return null;
    }
}
