package com.jilnash.homeworkservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableFeignClients
@EnableAsync(proxyTargetClass = true)
@EnableCaching(proxyTargetClass = true)
@SpringBootApplication
public class HomeworkServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(HomeworkServiceApplication.class, args);
    }

}
