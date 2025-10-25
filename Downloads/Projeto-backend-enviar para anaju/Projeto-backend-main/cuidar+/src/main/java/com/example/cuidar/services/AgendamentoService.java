package com.example.cuidar.services;

import com.example.cuidar.dtos.agendamento.AgendamentoCreateDto;
import com.example.cuidar.dtos.agendamento.AgendamentoResponseDto;
import com.example.cuidar.dtos.agendamento.AgendamentoUpdateDto;
import com.example.cuidar.models.Agendamento;
import com.example.cuidar.models.Cliente;
import com.example.cuidar.models.Profissional;
import com.example.cuidar.models.Servico;
import com.example.cuidar.repositories.AgendamentoRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AgendamentoService {

    private static final String ENTITY_NAME = "Agendamento";

    private final AgendamentoRepository agendamentoRepository;
    private final ClienteService clienteService;
    private final ProfissionalService profissionalService;
    private final ServicoService servicoService;
    private final AuditLogService auditLogService;

    public AgendamentoService(
            AgendamentoRepository agendamentoRepository,
            ClienteService clienteService,
            ProfissionalService profissionalService,
            ServicoService servicoService,
            AuditLogService auditLogService) {
        this.agendamentoRepository = agendamentoRepository;
        this.clienteService = clienteService;
        this.profissionalService = profissionalService;
        this.servicoService = servicoService;
        this.auditLogService = auditLogService;
    }


    @PreAuthorize("hasAnyRole('CLIENTE', 'ADMIN')")
    public AgendamentoResponseDto create(AgendamentoCreateDto dto) {
        Cliente cliente = clienteService.findModelById(dto.clienteId());
        Profissional profissional = profissionalService.findModelById(dto.profissionalId());
        Servico servico = servicoService.findModelById(dto.servicoId());

        LocalDateTime dataHoraFim = dto.dataHoraInicio().plusMinutes(servico.getDuracaoEmMinutos());

        Agendamento agendamento = new Agendamento();
        agendamento.setCliente(cliente);
        agendamento.setProfissional(profissional);
        agendamento.setServico(servico);
        agendamento.setDataHoraInicio(dto.dataHoraInicio());
        agendamento.setDataHoraFim(dataHoraFim);
        agendamento.setStatus("PENDENTE");

        Agendamento savedAgendamento = agendamentoRepository.save(agendamento);

        auditLogService.registrarLog(
                "CRIACAO_AGENDAMENTO",
                ENTITY_NAME,
                savedAgendamento.getId(),
                String.format("Novo agendamento criado para o cliente %s com o profissional %s.",
                        cliente.getNome(), profissional.getNome()),
                cliente.getNome()
        );

        return toResponseDto(savedAgendamento);
    }

    public Page<AgendamentoResponseDto> findAll(
            Long profissionalId,
            String status,
            LocalDateTime data,
            Pageable pageable)
    {
        Specification<Agendamento> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (profissionalId != null) {
                predicates.add(criteriaBuilder.equal(root.get("profissional").get("id"), profissionalId));
            }

            if (status != null && !status.isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("status"), status));
            }

            if (data != null) {
                LocalDateTime startOfDay = data.toLocalDate().atStartOfDay();
                LocalDateTime endOfDay = data.toLocalDate().atTime(23, 59, 59);

                predicates.add(criteriaBuilder.between(root.get("dataHoraInicio"), startOfDay, endOfDay));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        Page<Agendamento> agendamentoPage = agendamentoRepository.findAll(spec, pageable);
        return agendamentoPage.map(this::toResponseDto);
    }


    @PreAuthorize("hasRole('ADMIN') or (hasRole('CLIENTE') and @agendamentoService.isAgendamentoOwner(#id, authentication.principal.id)) or (hasRole('PROFISSIONAL') and @agendamentoService.isAgendamentoForProfessional(#id, authentication.principal.id))")
    public AgendamentoResponseDto findById(Long id) {
        Agendamento agendamento = findModelById(id);
        return toResponseDto(agendamento);
    }

    public Agendamento findModelById(Long id) {
        return agendamentoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Agendamento com ID " + id + " não encontrado."));
    }


    @PreAuthorize("hasRole('ADMIN') or (hasRole('PROFISSIONAL') and @agendamentoService.isAgendamentoForProfessional(#id, authentication.principal.id))")
    public AgendamentoResponseDto update(Long id, AgendamentoUpdateDto dto) {
        Agendamento agendamento = findModelById(id);
        String oldStatus = agendamento.getStatus();

        Optional.ofNullable(dto.status()).ifPresent(agendamento::setStatus);

        if (dto.dataHoraInicio() != null) {
            agendamento.setDataHoraInicio(dto.dataHoraInicio());

            Servico servico = agendamento.getServico();
            agendamento.setDataHoraFim(dto.dataHoraInicio().plusMinutes(servico.getDuracaoEmMinutos()));
        }

        Agendamento updatedAgendamento = agendamentoRepository.save(agendamento);

        String newStatus = updatedAgendamento.getStatus();
        String detalhes = "Agendamento atualizado. Status mudou de " + oldStatus + " para " + newStatus + ".";

        auditLogService.registrarLog(
                "ATUALIZACAO_AGENDAMENTO",
                ENTITY_NAME,
                updatedAgendamento.getId(),
                detalhes,
                updatedAgendamento.getCliente().getNome()
        );

        return toResponseDto(updatedAgendamento);
    }


    @PreAuthorize("hasRole('ADMIN') or (hasRole('CLIENTE') and @agendamentoService.isAgendamentoOwner(#id, authentication.principal.id))")
    public void delete(Long id) {
        Agendamento agendamento = findModelById(id);

        if (!"CANCELADO".equals(agendamento.getStatus())) {
            agendamento.setStatus("CANCELADO");
            agendamentoRepository.save(agendamento);

            auditLogService.registrarLog(
                    "CANCELAMENTO_AGENDAMENTO",
                    ENTITY_NAME,
                    id,
                    "Agendamento cancelado.",
                    agendamento.getCliente().getNome()
            );
        }
    }

    // Métodos auxiliares para @PreAuthorize
    public boolean isAgendamentoOwner(Long agendamentoId, Long userId) {
        Agendamento agendamento = findModelById(agendamentoId);
        return agendamento.getCliente().getId().equals(userId);
    }

    public boolean isAgendamentoForProfessional(Long agendamentoId, Long userId) {
        Agendamento agendamento = findModelById(agendamentoId);
        return agendamento.getProfissional().getId().equals(userId);
    }

    private AgendamentoResponseDto toResponseDto(Agendamento agendamento) {
        return new AgendamentoResponseDto(
                agendamento.getId(),
                agendamento.getCliente().getId(),
                agendamento.getCliente().getNome(),
                agendamento.getProfissional().getId(),
                agendamento.getProfissional().getNome(),
                agendamento.getServico().getId(),
                agendamento.getServico().getNome(),
                agendamento.getDataHoraInicio(),
                agendamento.getDataHoraFim(),
                agendamento.getStatus(),
                agendamento.getDataCriacao(),
                agendamento.getDataAtualizacao()
        );
    }
}

