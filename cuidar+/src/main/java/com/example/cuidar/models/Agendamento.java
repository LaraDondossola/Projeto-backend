package com.example.cuidar.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_agendamentos")
public class Agendamento extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relacionamento com Cliente (N Agendamentos para 1 Cliente)
    @ManyToOne
    @JoinColumn(name = "cliente_id") // Coluna de chave estrangeira na tabela tb_agendamentos
    private Cliente cliente;

    // Relacionamento com Profissional (N Agendamentos para 1 Profissional)
    @ManyToOne
    @JoinColumn(name = "profissional_id") // Coluna de chave estrangeira
    private Profissional profissional;

    // Relacionamento com Serviço (N Agendamentos para 1 Serviço)
    @ManyToOne
    @JoinColumn(name = "servico_id") // Coluna de chave estrangeira

    private Servico servico;
    private LocalDateTime dataHoraInicio;
    private LocalDateTime dataHoraFim; // Calculado com base na duração do Serviço
    private String status; // Ex: PENDENTE, CONFIRMADO, CANCELADO, CONCLUIDO

    public Agendamento() {}

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Profissional getProfissional() {
        return profissional;
    }

    public void setProfissional(Profissional profissional) {
        this.profissional = profissional;
    }

    public Servico getServico() {
        return servico;
    }

    public void setServico(Servico servico) {
        this.servico = servico;
    }

    public LocalDateTime getDataHoraInicio() {
        return dataHoraInicio;
    }

    public void setDataHoraInicio(LocalDateTime dataHoraInicio) {
        this.dataHoraInicio = dataHoraInicio;
    }

    public LocalDateTime getDataHoraFim() {
        return dataHoraFim;
    }

    public void setDataHoraFim(LocalDateTime dataHoraFim) {
        this.dataHoraFim = dataHoraFim;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
