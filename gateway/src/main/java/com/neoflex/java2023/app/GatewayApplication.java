package com.neoflex.java2023.app;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.neoflex.java2023")
@EnableFeignClients(basePackages = "com.neoflex.java2023.service")
@SuppressWarnings("unused")
public class GatewayApplication {
    public static void main(String[] args) {
        ApplicationContext context = new SpringApplicationBuilder(GatewayApplication.class).headless(false).run(args);
    }
}
