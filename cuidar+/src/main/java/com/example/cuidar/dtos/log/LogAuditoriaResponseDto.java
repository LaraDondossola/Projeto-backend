package com.example.cuidar.dtos.log;

import java.time.LocalDateTime;

public record LogAuditoriaResponseDto(
        Long id,
        LocalDateTime dataHoraAcao,
        String tipoAcao,
        String entidadeAfetada,
        Long entidadeId,
        String detalhes,
        String usuarioResponsavel
) {}