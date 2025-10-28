package com.example.cuidar.dtos.servico;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ServicoResponseDto(
        Long id,
        String nome,
        String descricao,
        Integer duracaoEmMinutos,
        BigDecimal preco,
        Boolean ativo,
        LocalDateTime dataCriacao,
        LocalDateTime dataAtualizacao
) {}
