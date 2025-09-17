package com.cesarschool.barbearia_backend.agendamento.rest.controller;



import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cesarschool.barbearia_backend.agendamento.dto.AgendamentoDTOs.AgendamentoResponse;
import com.cesarschool.barbearia_backend.agendamento.dto.AgendamentoDTOs.CriarAgendamentoRequest;
import com.cesarschool.barbearia_backend.agendamento.service.AgendamentoService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/agendamentos")
@RequiredArgsConstructor
public class AgendamentoController {

    private final AgendamentoService service;

    @PostMapping("/criar-agendamento")
    public ResponseEntity<AgendamentoResponse> criarAgendamento(@RequestBody CriarAgendamentoRequest request) {
        AgendamentoResponse response = service.criarAgendamento(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AgendamentoResponse> buscarPorId(@PathVariable Integer id) {
        AgendamentoResponse response = service.buscarPorId(id);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/confirmar-agendamento/{id}")
    public ResponseEntity<AgendamentoResponse> confirmarAgendamento(@PathVariable Integer id) {
        AgendamentoResponse response = service.confirmarAgendamento(id);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/cancelar-agendamento/{id}")
    public ResponseEntity<AgendamentoResponse> cancelarAgendamento(@PathVariable Integer id) {
        AgendamentoResponse response = service.cancelarAgendamento(id);
        return ResponseEntity.ok(response);
    }
}
