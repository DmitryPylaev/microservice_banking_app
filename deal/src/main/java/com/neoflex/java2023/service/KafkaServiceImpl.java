package com.neoflex.java2023.service;

import com.neoflex.java2023.dto.EmailMessage;
import com.neoflex.java2023.enums.EmailMessageTheme;
import com.neoflex.java2023.model.Application;
import com.neoflex.java2023.service.abstraction.KafkaService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@AllArgsConstructor
@Log4j2
public class KafkaServiceImpl implements KafkaService {
    private KafkaTemplate<String, EmailMessage> kafkaTemplate;

    @Override
    public void generateEmail(EmailMessageTheme emailMessageTheme, Application application) {
        EmailMessage emailMessage = EmailMessage.builder()
                .address(application.getClient().getEmail())
                .emailMessageTheme(emailMessageTheme)
                .applicationId(application.getId())
                .build();

        sendEmail(emailMessageTheme.toString(), emailMessage);
    }

    private void sendEmail(String topicName, EmailMessage emailMessage) {
        CompletableFuture<SendResult<String, EmailMessage>> future = kafkaTemplate.send(topicName, emailMessage);
        future.whenComplete((result, ex) -> {
            if (ex == null) {
                log.info("Sent message=[" + emailMessage +
                        "] with offset=[" + result.getRecordMetadata().offset() + "]");
            } else {
                log.info("Unable to send message=[" +
                        emailMessage + "] due to : " + ex.getMessage());
            }
        });
    }
}
