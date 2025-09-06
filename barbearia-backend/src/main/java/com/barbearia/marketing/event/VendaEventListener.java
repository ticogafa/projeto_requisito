package com.barbearia.marketing.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.barbearia.marketing.model.Cliente;
import com.barbearia.marketing.repository.ClienteRepository;
import com.barbearia.marketing.service.FidelidadeService;
import com.barbearia.vendas.model.Venda;

@Component
public class VendaEventListener {
    private final FidelidadeService fidelidadeService;
    private final ClienteRepository clienteRepository;

public VendaEventListener(FidelidadeService fidelidadeService, ClienteRepository clienteRepository) {
        this.fidelidadeService = fidelidadeService;
        this.clienteRepository = clienteRepository;
    }

    @EventListener
    @Transactional 
    public void handleVendaRealizadaEvent(Venda venda) {
        int pontosGanhos = fidelidadeService.calcularPontosParaVenda(venda);
        if (pontosGanhos > 0) {
            Cliente cliente = clienteRepository.findById(venda.getCliente().getId())
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado: " + venda.getCliente().getId()));
            cliente.adicionarPontos(pontosGanhos);
            clienteRepository.save(cliente);
        }
    }
}