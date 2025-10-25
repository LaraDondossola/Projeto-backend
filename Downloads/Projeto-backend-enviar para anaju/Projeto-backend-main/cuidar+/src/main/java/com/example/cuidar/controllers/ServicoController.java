package com.example.cuidar.controllers;

import com.example.cuidar.dtos.servico.ServicoCreateDto;
import com.example.cuidar.dtos.servico.ServicoResponseDto;
import com.example.cuidar.dtos.servico.ServicoUpdateDto;
import com.example.cuidar.services.ServicoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/servicos")
public class ServicoController {

    private final ServicoService servicoService;

    @Autowired
    public ServicoController(ServicoService servicoService) {
        this.servicoService = servicoService;
    }

    @PostMapping
    public ResponseEntity<ServicoResponseDto> create(@RequestBody @Valid ServicoCreateDto dto) {
        ServicoResponseDto newServico = servicoService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(newServico);
    }

    @GetMapping
    public ResponseEntity<Page<ServicoResponseDto>> findAll(
            @PageableDefault(size = 10, sort = "nome") Pageable pageable) {

        Page<ServicoResponseDto> servicos = servicoService.findAll(pageable);
        return ResponseEntity.ok(servicos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServicoResponseDto> findById(@PathVariable Long id) {
        ServicoResponseDto servico = servicoService.findById(id);
        return ResponseEntity.ok(servico);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServicoResponseDto> update(
            @PathVariable Long id,
            @RequestBody @Valid ServicoUpdateDto dto) {

        ServicoResponseDto updatedServico = servicoService.update(id, dto);
        return ResponseEntity.ok(updatedServico);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        servicoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
