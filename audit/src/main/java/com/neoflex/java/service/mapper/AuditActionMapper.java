package com.neoflex.java.service.mapper;

import com.neoflex.java.dto.AuditDTO;
import com.neoflex.java.model.AuditAction;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuditActionMapper {
    AuditAction mapAuditAction(AuditDTO auditDTO);
}
