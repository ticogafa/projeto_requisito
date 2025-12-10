package com.cesarschool.barbearia.apresentacao.profissional;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

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

    @PostMapping
    public ResponseEntity<Profissional> criar(@RequestBody Profissional profissional) {
        return exceptionHandler.withHandler(() -> {
            logger.info("Recebendo requisição POST para criar profissional: " + profissional.getNome());
            
            Profissional novo = servico.registrarNovo(profissional);
            
            URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(novo.getId().getValor())
                    .toUri();
                    
            logger.success("Profissional criado com sucesso. ID: " + novo.getId().getValor());
            return ResponseEntity.created(uri).body(novo);
        });
    }

    @GetMapping
    public ResponseEntity<List<Profissional>> listarTodos() {
        return exceptionHandler.withHandler(() -> {
            logger.info("Listando todos os profissionais");
            return ResponseEntity.ok(servico.listarTodos());
        });
    }

    @GetMapping("/{id}")
    public ResponseEntity<Profissional> buscarPorId(@PathVariable Integer id) {
        return exceptionHandler.withHandler(() -> {
            return ResponseEntity.ok(servico.buscarPorId(id));
        });
    }

    @PutMapping("/{id}")
    public ResponseEntity<Profissional> atualizar(@PathVariable Integer id, @RequestBody Profissional profissional) {
        return exceptionHandler.withHandler(() -> {
            logger.info("Atualizando profissional ID: " + id);
            return ResponseEntity.ok(servico.atualizar(id, profissional));
        });
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        return exceptionHandler.withHandler(() -> {
            logger.info("Solicitando desligamento do profissional ID: " + id);
            servico.desligarProfissional(id);
            return ResponseEntity.noContent().build();
        });
    }
    
    @GetMapping("/disponivel/primeiro") 
    public ResponseEntity<Profissional> buscarPrimeiroProfissionalDisponivel(LocalDateTime dataHora, int duracaoServicoMinutos) {
        return exceptionHandler.withHandler(() -> {
            logger.info("Buscando primeiro profissional disponível - dataHora: " + dataHora);
            Profissional profissional = servico.buscarPrimeiroProfissionalDisponivel(dataHora, duracaoServicoMinutos);
            return ResponseEntity.ok(profissional);
        });
    }

    @GetMapping("/qualificados/{servicoId}")
    public ResponseEntity<List<Profissional>> buscarQualificadosParaServico(@PathVariable Integer servicoId) {
        return exceptionHandler.withHandler(() -> {
            logger.info("Buscando profissionais qualificados para serviço ID: " + servicoId);
            List<Profissional> profissionais = servico.buscarQualificadosParaServico(new ServicoOferecidoId(servicoId));
            return ResponseEntity.ok(profissionais);
        });
    }
    
    @GetMapping("/disponiveis/{dataHora}/{duracaoMinutos}")
    public ResponseEntity<List<Profissional>> buscarDisponiveisNaDataHora(@PathVariable LocalDateTime dataHora, @PathVariable Integer duracaoMinutos) {
        return exceptionHandler.withHandler(() -> {
            logger.info("Buscando profissionais disponíveis na data: " + dataHora);
            List<Profissional> profissionais = servico.buscarDisponiveisNaDataHora(dataHora, duracaoMinutos);
            return ResponseEntity.ok(profissionais);
        });
    }
}