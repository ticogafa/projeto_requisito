package com.cesarschool.barbearia.apresentacao.servico;

import java.net.URI;
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
import com.cesarschool.barbearia.dominio.principal.servico.ServicoOferecido;
import com.cesarschool.barbearia.dominio.principal.servico.ServicoOferecidoServico;

@RestController
@RequestMapping("/api/servico") 
public class ServicoOferecidoControlador {

    private static final LoggerSingleton logger = LoggerSingleton.getInstance();

    @Autowired
    private ServicoOferecidoServico servico;
    
    @Autowired
    private ExceptionHandler exceptionHandler;

    @PostMapping
    public ResponseEntity<ServicoOferecido> criar(@RequestBody ServicoOferecido novoServico) {
        return exceptionHandler.withHandler(() -> {
            logger.info("Criando novo serviço: " + novoServico.getNome());
            ServicoOferecido salvo = servico.registrar(novoServico);

            URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(salvo.getId().getValor())
                    .toUri();
                    
            return ResponseEntity.created(uri).body(salvo);
        });
    }

    @GetMapping
    public ResponseEntity<List<ServicoOferecido>> listarTodos() {
        return exceptionHandler.withHandler(() -> {
            return ResponseEntity.ok(servico.listarTodos());
        });
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServicoOferecido> buscarPorId(@PathVariable Integer id) {
        return exceptionHandler.withHandler(() -> {
            return ResponseEntity.ok(servico.buscarPorId(id));
        });
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServicoOferecido> atualizar(@PathVariable Integer id, @RequestBody ServicoOferecido dados) {
        return exceptionHandler.withHandler(() -> {
            logger.info("Atualizando serviço ID: " + id);
            return ResponseEntity.ok(servico.atualizar(id, dados));
        });
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        return exceptionHandler.withHandler(() -> {
            logger.info("Desativando serviço ID: " + id);
            servico.desativar(id, "Removido via API");
            return ResponseEntity.noContent().build();
        });
    }
}