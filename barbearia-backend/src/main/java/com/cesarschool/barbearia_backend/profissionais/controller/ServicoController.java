package com.cesarschool.barbearia_backend.profissionais.controller;

import com.cesarschool.barbearia_backend.profissionais.dto.ServicoDTOs.*;
import com.cesarschool.barbearia_backend.profissionais.service.ServicoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/servicos")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ServicoController {

    private final ServicoService servicoService;

    @PostMapping("/criar")
    public ResponseEntity<ServicoResponse> criarServico(@Valid @RequestBody CriarServicoRequest request) {
        ServicoResponse servico = servicoService.criarServico(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(servico);
    }

    @GetMapping("/buscar/{id}")
    public ResponseEntity<ServicoResponse> buscarServicoPorId(@PathVariable Integer id) {
        ServicoResponse servico = servicoService.buscarPorId(id);
        return ResponseEntity.ok(servico);
    }

    @GetMapping("/listar")
    public ResponseEntity<List<ServicoResponse>> listarServicos() {
        List<ServicoResponse> response = servicoService.listarServicos();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/atualizar")
    public ResponseEntity<ServicoResponse> atualizarServico(
            @Valid @RequestBody AtualizarServicoRequest request) {
        ServicoResponse servico = servicoService.atualizarServico(request);
        return ResponseEntity.ok(servico);
    }

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<Void> deletarServico(@PathVariable Integer id) {
        servicoService.deletarServico(id);
        return ResponseEntity.noContent().build();
    }
}
