package com.neoflex.java.app;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan("com.neoflex.java")
@EnableJpaRepositories("com.neoflex.java.repository")
@EntityScan(basePackages = "com.neoflex.java.model")
@EnableFeignClients(basePackages = "com.neoflex.java.service")
@SuppressWarnings("unused")
public class DealApplication {
    public static void main(String[] args) {
        ApplicationContext context = new SpringApplicationBuilder(DealApplication.class).headless(false).run(args);
    }
}
