package com.neoflex.java.repository;

import com.neoflex.java.model.AuditAction;
import org.springframework.data.keyvalue.repository.KeyValueRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditActionRepository extends KeyValueRepository<AuditAction, String> {
}
