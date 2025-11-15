package com.cesarschool.barbearia.apresentacao.servico;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cesarschool.barbearia.aplicacao.servico.ServicoOferecidoResumo;
import com.cesarschool.barbearia.aplicacao.servico.ServicoOferecidolRepositorioAplicacao;

/**
 * Controlador REST para operações com ServicoOferecido.
 * Seguindo o padrão do SGB, retorna DTOs (Resumos) diretamente do repositório de aplicação.
 * Usa Interface-based Projection do Spring Data JPA para otimização de queries.
 */
@RestController
@RequestMapping("/api/servico-oferecido")
public class ServicoOferecidoControlador {

    @Autowired 
    private ServicoOferecidolRepositorioAplicacao repositorioAplicacao;

    /**
     * Lista todos os serviços oferecidos ordenados por nome.
     * Usa projeção direta do JPA - o Spring Data cria automaticamente a implementação
     * e otimiza a query SQL para buscar apenas os campos necessários.
     * 
     * @return Lista de resumos de serviços oferecidos
     */
    @GetMapping("/listar/")
    public ResponseEntity<List<ServicoOferecidoResumo>> listarTodos() {
        return ResponseEntity.ok(repositorioAplicacao.listarTodosResumos());
    }
}
