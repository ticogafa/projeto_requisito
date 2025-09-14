package com.cesarschool.barbearia_backend.agendamento.controller;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cesarschool.barbearia_backend.agendamento.model.Agendamento;
import com.cesarschool.barbearia_backend.agendamento.service.AgendamentoService;
import com.cesarschool.barbearia_backend.common.enums.StatusAgendamento;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/agendamentos")
@RequiredArgsConstructor
public class AgendamentoController {

    private final AgendamentoService service;

    @PostMapping("/criar-agendamento")
    public ResponseEntity<Agendamento> criarAgendamento(@RequestBody Agendamento agendamento) {
        Agendamento agendamentoSalvo = service.save(agendamento);
        return ResponseEntity.ok(agendamentoSalvo);
    }

    @PatchMapping("/confirmar-agendamento/{id}")
    public ResponseEntity<Agendamento> confirmarAgendamento(@PathVariable UUID id) {
        Agendamento agendamento = service.findById(id);
        agendamento.setStatus(StatusAgendamento.CONFIRMADO);
        Agendamento agendamentoSalvo = service.save(agendamento);
        return ResponseEntity.ok(agendamentoSalvo);
    }

    @PatchMapping("/cancelar-agendamento/{id}")
    public ResponseEntity<Agendamento> cancelarAgendamento(@PathVariable UUID id) {
        Agendamento agendamento = service.findById(id);
        agendamento.setStatus(StatusAgendamento.CANCELADO);
        Agendamento agendamentoSalvo = service.save(agendamento);
        return ResponseEntity.ok(agendamentoSalvo);
    }
}
