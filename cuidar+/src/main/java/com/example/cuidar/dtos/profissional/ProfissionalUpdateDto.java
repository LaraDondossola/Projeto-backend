package com.example.cuidar.dtos.profissional;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record ProfissionalUpdateDto(
        @Size(max = 100, message = "O nome não pode exceder 100 caracteres.")
        String nome,

        @Email(message = "Formato de email inválido.")
        @Size(max = 100, message = "O email não pode exceder 100 caracteres.")
        String email,

        @Size(max = 20, message = "O telefone não pode exceder 20 caracteres.")
        String telefone,

        @Size(max = 50, message = "A especialidade não pode exceder 50 caracteres.")
        String especialidade,

        Boolean ativo
) {}
