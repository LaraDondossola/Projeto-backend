package com.example.cuidar.services;

import com.example.cuidar.dtos.log.LogAuditoriaResponseDto;
import com.example.cuidar.models.LogAuditoria;
import com.example.cuidar.repositories.LogAuditoriaRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class AuditLogService {

    private final LogAuditoriaRepository logAuditoriaRepository;

    public AuditLogService(LogAuditoriaRepository logAuditoriaRepository) {
        this.logAuditoriaRepository = logAuditoriaRepository;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void registrarLog(String tipoAcao, String entidadeAfetada, Long entidadeId, String detalhes, String usuarioResponsavel) {
        LogAuditoria log = new LogAuditoria();
        log.setDataHoraAcao(LocalDateTime.now());
        log.setTipoAcao(tipoAcao);
        log.setEntidadeAfetada(entidadeAfetada);
        log.setEntidadeId(entidadeId);
        log.setDetalhes(detalhes);

        log.setUsuarioResponsavel(usuarioResponsavel != null ? usuarioResponsavel : "Sistema/Anonimo");

        logAuditoriaRepository.save(log);
    }

    public Page<LogAuditoriaResponseDto> findAll(
            String entidade,
            String tipoAcao,
            Pageable pageable)
    {
        Specification<LogAuditoria> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (entidade != null && !entidade.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("entidadeAfetada"), entidade));
            }

            if (tipoAcao != null && !tipoAcao.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("tipoAcao"), tipoAcao));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        return logAuditoriaRepository.findAll(spec, pageable).map(this::toResponseDto);
    }

    public LogAuditoriaResponseDto findById(Long id) {
        LogAuditoria log = logAuditoriaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Log de Auditoria com ID " + id + " não encontrado."));
        return toResponseDto(log);
    }

    private LogAuditoriaResponseDto toResponseDto(LogAuditoria log) {
        return new LogAuditoriaResponseDto(
                log.getId(),
                log.getDataHoraAcao(),
                log.getTipoAcao(),
                log.getEntidadeAfetada(),
                log.getEntidadeId(),
                log.getDetalhes(),
                log.getUsuarioResponsavel()
        );
    }
}
