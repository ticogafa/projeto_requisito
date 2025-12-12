package com.cesarschool.barbearia.apresentacao.atendimento;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cesarschool.barbearia.dominio.principal.profissional.atendimento.ExecucaoAtendimento;
import com.cesarschool.barbearia.dominio.principal.profissional.atendimento.ExecucaoAtendimentoServico;

import lombok.Data;

@RestController
@RequestMapping("/api/atendimento")
public class ExecucaoAtendimentoController {

    @Autowired
    private ExecucaoAtendimentoServico servico;

    @PostMapping("/registrar")
    public ResponseEntity<ExecucaoAtendimento> registrar(@RequestBody RegistroAtendimentoRequest request) {
        ExecucaoAtendimento execucao = servico.registrarExecucao(
            request.getProfissionalId(),
            request.getValor(),
            request.getInicio(),
            request.getFim()
        );
        return ResponseEntity.ok(execucao);
    }

    @Data
    public static class RegistroAtendimentoRequest {
        private Integer profissionalId;
        private BigDecimal valor;
        private LocalDateTime inicio;
        private LocalDateTime fim;
    }
}
