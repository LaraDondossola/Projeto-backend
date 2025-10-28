package com.example.cuidar.dtos.servico;

import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.Optional;

public record ServicoUpdateDto(
        String nome,
        String descricao,

        @Positive(message = "A duração deve ser um valor positivo.")
        Integer duracaoEmMinutos,

        @Positive(message = "O preço deve ser um valor positivo.")
        BigDecimal preco,

        Boolean ativo
) {}
