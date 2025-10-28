package com.example.cuidar.dtos.profissional;

import java.time.LocalDateTime;

public record ProfissionalResponseDto(
        Long id,
        String nome,
        String email,
        String telefone,
        String especialidade,
        Boolean ativo,
        LocalDateTime dataCriacao,
        LocalDateTime dataAtualizacao
) {}

