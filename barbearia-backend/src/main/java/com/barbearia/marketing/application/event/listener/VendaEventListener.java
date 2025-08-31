package com.barbearia.marketing.application.event.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.barbearia.marketing.application.dto.VendaDTO;
import com.barbearia.marketing.domain.model.Cliente;
import com.barbearia.marketing.domain.repository.ClienteRepository;
import com.barbearia.marketing.domain.service.FidelidadeService; 

@Component
public class VendaEventListener {

    private static final Logger log = LoggerFactory.getLogger(VendaEventListener.class);

    private final FidelidadeService fidelidadeService;
    private final ClienteRepository clienteRepository;

    @Autowired 
    public VendaEventListener(FidelidadeService fidelidadeService, ClienteRepository clienteRepository) {
        this.fidelidadeService = fidelidadeService;
        this.clienteRepository = clienteRepository;
    }

   
    @EventListener
    @Transactional 
    public void handleVendaRealizadaEvent(VendaDTO venda) {
        log.info("Recebido evento de venda para o cliente {}", venda.clienteId());

        
        int pontosGanhos = fidelidadeService.calcularPontosParaVenda(venda);

        if (pontosGanhos > 0) {
            
            Cliente cliente = clienteRepository.findById(venda.clienteId())
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado: " + venda.clienteId()));

            
            cliente.adicionarPontos(pontosGanhos);

            
            clienteRepository.save(cliente);

            log.info("{} pontos adicionados ao cliente {}. Saldo atual: {}", pontosGanhos, cliente.getId(), cliente.getPontos());
        } else {
            log.info("Nenhum ponto a ser adicionado para a venda {}", venda.vendaId());
        }
    }
}