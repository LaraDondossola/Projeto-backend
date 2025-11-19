package com.example.cuidar.services;

import com.example.cuidar.dtos.profissional.ProfissionalCreateDto;
import com.example.cuidar.dtos.profissional.ProfissionalResponseDto;
import com.example.cuidar.dtos.profissional.ProfissionalUpdateDto;
import com.example.cuidar.models.Profissional;
import com.example.cuidar.models.Role;
import com.example.cuidar.repositories.ProfissionalRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProfissionalService {

    private static final String ENTITY_NAME = "Profissional";

    @Autowired
    private ProfissionalRepository profissionalRepository;

    @Autowired
    private AuditLogService auditLogService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PreAuthorize("hasRole('ADMIN')")
    public ProfissionalResponseDto create(ProfissionalCreateDto dto) {
        Profissional profissional = new Profissional();

        profissional.setNome(dto.nome());
        profissional.setEmail(dto.email());
        profissional.setTelefone(dto.telefone());
        profissional.setEspecialidade(dto.especialidade());

        profissional.setLogin(dto.email());
        profissional.setSenha(passwordEncoder.encode(dto.senha()));
        profissional.setRole(Role.PROFISSIONAL);

        profissional.setAtivo(true);

        Profissional savedProfissional = profissionalRepository.save(profissional);

        auditLogService.registrarLog(
                "CRIACAO_PROFISSIONAL",
                ENTITY_NAME,
                savedProfissional.getId(),
                "Novo profissional cadastrado."
        );

        return toResponseDto(savedProfissional);
    }


    @PreAuthorize("permitAll()")
    public Page<ProfissionalResponseDto> findAll(Pageable pageable) {
        Page<Profissional> profissionalPage = profissionalRepository.findAll(pageable);
        return profissionalPage.map(this::toResponseDto);
    }


    @PreAuthorize("hasRole('ADMIN') or (hasRole('PROFISSIONAL') and #id == authentication.principal.id)")
    public ProfissionalResponseDto findById(Long id) {
        Profissional profissional = findModelById(id);
        return toResponseDto(profissional);
    }

    public Profissional findModelById(Long id) {
        return profissionalRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Profissional com ID " + id + " não encontrado."));
    }


    @PreAuthorize("hasRole('ADMIN') or (hasRole('PROFISSIONAL') and #id == authentication.principal.id)")
    public ProfissionalResponseDto update(Long id, ProfissionalUpdateDto dto) {
        Profissional profissional = findModelById(id);

        Optional.ofNullable(dto.nome()).ifPresent(profissional::setNome);
        Optional.ofNullable(dto.email()).ifPresent(profissional::setEmail);
        Optional.ofNullable(dto.telefone()).ifPresent(profissional::setTelefone);
        Optional.ofNullable(dto.especialidade()).ifPresent(profissional::setEspecialidade);
        Optional.ofNullable(dto.ativo()).ifPresent(profissional::setAtivo);

        Profissional updatedProfissional = profissionalRepository.save(profissional);

        auditLogService.registrarLog(
                "ATUALIZACAO_PROFISSIONAL",
                ENTITY_NAME,
                updatedProfissional.getId(),
                "Dados do profissional atualizados."
        );

        return toResponseDto(updatedProfissional);
    }


    @PreAuthorize("hasRole('ADMIN') or (hasRole('PROFISSIONAL') and #id == authentication.principal.id)")
    public void delete(Long id) {
        Profissional profissional = findModelById(id);

        if (profissional.getAtivo()) {
            profissional.setAtivo(false);
            profissionalRepository.save(profissional);

            auditLogService.registrarLog(
                    "DESATIVACAO_PROFISSIONAL",
                    ENTITY_NAME,
                    id,
                    "Profissional desativado logicamente."
            );
        }
    }

    private ProfissionalResponseDto toResponseDto(Profissional profissional) {
        return new ProfissionalResponseDto(
                profissional.getId(),
                profissional.getNome(),
                profissional.getEmail(),
                profissional.getTelefone(),
                profissional.getEspecialidade(),
                profissional.getAtivo(),
                profissional.getDataCriacao(),
                profissional.getDataAtualizacao()
        );
    }
}