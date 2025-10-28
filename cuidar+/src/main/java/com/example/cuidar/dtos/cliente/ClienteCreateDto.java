package com.example.cuidar.dtos.cliente;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ClienteCreateDto(
        @NotBlank(message = "O nome é obrigatório.")
        @Size(max = 100, message = "O nome não pode exceder 100 caracteres.")
        String nome,

        @NotBlank(message = "O email é obrigatório.")
        @Email(message = "Formato de email inválido.")
        @Size(max = 100, message = "O email não pode exceder 100 caracteres.")
        String email,

        @Size(max = 20, message = "O telefone não pode exceder 20 caracteres.")
        String telefone,

        @NotBlank(message = "A senha é obrigatória.")
        @Size(min = 6, max = 50, message = "A senha deve ter entre 6 e 50 caracteres.")
        String senha
) {}

