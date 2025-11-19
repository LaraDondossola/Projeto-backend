package com.example.cuidar.services;

import com.example.cuidar.dtos.servico.ServicoCreateDto;
import com.example.cuidar.dtos.servico.ServicoResponseDto;
import com.example.cuidar.dtos.servico.ServicoUpdateDto;
import com.example.cuidar.models.Servico;
import com.example.cuidar.repositories.ServicoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ServicoService {

    private static final String ENTITY_NAME = "Servico";

    @Autowired
    private ServicoRepository servicoRepository;

    @Autowired
    private AuditLogService auditLogService;


    @PreAuthorize("hasRole('ADMIN')")
    public ServicoResponseDto create(ServicoCreateDto dto) {
        Servico servico = new Servico();
        servico.setNome(dto.nome());
        servico.setDescricao(dto.descricao());
        servico.setDuracaoEmMinutos(dto.duracaoEmMinutos());
        servico.setPreco(dto.preco());
        servico.setAtivo(true);

        Servico savedServico = servicoRepository.save(servico);

        auditLogService.registrarLog(
                "CRIACAO_SERVICO",
                ENTITY_NAME,
                savedServico.getId(),
                "Novo serviço cadastrado: " + savedServico.getNome()
        );

        return toResponseDto(savedServico);
    }


    @PreAuthorize("permitAll()")
    public Page<ServicoResponseDto> findAll(Pageable pageable) {
        return servicoRepository.findAll(pageable).map(this::toResponseDto);
    }


    @PreAuthorize("isAuthenticated()")
    public ServicoResponseDto findById(Long id) {
        Servico servico = findModelById(id);
        return toResponseDto(servico);
    }

    public Servico findModelById(Long id) {
        return servicoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Serviço com ID " + id + " não encontrado."));
    }


    @PreAuthorize("hasRole('ADMIN')")
    public ServicoResponseDto update(Long id, ServicoUpdateDto dto) {
        Servico servico = findModelById(id);

        Optional.ofNullable(dto.nome()).ifPresent(servico::setNome);
        Optional.ofNullable(dto.descricao()).ifPresent(servico::setDescricao);
        Optional.ofNullable(dto.duracaoEmMinutos()).ifPresent(servico::setDuracaoEmMinutos);
        Optional.ofNullable(dto.preco()).ifPresent(servico::setPreco);
        Optional.ofNullable(dto.ativo()).ifPresent(servico::setAtivo);

        Servico updatedServico = servicoRepository.save(servico);

        auditLogService.registrarLog(
                "ATUALIZACAO_SERVICO",
                ENTITY_NAME,
                updatedServico.getId(),
                "Serviço atualizado: " + updatedServico.getNome()
        );

        return toResponseDto(updatedServico);
    }


    @PreAuthorize("hasRole('ADMIN')")
    public void delete(Long id) {
        Servico servico = findModelById(id);

        if (servico.getAtivo()) {
            servico.setAtivo(false);
            servicoRepository.save(servico);

            auditLogService.registrarLog(
                    "DESATIVACAO_SERVICO",
                    ENTITY_NAME,
                    id,
                    "Serviço desativado logicamente: " + servico.getNome()
            );
        }
    }

    private ServicoResponseDto toResponseDto(Servico servico) {
        return new ServicoResponseDto(
                servico.getId(),
                servico.getNome(),
                servico.getDescricao(),
                servico.getDuracaoEmMinutos(),
                servico.getPreco(),
                servico.getAtivo(),
                servico.getDataCriacao(),
                servico.getDataAtualizacao()
        );
    }
}