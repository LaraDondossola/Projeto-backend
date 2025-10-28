package com.example.cuidar.controllers;

import com.example.cuidar.dtos.cliente.ClienteCreateDto;
import com.example.cuidar.services.ClienteService;
import com.example.cuidar.dtos.cliente.ClienteResponseDto;
import com.example.cuidar.dtos.cliente.ClienteUpdateDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    @Autowired
    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @PostMapping
    public ResponseEntity<ClienteResponseDto> create(@RequestBody @Valid ClienteCreateDto dto) {
        ClienteResponseDto newCliente = clienteService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(newCliente);
    }

    @GetMapping
    public ResponseEntity<Page<ClienteResponseDto>> findAll(
            @PageableDefault(size = 10, sort = "nome") Pageable pageable) {

        Page<ClienteResponseDto> clientes = clienteService.findAll(pageable);
        return ResponseEntity.ok(clientes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponseDto> findById(@PathVariable Long id) {
        ClienteResponseDto cliente = clienteService.findById(id);
        return ResponseEntity.ok(cliente);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponseDto> update(
            @PathVariable Long id,
            @RequestBody @Valid ClienteUpdateDto dto) {

        ClienteResponseDto updatedCliente = clienteService.update(id, dto);
        return ResponseEntity.ok(updatedCliente);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        clienteService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

