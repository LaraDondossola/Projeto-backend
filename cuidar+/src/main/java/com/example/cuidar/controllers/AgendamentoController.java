package com.example.cuidar.controllers;

import com.example.cuidar.dtos.agendamento.AgendamentoCreateDto;
import com.example.cuidar.dtos.agendamento.AgendamentoResponseDto;
import com.example.cuidar.dtos.agendamento.AgendamentoUpdateDto;
import com.example.cuidar.services.AgendamentoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/agendamentos")
public class AgendamentoController {

    private final AgendamentoService agendamentoService;

    @Autowired
    public AgendamentoController(AgendamentoService agendamentoService) {
        this.agendamentoService = agendamentoService;
    }

    @PostMapping
    public ResponseEntity<AgendamentoResponseDto> create(@RequestBody @Valid AgendamentoCreateDto dto) {
        AgendamentoResponseDto newAgendamento = agendamentoService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(newAgendamento);
    }

    @GetMapping
    public ResponseEntity<Page<AgendamentoResponseDto>> findAll(
            @RequestParam(required = false) Long profissionalId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime data,
            @PageableDefault(size = 10, sort = "dataHoraInicio") Pageable pageable) {

        Page<AgendamentoResponseDto> agendamentos =
                agendamentoService.findAll(profissionalId, status, data, pageable);

        return ResponseEntity.ok(agendamentos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AgendamentoResponseDto> findById(@PathVariable Long id) {
        AgendamentoResponseDto agendamento = agendamentoService.findById(id);
        return ResponseEntity.ok(agendamento);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AgendamentoResponseDto> update(
            @PathVariable Long id,
            @RequestBody @Valid AgendamentoUpdateDto dto) {

        AgendamentoResponseDto updatedAgendamento = agendamentoService.update(id, dto);
        return ResponseEntity.ok(updatedAgendamento);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        agendamentoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
