package com.neoflex.java.controller;

import com.neoflex.java.model.AuditAction;
import com.neoflex.java.service.abstraction.AuditActionService;
import com.neoflex.java.util.CustomLogger;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Контроллер микросервиса аудита
 */
@RestController
@RequestMapping("/audit")
@AllArgsConstructor
@SuppressWarnings("unused")
@Tag(name = "AuditController", description = "Контроллер аудита")
public class AuditController {
    private AuditActionService auditActionService;

    @Operation(summary = "Получить событие аудита по uuid")
    @PostMapping(value = "/get/{uuid}", produces = MediaType.APPLICATION_JSON_VALUE)
    public AuditAction getAuditAction(@RequestParam UUID uuid) {
        CustomLogger.logInfoClassAndMethod();
        CustomLogger.logInfoRequest(uuid);
        return auditActionService.getById(uuid);
    }

    @Operation(summary = "Получить все события аудита по странично")
    @PostMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<AuditAction> getAllAuditActions() {
        CustomLogger.logInfoClassAndMethod();
        return auditActionService.findPageAuditAction();
    }
}
