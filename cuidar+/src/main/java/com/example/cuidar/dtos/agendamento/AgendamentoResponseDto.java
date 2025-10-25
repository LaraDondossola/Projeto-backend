package com.example.cuidar.dtos.agendamento;

import java.time.LocalDateTime;

public record AgendamentoResponseDto(
        Long id,

        // Dados do Cliente (simplificados)
        Long clienteId,
        String clienteNome,

        // Dados do Profissional (simplificados)
        Long profissionalId,
        String profissionalNome,

        // Dados do Serviço (simplificados)
        Long servicoId,
        String servicoNome,

        LocalDateTime dataHoraInicio,
        LocalDateTime dataHoraFim,
        String status,

        LocalDateTime dataCriacao,
        LocalDateTime dataAtualizacao
) {}