package com.example.cuidar.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_clientes")
public class Cliente extends Usuario {

    // O 'id', 'login', 'senha', 'role' e 'ativo' são herdados de Usuario.
    // REMOVEMOS OS CAMPOS DUPLICADOS DAQUI.

    // Campos que são específicos de Cliente
    private String nome;
    private String telefone;

    public Cliente() {
        super(); // Chama o construtor do Usuario
    }

    // Construtor opcional para facilitar
    public Cliente(String nome, String telefone) {
        super();
        this.nome = nome;
        this.telefone = telefone;
    }

    // Getters e Setters para os campos de Cliente
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