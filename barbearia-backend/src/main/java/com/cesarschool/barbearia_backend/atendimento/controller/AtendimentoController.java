package com.cesarschool.barbearia_backend.atendimento.controller;

import com.cesarschool.barbearia_backend.atendimento.controller.dto.IniciarAtendimentoRequest;
import com.cesarschool.barbearia_backend.atendimento.model.Atendimento;
import com.cesarschool.barbearia_backend.atendimento.service.AtendimentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/atendimentos")
public class AtendimentoController {

    @Autowired
    private AtendimentoService atendimentoService;

    @PostMapping("/iniciar")
    public ResponseEntity<Atendimento> iniciarAtendimento(@RequestBody IniciarAtendimentoRequest request) {
        Atendimento atendimentoIniciado = atendimentoService.iniciarAtendimento(request.agendamentoId());
        return ResponseEntity.ok(atendimentoIniciado);
    }

    @PostMapping("/{id}/finalizar")
    public ResponseEntity<Atendimento> finalizarAtendimento(@PathVariable Long id) {
        Atendimento atendimentoFinalizado = atendimentoService.finalizarAtendimento(id);
        return ResponseEntity.ok(atendimentoFinalizado);
    }
}