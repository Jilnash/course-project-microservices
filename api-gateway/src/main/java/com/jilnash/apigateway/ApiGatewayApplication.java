package com.jilnash.apigateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/")
@SpringBootApplication
public class ApiGatewayApplication {

    @Autowired
    private RouteDefinitionLocator routeDefinitionLocator;

    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }

    @GetMapping("/api/routes")
    public Flux<RouteDefinition> getRoutes() {
        return routeDefinitionLocator.getRouteDefinitions();
    }
}
