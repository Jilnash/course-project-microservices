package com.jilnash.courseservicesaga;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
        "com.jilnash.*"
})
public class CourseServiceSagaApplication {

    public static void main(String[] args) {
        SpringApplication.run(CourseServiceSagaApplication.class, args);
    }

}
