package com.example.cuidar.services;

import com.example.cuidar.dtos.cliente.ClienteCreateDto;
import com.example.cuidar.dtos.cliente.ClienteResponseDto;
import com.example.cuidar.dtos.cliente.ClienteUpdateDto;
import com.example.cuidar.models.Cliente;
import com.example.cuidar.models.Role;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.example.cuidar.repositories.ClienteRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ClienteService {

    private static final String ENTITY_NAME = "Cliente";

    private final ClienteRepository clienteRepository;
    private final AuditLogService auditLogService;
    private final PasswordEncoder passwordEncoder;

    public ClienteService(ClienteRepository clienteRepository, AuditLogService auditLogService, PasswordEncoder passwordEncoder) {
        this.clienteRepository = clienteRepository;
        this.auditLogService = auditLogService;
        this.passwordEncoder = passwordEncoder;
    }


    public ClienteResponseDto create(ClienteCreateDto dto) {
        Cliente cliente = new Cliente();
        cliente.setNome(dto.nome());
        cliente.setLogin(dto.email());
        cliente.setSenha(passwordEncoder.encode(dto.senha()));
        cliente.setTelefone(dto.telefone());
        cliente.setAtivo(true);
        cliente.setRole(Role.CLIENTE);

        Cliente savedCliente = clienteRepository.save(cliente);

        auditLogService.registrarLog(
                "CRIACAO_CLIENTE",
                ENTITY_NAME,
                savedCliente.getId(),
                "Novo cliente cadastrado."
        );

        return toResponseDto(savedCliente);
    }


    @PreAuthorize("hasRole('ADMIN')")
    public Page<ClienteResponseDto> findAll(Pageable pageable) {
        Page<Cliente> clientesPage = clienteRepository.findAllByAtivoTrue(pageable);
        return clientesPage.map(this::toResponseDto);
    }


    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    public ClienteResponseDto findById(Long id) {
        Cliente cliente = findModelById(id);
        return toResponseDto(cliente);
    }

    public Cliente findModelById(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente com ID " + id + " não encontrado."));

        if (Boolean.FALSE.equals(cliente.getAtivo())) {
            throw new EntityNotFoundException("Cliente com ID " + id + " não encontrado.");
        }

        return cliente;
    }


    @Transactional
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    public ClienteResponseDto update(Long id, ClienteUpdateDto dto) {
        Cliente cliente = findModelById(id);

        Optional.ofNullable(dto.nome()).ifPresent(cliente::setNome);
        Optional.ofNullable(dto.email()).ifPresent(cliente::setLogin);
        Optional.ofNullable(dto.telefone()).ifPresent(cliente::setTelefone);
        Optional.ofNullable(dto.ativo()).ifPresent(cliente::setAtivo);

        Cliente updatedCliente = clienteRepository.save(cliente);

        auditLogService.registrarLog(
                "ATUALIZACAO_CLIENTE",
                ENTITY_NAME,
                updatedCliente.getId(),
                "Dados do cliente atualizados."
        );

        return toResponseDto(updatedCliente);
    }


    @Transactional
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    public void delete(Long id) {
        Cliente cliente = findModelById(id);

        if (cliente.getAtivo()) {
            cliente.setAtivo(false);
            clienteRepository.save(cliente);

            auditLogService.registrarLog(
                    "DESATIVACAO_CLIENTE",
                    ENTITY_NAME,
                    id,
                    "Cliente desativado logicamente."
            );
        }
    }

    private ClienteResponseDto toResponseDto(Cliente cliente) {
        return new ClienteResponseDto(
                cliente.getId(),
                cliente.getNome(),
                cliente.getLogin(),
                cliente.getTelefone(),
                cliente.getAtivo(),
                cliente.getDataCriacao(),
                cliente.getDataAtualizacao()
        );
    }
}