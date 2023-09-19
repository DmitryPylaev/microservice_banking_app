package com.neoflex.java.app;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.neoflex.java")
@EnableFeignClients(basePackages = "com.neoflex.java.service")
@SuppressWarnings("unused")
public class GatewayApplication {
    public static void main(String[] args) {
        ApplicationContext context = new SpringApplicationBuilder(GatewayApplication.class).headless(false).run(args);
    }
}
