package com.example.cuidar.dtos.cliente;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record ClienteUpdateDto(
        @Size(max = 100, message = "O nome não pode exceder 100 caracteres.")
        String nome,

        @Email(message = "Formato de email inválido.")
        @Size(max = 100, message = "O email não pode exceder 100 caracteres.")
        String email,

        @Size(max = 20, message = "O telefone não pode exceder 20 caracteres.")
        String telefone,

        Boolean ativo
) {}

