package com.jilnash.apigateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.HandlerFilterFunction;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import static com.jilnash.apigateway.config.SetEmailHeaderFilter.setEmail;
import static org.springframework.cloud.gateway.server.mvc.filter.FilterFunctions.rewritePath;
import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.http;
import static org.springframework.cloud.gateway.server.mvc.predicate.GatewayRequestPredicates.path;
import static org.springframework.web.servlet.function.RouterFunctions.route;

@Configuration
public class RouterConfig {

    @Bean
    public RouterFunction<ServerResponse> userService() {
        return route(path("/user-service/**"), http("http://localhost:8082"))
                .filter(HandlerFilterFunction.ofRequestProcessor(setEmail()))
                .filter(rewritePath("/user-service/(?<remaining>.*)", "/${remaining}"));
    }

    @Bean
    public RouterFunction<ServerResponse> adminService() {
        return route(path("/admin-service/**"), http("http://localhost:8085"))
                .filter(HandlerFilterFunction.ofRequestProcessor(setEmail()))
                .filter(rewritePath("/admin-service/(?<remaining>.*)", "/${remaining}"));
    }

    @Bean
    public RouterFunction<ServerResponse> teacherService() {
        return route(path("/teacher-service/**"), http("http://localhost:8084"))
                .filter(HandlerFilterFunction.ofRequestProcessor(setEmail()))
                .filter(rewritePath("/teacher-service/(?<remaining>.*)", "/${remaining}"));
    }

    @Bean
    public RouterFunction<ServerResponse> studentService() {
        return route(path("/student-service/**"), http("http://localhost:8083"))
                .filter(HandlerFilterFunction.ofRequestProcessor(setEmail()))
                .filter(rewritePath("/student-service/(?<remaining>.*)", "/${remaining}"));
    }

    @Bean
    public RouterFunction<ServerResponse> courseService() {
        return route(path("/course-service/**"), http("http://localhost:8086"))
                .filter(HandlerFilterFunction.ofRequestProcessor(setEmail()))
                .filter(rewritePath("/course-service/(?<remaining>.*)", "/${remaining}"));
    }

    @Bean
    public RouterFunction<ServerResponse> fileService() {
        return route(path("/file-service/**"), http("http://localhost:8087"))
                .filter(HandlerFilterFunction.ofRequestProcessor(setEmail()))
                .filter(rewritePath("/file-service/(?<remaining>.*)", "/${remaining}"));
    }

    @Bean
    public RouterFunction<ServerResponse> hwService() {
        return route(path("/hw-service/**"), http("http://localhost:8088"))
                .filter(HandlerFilterFunction.ofRequestProcessor(setEmail()))
                .filter(rewritePath("/hw-service/(?<remaining>.*)", "/${remaining}"));
    }

    @Bean
    public RouterFunction<ServerResponse> responseService() {
        return route(path("/response-service/**"), http("http://localhost:8089"))
                .filter(HandlerFilterFunction.ofRequestProcessor(setEmail()))
                .filter(rewritePath("/response-service/(?<remaining>.*)", "/${remaining}"));
    }

    @Bean
    public RouterFunction<ServerResponse> courseAccessService() {
        return route(path("/course-access-service/**"), http("http://localhost:8090"))
                .filter(HandlerFilterFunction.ofRequestProcessor(setEmail()))
                .filter(rewritePath("/course-access-service/(?<remaining>.*)", "/${remaining}"));
    }

    @Bean
    public RouterFunction<ServerResponse> courseRightsService() {
        return route(path("/course-rights-service/**"), http("http://localhost:8091"))
                .filter(HandlerFilterFunction.ofRequestProcessor(setEmail()))
                .filter(rewritePath("/course-rights-service/(?<remaining>.*)", "/${remaining}"));
    }

    @Bean
    public RouterFunction<ServerResponse> progressService() {
        return route(path("/progress-service/**"), http("http://localhost:8092"))
                .filter(HandlerFilterFunction.ofRequestProcessor(setEmail()))
                .filter(rewritePath("/progress-service/(?<remaining>.*)", "/${remaining}"));
    }
}
