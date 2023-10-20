package com.neoflex.java.service.kafka_config;

import com.neoflex.java.dto.EmailMessage;
import com.neoflex.java.service.mail.EmailSender;
import com.neoflex.java.util.CustomLogger;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Log4j2
@SuppressWarnings("unused")
public class KafkaConsumer {

    private EmailSender mailSender;
    private static final String[] ATTACHED_FILES = {"Кредитный договор.docx", "Анкета.docx", "График платежей.docx"};

    @KafkaListener(topics = {"FINISH_REGISTRATION",
            "CREATE_DOCUMENTS",
            "SEND_SES",
            "CREDIT_ISSUED",
            "APPLICATION_DENIED"},
            groupId = "dossierGroup",
            containerFactory = "emailKafkaListenerContainerFactory")
    public void handleEmail(EmailMessage emailMessage) throws MessagingException {
        CustomLogger.logInfoClassAndMethod();
        mailSender.send(emailMessage, new String[0]);
        log.info("Mail send: " + emailMessage);
    }

    @KafkaListener(topics = {"SEND_DOCUMENTS"},
            groupId = "dossierGroup",
            containerFactory = "emailKafkaListenerContainerFactory")
    public void handleEmailWithAttachments(EmailMessage emailMessage) throws MessagingException {
        CustomLogger.logInfoClassAndMethod();
        mailSender.send(emailMessage, ATTACHED_FILES);
        log.info("Mail send with attachments: " + emailMessage);
    }
}
