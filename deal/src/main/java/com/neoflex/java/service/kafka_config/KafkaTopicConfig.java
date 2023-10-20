package com.neoflex.java.service.kafka_config;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

@Configuration
@SuppressWarnings("unused")
public class KafkaTopicConfig {

    @Value(value = "${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic finishRegistration() {
        return new NewTopic("FINISH_REGISTRATION", 1, (short) 1);
    }

    @Bean
    public NewTopic createDocuments() {
        return new NewTopic("CREATE_DOCUMENTS", 1, (short) 1);
    }

    @Bean
    public NewTopic sendDocuments() {
        return new NewTopic("SEND_DOCUMENTS", 1, (short) 1);
    }

    @Bean
    public NewTopic sendSes() {
        return new NewTopic("SEND_SES", 1, (short) 1);
    }

    @Bean
    public NewTopic creditIssued() {
        return new NewTopic("CREDIT_ISSUED", 1, (short) 1);
    }

    @Bean
    public NewTopic applicationDenied() {
        return new NewTopic("APPLICATION_DENIED", 1, (short) 1);
    }
}
