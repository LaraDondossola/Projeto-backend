package com.example.cuidar.services;

import com.example.cuidar.dtos.log.LogAuditoriaResponseDto;
import com.example.cuidar.models.LogAuditoria;
import com.example.cuidar.repositories.LogAuditoriaRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
    public void registrarLog(String tipoAcao, String entidadeAfetada, Long entidadeId, String detalhes) {
        LogAuditoria log = new LogAuditoria();
        log.setDataHoraAcao(LocalDateTime.now());
        log.setTipoAcao(tipoAcao);
        log.setEntidadeAfetada(entidadeAfetada);
        log.setEntidadeId(entidadeId);
        log.setDetalhes(detalhes);

        log.setUsuarioResponsavel(getUsuarioLogado());

        logAuditoriaRepository.save(log);
    }

    private String getUsuarioLogado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return "usuário não logado";
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof String && "anonymousUser".equals(principal)) {
            return "usuário não logado";
        }
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        }
        return principal.toString();
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
