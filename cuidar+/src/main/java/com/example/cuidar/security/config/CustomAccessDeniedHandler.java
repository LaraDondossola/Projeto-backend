package com.example.cuidar.security.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        // Define o status da resposta como 403 Forbidden
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        // Define o tipo de conteúdo da resposta
        response.setContentType("application/json");
        // Escreve o corpo da resposta
        response.getWriter().write("{\"error\": \"Acesso Negado\", \"message\": \"Você não tem permissão para acessar este recurso.\"}");
    }
}