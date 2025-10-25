package com.example.cuidar.security.controller;

import com.example.cuidar.dtos.auth.LoginDto;
import com.example.cuidar.models.Usuario;
import com.example.cuidar.repositories.UsuarioRepository;
import com.example.cuidar.security.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth" )
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDto data) {
        UsernamePasswordAuthenticationToken usernamePasswordToken = new UsernamePasswordAuthenticationToken(data.email(), data.senha());
        Authentication auth = authenticationManager.authenticate(usernamePasswordToken);

        String token = jwtService.generateToken((Usuario) auth.getPrincipal());

        return ResponseEntity.ok(token);
    }

    // Método de inicialização para criar um ADMIN para testes
    @PostMapping("/register-admin")
    public ResponseEntity<String> registerAdmin() {
        if (usuarioRepository.findByLogin("admin") != null) {
            return ResponseEntity.badRequest().body("Admin já existe.");
        }

        Usuario admin = new Usuario("admin", passwordEncoder.encode("123456"), com.example.cuidar.models.Role.ADMIN);
        usuarioRepository.save(admin);

        return ResponseEntity.ok("Admin registrado com sucesso. Login: admin, Senha: 123456");
    }
}
