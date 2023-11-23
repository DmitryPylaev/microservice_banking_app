package com.neoflex.java.service.kafka_config;

import com.neoflex.java.dto.AuditDTO;
import com.neoflex.java.service.abstraction.AuditActionService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Log4j2
@SuppressWarnings("unused")
public class KafkaConsumer {
    private final AuditActionService auditActionService;

    @KafkaListener(topics = {"AUDIT"},
            groupId = "dossierGroup",
            containerFactory = "auditKafkaListenerContainerFactory")
    public void handleAuditEvents(AuditDTO auditDTO) {
        auditActionService.saveAuditAction(auditDTO);
        log.info("Audit save: " + auditDTO);
    }
}
