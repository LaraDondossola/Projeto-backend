package com.example.cuidar.dtos.cliente;

import java.time.LocalDateTime;

public record ClienteResponseDto(
        Long id,
        String nome,
        String email,
        String telefone,
        Boolean ativo,
        LocalDateTime dataCriacao,
        LocalDateTime dataAtualizacao
) {}

