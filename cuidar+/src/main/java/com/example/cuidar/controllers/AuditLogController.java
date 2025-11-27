package com.example.cuidar.controllers;

import com.example.cuidar.dtos.log.LogAuditoriaResponseDto;
import com.example.cuidar.services.AuditLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort; // <-- IMPORTAR ISTO
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/log"  )
@PreAuthorize("hasRole('ADMIN')")
public class AuditLogController {

    private final AuditLogService auditLogService;

    @Autowired
    public AuditLogController(AuditLogService auditLogService) {
        this.auditLogService = auditLogService;
    }


    @GetMapping
    public ResponseEntity<Page<LogAuditoriaResponseDto>> findAll(
            @RequestParam(required = false) String entidade,
            @RequestParam(required = false) String tipoAcao,
            // AJUSTE AQUI: O 'sort' e 'direction' foram separados
            @PageableDefault(size = 20, sort = "dataHoraAcao", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<LogAuditoriaResponseDto> logs =
                auditLogService.findAll(entidade, tipoAcao, pageable);

        return ResponseEntity.ok(logs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LogAuditoriaResponseDto> findById(@PathVariable Long id) {
        LogAuditoriaResponseDto log = auditLogService.findById(id);
        return ResponseEntity.ok(log);
    }
}
