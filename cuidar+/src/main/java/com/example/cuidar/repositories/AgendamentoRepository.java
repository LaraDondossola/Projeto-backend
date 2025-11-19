package com.example.cuidar.repositories;

import com.example.cuidar.models.Agendamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDateTime;
import java.util.List;

public interface AgendamentoRepository extends
        JpaRepository<Agendamento, Long>,
        JpaSpecificationExecutor<Agendamento> {

    List<Agendamento> findByProfissionalIdAndStatusNotAndDataHoraInicioBeforeAndDataHoraFimAfter(
            Long profissionalId, String status, LocalDateTime novoFim, LocalDateTime novoInicio
    );

    List<Agendamento> findByClienteIdAndStatusNotAndDataHoraInicioBeforeAndDataHoraFimAfter(
            Long clienteId, String status, LocalDateTime novoFim, LocalDateTime novoInicio
    );

    List<Agendamento> findByProfissionalIdAndStatusNotAndDataHoraInicioBeforeAndDataHoraFimAfterAndIdNot(
            Long profissionalId, String status, LocalDateTime novoFim, LocalDateTime novoInicio, Long agendamentoId
    );

    List<Agendamento> findByClienteIdAndStatusNotAndDataHoraInicioBeforeAndDataHoraFimAfterAndIdNot(
            Long clienteId, String status, LocalDateTime novoFim, LocalDateTime novoInicio, Long agendamentoId
    );
}