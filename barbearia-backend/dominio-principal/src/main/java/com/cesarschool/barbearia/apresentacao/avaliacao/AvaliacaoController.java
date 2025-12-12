package com.cesarschool.barbearia.apresentacao.avaliacao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cesarschool.barbearia.dominio.principal.profissional.avaliacao.Avaliacao;
import com.cesarschool.barbearia.dominio.principal.profissional.avaliacao.AvaliacaoServico;

import lombok.Data;

@RestController
@RequestMapping("/api/avaliacao")
public class AvaliacaoController {

    @Autowired
    private AvaliacaoServico servico;

    @PostMapping
    public ResponseEntity<Avaliacao> avaliar(@RequestBody AvaliacaoRequest request) {
        Avaliacao avaliacao = servico.registrarAvaliacao(
            request.getProfissionalId(),
            request.getNota()
        );
        return ResponseEntity.ok(avaliacao);
    }

    @Data
    public static class AvaliacaoRequest {
        private Integer profissionalId;
        private int nota;
    }
}
