package com.cesarschool.barbearia_backend.vendas.controller;

import java.math.BigDecimal;
import java.util.List;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cesarschool.barbearia_backend.vendas.model.Venda;
import com.cesarschool.barbearia_backend.vendas.service.VendaService;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/pdv")
@RequiredArgsConstructor
public class PdvController {

    private final VendaService vendaService;

    @PostMapping("/vendas")
    public ResponseEntity<Venda> registrarVenda(@RequestBody VendaRequest request) {
        Venda venda = vendaService.registrarVendaPDV(request);
        return ResponseEntity.ok(venda);
    }

    @Data
    public static class ItemRequest {
        private Integer produtoId; // opcional se for serviço
        private Integer servicoId; // opcional se for produto
        private String descricao;
        private int quantidade;
        private BigDecimal precoUnitario;
        private String tipo; // PRODUTO ou SERVICO
    }

    @Data
    public static class VendaRequest {
        private Integer clienteId; // opcional
        private List<ItemRequest> itens;
        private String observacoes; // opcional
    }
}
