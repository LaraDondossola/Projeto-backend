package com.example.cuidar.dtos.agendamento;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

public record AgendamentoUpdateDto(
        @FutureOrPresent(message = "A nova data deve ser no presente ou futuro.")
        LocalDateTime dataHoraInicio,

        @Size(max = 20, message = "O status não pode exceder 20 caracteres.")
        String status
) {}