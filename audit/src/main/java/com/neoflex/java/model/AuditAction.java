package com.neoflex.java.model;

import com.neoflex.java.dto.AuditActionType;
import com.neoflex.java.dto.ServiceEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@RedisHash
public class AuditAction implements Serializable {
    @Id
    private UUID uuid;
    private AuditActionType auditActionType;
    private ServiceEnum service;
    private String message;
}
