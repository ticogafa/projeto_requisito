package com.cesarschool.barbearia.apresentacao.servico;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cesarschool.barbearia.dominio.principal.servico.ServicoOferecido;
import com.cesarschool.barbearia.dominio.principal.servico.ServicoOferecidoServico;

@RestController
@RequestMapping("/api/servico-oferecido")
public class ServicoOferecidoControlador {

    @Autowired private ServicoOferecidoServico servico;

    @RequestMapping("/listar/")
    public ResponseEntity<List<ServicoOferecido>> listarTodos() {
        return ResponseEntity.ok(servico.listarTodos());
    }
}
