package com.example.cuidar.security.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        // Define o status da resposta como 401 Unauthorized
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        // Define o tipo de conteúdo da resposta
        response.setContentType("application/json");
        // Escreve o corpo da resposta
        response.getWriter().write("{\"error\": \"Não Autorizado\", \"message\": \"Acesso negado. Credenciais de autenticação ausentes ou inválidas.\"}");
    }
}