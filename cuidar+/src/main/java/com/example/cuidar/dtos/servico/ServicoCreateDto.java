package com.example.cuidar.dtos.servico;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record ServicoCreateDto(
        @NotBlank(message = "O nome do serviço é obrigatório.")
        String nome,

        String descricao,

        @NotNull(message = "A duração em minutos é obrigatória.")
        @Positive(message = "A duração deve ser um valor positivo.")
        Integer duracaoEmMinutos,

        @NotNull(message = "O preço é obrigatório.")
        @Positive(message = "O preço deve ser um valor positivo.")
        BigDecimal preco
) {}
