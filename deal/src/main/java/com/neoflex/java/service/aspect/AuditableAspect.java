package com.neoflex.java.service.aspect;

import com.neoflex.java.dto.AuditActionType;
import com.neoflex.java.dto.AuditDTO;
import com.neoflex.java.service.abstraction.KafkaService;
import lombok.AllArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Aspect
@Component
@AllArgsConstructor
public class AuditableAspect {
    private static final String TOPIC_NAME = "AUDIT";
    private KafkaService kafkaService;

    @Around("@annotation(auditAction)")
    public Object auditBefore(ProceedingJoinPoint joinPoint, AuditAction auditAction) throws Throwable {
        AuditDTO auditDTO = AuditDTO.builder()
                .uuid(UUID.randomUUID())
                .auditActionType(AuditActionType.START)
                .service(auditAction.service())
                .message(sendAuditMessage(joinPoint.getArgs()[0]))
                .build();

        kafkaService.generateAuditAction(TOPIC_NAME, auditDTO);

        Object result = new Object();
        try {
            result = joinPoint.proceed();
            auditDTO.setAuditActionType(AuditActionType.SUCCESS);
            kafkaService.generateAuditAction(TOPIC_NAME, auditDTO);
        } catch (Exception e) {
            auditDTO.setAuditActionType(AuditActionType.FAILURE);
            kafkaService.generateAuditAction(TOPIC_NAME, auditDTO);
        }

        return result;
    }

    private String sendAuditMessage(Object request) {
        return "Create: " + LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS) + "; request = " + request.toString();
    }
}
