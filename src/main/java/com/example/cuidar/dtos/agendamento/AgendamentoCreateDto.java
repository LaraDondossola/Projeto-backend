package com.example.cuidar.dtos.agendamento;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record AgendamentoCreateDto(
        @NotNull(message = "O ID do cliente é obrigatório.")
        Long clienteId,

        @NotNull(message = "O ID do profissional é obrigatório.")
        Long profissionalId,

        @NotNull(message = "O ID do serviço é obrigatório.")
        Long servicoId,

        @NotNull(message = "A data e hora de início são obrigatórias.")
        @FutureOrPresent(message = "A data de agendamento deve ser no presente ou futuro.")
        LocalDateTime dataHoraInicio
) {}