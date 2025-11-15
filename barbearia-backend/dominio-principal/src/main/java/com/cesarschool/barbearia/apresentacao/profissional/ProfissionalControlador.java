package com.cesarschool.barbearia.apresentacao.profissional;



import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cesarschool.barbearia.dominio.principal.profissional.Profissional;
import com.cesarschool.barbearia.dominio.principal.profissional.ProfissionalServico;
import com.cesarschool.barbearia.dominio.principal.servico.ServicoOferecidoId;


@RestController
@RequestMapping("/api/profissional")
public class ProfissionalControlador {

    private @Autowired ProfissionalServico servico;
    
    public ResponseEntity<Profissional> buscarPrimeiroProfissionalDisponivel(LocalDateTime dataHora, int duracaoServicoMinutos) {
        return ResponseEntity.ok(
            servico.buscarPrimeiroProfissionalDisponivel(dataHora, duracaoServicoMinutos)
        );
    }

    @GetMapping("/qualificados/{servicoId}")
    public ResponseEntity<List<Profissional>> buscarQualificadosParaServico(@PathVariable ServicoOferecidoId servicoId) {
        return ResponseEntity.ok(servico.buscarQualificadosParaServico(servicoId));
    }
    @GetMapping("/disponiveis/{dataHora}/{duracaoMinutos}")
    public ResponseEntity<List<Profissional>> buscarDisponiveisNaDataHora(@PathVariable LocalDateTime dataHora, @PathVariable Integer duracaoMinutos) {
        return ResponseEntity.ok(servico.buscarDisponiveisNaDataHora(dataHora, duracaoMinutos));
    }
}