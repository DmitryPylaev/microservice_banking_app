package com.neoflex.java.app;

import com.neoflex.java.service.properties.ScoringProperties;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.neoflex.java")
@EnableConfigurationProperties(ScoringProperties.class)
@SuppressWarnings("unused")
public class ConveyorApplication {
    public static void main(String[] args) {
        ApplicationContext context = new SpringApplicationBuilder(ConveyorApplication.class).headless(false).run(args);
    }
}
