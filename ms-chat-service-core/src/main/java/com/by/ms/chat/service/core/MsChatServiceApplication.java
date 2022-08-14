package com.by.ms.chat.service.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.cyperspace.b.user.apis")
@ComponentScan(basePackages = "com.by.ms.chat.service")
public class MsChatServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsChatServiceApplication.class, args);
    }

}
