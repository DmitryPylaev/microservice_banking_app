package com.neoflex.java.service.abstraction;

import com.neoflex.java.dto.AuditDTO;
import com.neoflex.java.model.AuditAction;

import java.util.List;
import java.util.UUID;

public interface AuditActionService {
    void saveAuditAction(AuditDTO auditDTO);

    List<AuditAction> findPageAuditAction();

    AuditAction getById(UUID uuid);
}
