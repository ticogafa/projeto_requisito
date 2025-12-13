package com.cesarschool.barbearia.apresentacao.caixa;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cesarschool.barbearia.dominio.principal.caixa.Caixa;
import com.cesarschool.barbearia.dominio.principal.cliente.caixa.IGestaoCaixa;
import com.cesarschool.barbearia.dominio.principal.cliente.caixa.LancamentoRepositorio;
import com.cesarschool.barbearia.dominio.principal.cliente.caixa.MeioPagamento;
import com.cesarschool.barbearia.dominio.principal.cliente.caixa.StatusLancamento;

import lombok.Data;

@RestController
@RequestMapping("/api/caixa")
public class CaixaControlador {

    @Autowired
    private IGestaoCaixa gestaoCaixa;
    
    @Autowired
    private LancamentoRepositorio lancamentoRepositorio;

    @GetMapping
    public ResponseEntity<List<LancamentoResponse>> getLancamentos() {
        List<LancamentoResponse> caixas = lancamentoRepositorio.buscarTodos().stream()
            .map(l -> {
                LancamentoResponse c = new LancamentoResponse();
                c.setId(l.getId().getValor().toString());
                c.setDescricao(l.getDescricao());
                c.setValor(l.getValor());
                c.setData(l.getQuando());
                if (l.getStatus() == StatusLancamento.ENTRADA) {
                    c.setTipo(Caixa.TipoLancamento.ENTRADA);
                } else if (l.getStatus() == StatusLancamento.SAIDA) {
                    c.setTipo(Caixa.TipoLancamento.SAIDA);
                }
                return c;
            })
            .filter(c -> c.getTipo() != null)
            .collect(Collectors.toList());
            
        return ResponseEntity.ok(caixas);
    }

    @PostMapping
    public ResponseEntity<Void> adicionarLancamento(@RequestBody LancamentoRequest request) {
        if (request.getTipo() == Caixa.TipoLancamento.ENTRADA) {
            gestaoCaixa.registrarEntrada(request.getDescricao(), request.getValor(), MeioPagamento.DINHEIRO);
        } else {
            gestaoCaixa.registrarSaida(request.getDescricao(), request.getValor(), MeioPagamento.DINHEIRO);
        }
        return ResponseEntity.ok().build();
    }

    @Data
    public static class LancamentoRequest {
        private String descricao;
        private BigDecimal valor;
        private Caixa.TipoLancamento tipo;
    }

    @Data
    public static class LancamentoResponse {
        private String id;
        private String descricao;
        private BigDecimal valor;
        private Caixa.TipoLancamento tipo;
        private LocalDateTime data;
    }
}
