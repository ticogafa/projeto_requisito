package com.cesarschool.barbearia.apresentacao.caixa;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cesarschool.barbearia.dominio.principal.caixa.Caixa;
import com.cesarschool.barbearia.dominio.principal.caixa.CaixaServico;

import lombok.Data;

@RestController
@RequestMapping("/api/caixa")
public class CaixaControlador {

    @Autowired
    private CaixaServico caixaServico;

    @GetMapping
    public ResponseEntity<List<Caixa>> getLancamentos() {
        return ResponseEntity.ok(caixaServico.getLancamentos());
    }

    @PostMapping
    public ResponseEntity<Caixa> adicionarLancamento(@RequestBody LancamentoRequest request) {
        Caixa novoLancamento = caixaServico.registrarLancamento(request.getDescricao(), request.getValor(), request.getTipo());
        return ResponseEntity.ok(novoLancamento);
    }

    @Data
    public static class LancamentoRequest {
        private String descricao;
        private BigDecimal valor;
        private Caixa.TipoLancamento tipo;
    }
}
