package com.jilnash.hwresponseservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class HwResponseServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(HwResponseServiceApplication.class, args);
    }

}
