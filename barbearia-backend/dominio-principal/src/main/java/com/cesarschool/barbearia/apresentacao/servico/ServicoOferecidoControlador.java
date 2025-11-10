package com.cesarschool.barbearia.apresentacao.servico;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cesarschool.barbearia.aplicacao.servico.ServicoOferecidoResumo;
import com.cesarschool.barbearia.apresentacao.BackendMapeador;
import com.cesarschool.barbearia.apresentacao.BackendMapeador.ServicoOferecidoResumoImpl;
import com.cesarschool.barbearia.dominio.principal.servico.ServicoOferecidoServico;

/**
 * Controlador REST para operações com ServicoOferecido.
 * Seguindo o padrão do SGB, retorna DTOs (Resumos) ao invés de entidades de domínio.
 */
@RestController
@RequestMapping("/api/servico-oferecido")
public class ServicoOferecidoControlador {

    @Autowired 
    private ServicoOferecidoServico servico;
    
    @Autowired 
    private BackendMapeador mapeador;

    /**
     * Lista todos os serviços oferecidos.
     * @return Lista de resumos de serviços oferecidos
     */
    @GetMapping("/listar/")
    public ResponseEntity<List<ServicoOferecidoResumo>> listarTodos() {
        List<ServicoOferecidoResumo> resumos = servico.listarTodos()
            .stream()
            .map(s -> mapeador.map(s, ServicoOferecidoResumoImpl.class))
            .collect(Collectors.toList());
        
        return ResponseEntity.ok(resumos);
    }

    
}
