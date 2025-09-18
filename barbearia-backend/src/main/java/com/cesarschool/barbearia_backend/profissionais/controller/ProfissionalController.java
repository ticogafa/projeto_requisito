package com.cesarschool.barbearia_backend.profissionais.controller;

import com.cesarschool.barbearia_backend.profissionais.dto.ProfissionalDTOs.*;
import com.cesarschool.barbearia_backend.profissionais.dto.HorarioTrabalhoDTOs.*;
import com.cesarschool.barbearia_backend.profissionais.service.ProfissionalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profissionais")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ProfissionalController {

    private final ProfissionalService profissionalService;

    @PostMapping
    public ResponseEntity<ProfissionalResponse> criarProfissional(@Valid @RequestBody CriarProfissionalRequest request) {
        ProfissionalResponse profissional = profissionalService.criarProfissional(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(profissional);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfissionalResponse> buscarProfissionalPorId(@PathVariable Integer id) {
        ProfissionalResponse profissional = profissionalService.buscarPorId(id);
        return ResponseEntity.ok(profissional);
    }

    @GetMapping
    public ResponseEntity<List<ProfissionalResponse>> listarProfissionais() {
        List<ProfissionalResponse> response = profissionalService.listarProfissionais();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProfissionalResponse> atualizarProfissional(
            @PathVariable Integer id,
            @Valid @RequestBody AtualizarProfissionalRequest request) {
        ProfissionalResponse profissional = profissionalService.atualizarProfissional(id, request);
        return ResponseEntity.ok(profissional);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarProfissional(@PathVariable Integer id) {
        profissionalService.deletarProfissional(id);
        return ResponseEntity.noContent().build();
    }

    // ================================
    // Endpoints para Horários de Trabalho
    // ================================

    @PostMapping("/{profissionalId}/horarios")
    public ResponseEntity<HorarioTrabalhoResponse> criarHorarioTrabalho(
            @PathVariable Integer profissionalId,
            @Valid @RequestBody CriarHorarioTrabalhoRequest request) {
        HorarioTrabalhoResponse response = profissionalService.criarHorarioTrabalho(profissionalId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{profissionalId}/horarios")
    public ResponseEntity<ListarHorariosTrabalhoResponse> listarHorariosPorProfissional(@PathVariable Integer profissionalId) {
        ListarHorariosTrabalhoResponse response = profissionalService.listarHorariosPorProfissional(profissionalId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{profissionalId}/horarios/{horarioId}")
    public ResponseEntity<HorarioTrabalhoResponse> atualizarHorarioTrabalho(
            @PathVariable Integer profissionalId,
            @PathVariable Integer horarioId,
            @Valid @RequestBody AtualizarHorarioTrabalhoRequest request) {
        HorarioTrabalhoResponse response = profissionalService.atualizarHorarioTrabalho(profissionalId, horarioId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{profissionalId}/horarios/{horarioId}")
    public ResponseEntity<Void> deletarHorarioTrabalho(
            @PathVariable Integer profissionalId,
            @PathVariable Integer horarioId) {
        profissionalService.deletarHorarioTrabalho(profissionalId, horarioId);
        return ResponseEntity.noContent().build();
    }
}
