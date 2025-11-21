package com.example.cuidar.dtos.agendamento;

import java.time.LocalDateTime;

public record AgendamentoResponseDto(
        Long id,

        Long clienteId,
        String clienteNome,

        Long profissionalId,
        String profissionalNome,

        Long servicoId,
        String servicoNome,

        LocalDateTime dataHoraInicio,
        LocalDateTime dataHoraFim,
        String status,

        LocalDateTime dataCriacao,
        LocalDateTime dataAtualizacao
) {}
