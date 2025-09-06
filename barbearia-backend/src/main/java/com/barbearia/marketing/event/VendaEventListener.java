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

    private static final Logger log = LoggerFactory.getLogger(VendaEventListener.class);

    private final FidelidadeService fidelidadeService;
    private final ClienteRepository clienteRepository;

public VendaEventListener(FidelidadeService fidelidadeService, ClienteRepository clienteRepository) {
        this.fidelidadeService = fidelidadeService;
        this.clienteRepository = clienteRepository;
    }

   
    @EventListener
    @Transactional 
    public void handleVendaRealizadaEvent(Venda venda) {
        log.info("Recebido evento de venda para o cliente {}", venda.getClienteId());

        
        int pontosGanhos = fidelidadeService.calcularPontosParaVenda(venda);

        if (pontosGanhos > 0) {
            
            Cliente cliente = clienteRepository.findById(venda.getClienteId())
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado: " + venda.getClienteId()));

            
            cliente.adicionarPontos(pontosGanhos);

            
            clienteRepository.save(cliente);

            log.info("{} pontos adicionados ao cliente {}. Saldo atual: {}", pontosGanhos, cliente.getId(), cliente.getPontos());
        } else {
            log.info("Nenhum ponto a ser adicionado para a venda {}", venda.getVendaId());
        }
    }
}