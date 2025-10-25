package com.example.cuidar.repositories;

import com.example.cuidar.models.LogAuditoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor; // <-- NOVO IMPORT

public interface LogAuditoriaRepository extends
        JpaRepository<LogAuditoria, Long>,
        JpaSpecificationExecutor<LogAuditoria> {

}