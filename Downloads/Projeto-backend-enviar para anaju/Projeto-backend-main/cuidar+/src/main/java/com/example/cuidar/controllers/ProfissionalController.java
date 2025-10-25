package com.example.cuidar.controllers;

import com.example.cuidar.dtos.profissional.ProfissionalCreateDto;
import com.example.cuidar.dtos.profissional.ProfissionalResponseDto;
import com.example.cuidar.dtos.profissional.ProfissionalUpdateDto;
import com.example.cuidar.services.ProfissionalService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profissionais")
public class ProfissionalController {

    private final ProfissionalService profissionalService;

    @Autowired
    public ProfissionalController(ProfissionalService profissionalService) {
        this.profissionalService = profissionalService;
    }

    @PostMapping
    public ResponseEntity<ProfissionalResponseDto> create(@RequestBody @Valid ProfissionalCreateDto dto) {
        ProfissionalResponseDto newProfissional = profissionalService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(newProfissional);
    }

    @GetMapping
    public ResponseEntity<Page<ProfissionalResponseDto>> findAll(
            @PageableDefault(size = 10, sort = "nome") Pageable pageable) {

        Page<ProfissionalResponseDto> profissionais = profissionalService.findAll(pageable);
        return ResponseEntity.ok(profissionais);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfissionalResponseDto> findById(@PathVariable Long id) {
        ProfissionalResponseDto profissional = profissionalService.findById(id);
        return ResponseEntity.ok(profissional);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProfissionalResponseDto> update(
            @PathVariable Long id,
            @RequestBody @Valid ProfissionalUpdateDto dto) {

        ProfissionalResponseDto updatedProfissional = profissionalService.update(id, dto);
        return ResponseEntity.ok(updatedProfissional);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        profissionalService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

