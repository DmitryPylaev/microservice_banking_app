package com.neoflex.java.service;

import com.neoflex.java.dto.AuditDTO;
import com.neoflex.java.model.AuditAction;
import com.neoflex.java.repository.AuditActionRepository;
import com.neoflex.java.service.abstraction.AuditActionService;
import com.neoflex.java.service.mapper.AuditActionMapper;
import com.neoflex.java.util.CustomLogger;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AuditActionServiceImpl implements AuditActionService {
    private static final int PAGE_SIZE = 1000;

    private final AuditActionRepository auditActionRepository;
    private final AuditActionMapper auditActionMapper;

    @Override
    public void saveAuditAction(AuditDTO auditDTO) {
        CustomLogger.logInfoClassAndMethod();
        auditActionRepository.save(auditActionMapper.mapAuditAction(auditDTO));
    }

    @Override
    public List<AuditAction> findPageAuditAction() {
        CustomLogger.logInfoClassAndMethod();
        return auditActionRepository.findAll(PageRequest.of(0, PAGE_SIZE)).getContent();
    }

    @Override
    public AuditAction getById(UUID uuid) {
        CustomLogger.logInfoClassAndMethod();
        return auditActionRepository.findById(uuid.toString()).orElse(AuditAction.builder().build());
    }
}
