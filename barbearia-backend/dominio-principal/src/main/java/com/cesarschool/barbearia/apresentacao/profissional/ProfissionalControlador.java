package com.cesarschool.barbearia.apresentacao.profissional;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cesarschool.barbearia.dominio.compartilhado.exceptions.ExceptionHandler;
import com.cesarschool.barbearia.dominio.compartilhado.logger.LoggerSingleton;
import com.cesarschool.barbearia.dominio.principal.profissional.Profissional;
import com.cesarschool.barbearia.dominio.principal.profissional.ProfissionalServico;
import com.cesarschool.barbearia.dominio.principal.servico.ServicoOferecidoId;

@RestController
@RequestMapping("/api/profissional")
public class ProfissionalControlador {

    private static final LoggerSingleton logger = LoggerSingleton.getInstance();

    @Autowired
    private ProfissionalServico servico;
    
    @Autowired
    private ExceptionHandler exceptionHandler;
    
    public ResponseEntity<Profissional> buscarPrimeiroProfissionalDisponivel(LocalDateTime dataHora, int duracaoServicoMinutos) {
        return exceptionHandler.withHandler(() -> {
            logger.info("Buscando primeiro profissional disponível - dataHora: " + dataHora + ", duração: " + duracaoServicoMinutos + "min");
            
            Profissional profissional = servico.buscarPrimeiroProfissionalDisponivel(dataHora, duracaoServicoMinutos);
            
            logger.success("Profissional encontrado: " + profissional.getNome());
            return ResponseEntity.ok(profissional);
        });
    }

    @GetMapping("/qualificados/{servicoId}")
    public ResponseEntity<List<Profissional>> buscarQualificadosParaServico(@PathVariable ServicoOferecidoId servicoId) {
        return exceptionHandler.withHandler(() -> {
            logger.info("Buscando profissionais qualificados para serviço ID: " + servicoId.getValor());
            
            List<Profissional> profissionais = servico.buscarQualificadosParaServico(servicoId);
            
            logger.success("Encontrados " + profissionais.size() + " profissionais qualificados");
            return ResponseEntity.ok(profissionais);
        });
    }
    
    @GetMapping("/disponiveis/{dataHora}/{duracaoMinutos}")
    public ResponseEntity<List<Profissional>> buscarDisponiveisNaDataHora(@PathVariable LocalDateTime dataHora, @PathVariable Integer duracaoMinutos) {
        return exceptionHandler.withHandler(() -> {
            logger.info("Buscando profissionais disponíveis - dataHora: " + dataHora + ", duração: " + duracaoMinutos + "min");
            
            List<Profissional> profissionais = servico.buscarDisponiveisNaDataHora(dataHora, duracaoMinutos);
            
            logger.success("Encontrados " + profissionais.size() + " profissionais disponíveis");
            return ResponseEntity.ok(profissionais);
        });
    }
}