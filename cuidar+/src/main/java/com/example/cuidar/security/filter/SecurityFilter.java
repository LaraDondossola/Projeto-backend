package com.example.cuidar.security.filter;

import com.example.cuidar.repositories.UsuarioRepository;
import com.example.cuidar.security.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain  ) throws ServletException, IOException {
        String token = recoverToken(request);

        if (token != null) {
            String login = jwtService.validateToken(token);

            if (!login.isEmpty()) {
                UserDetails user = usuarioRepository.findByLogin(login);

                if (user != null) {
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        // Lista de rotas públicas que o filtro deve ignorar
        List<String> publicPaths = Arrays.asList(
                "/auth",
                // "/clientes" FOI REMOVIDO DESTA LISTA
                "/profissionais",
                "/servicos"
        );

        String path = request.getServletPath();

        // O filtro não deve ser aplicado se for uma rota GET pública
        if (request.getMethod().equals("GET")) {
            for (String publicPath : publicPaths) {
                if (path.startsWith(publicPath)) {
                    return true;
                }
            }
        }

        // O filtro não deve ser aplicado se for uma rota POST pública
        if (request.getMethod().equals("POST")) {
            // Ajuste: O POST para /clientes é público (conforme SecurityConfig)
            if (path.startsWith("/auth") || path.startsWith("/clientes")) {
                return true;
            }
        }

        return false;
    }

    private String recoverToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null) return null;
        return authHeader.replace("Bearer ", "");
    }
}