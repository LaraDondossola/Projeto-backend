package com.example.cuidar.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_clientes")
public class Cliente extends Usuario {
    private String nome;
    private String telefone;

    public Cliente() {
        super();
    }

    public Cliente(String nome, String telefone) {
        super();
        this.nome = nome;
        this.telefone = telefone;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
}
