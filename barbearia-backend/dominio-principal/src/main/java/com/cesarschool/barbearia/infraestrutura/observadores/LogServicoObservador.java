package com.cesarschool.barbearia.infraestrutura.observadores;

import org.springframework.stereotype.Component;

import com.cesarschool.barbearia.dominio.compartilhado.observer.Observador;
import com.cesarschool.barbearia.dominio.principal.servico.eventos.ServicoOferecidoEvent;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class LogServicoObservador implements Observador<ServicoOferecidoEvent> {

    @Override
    public void atualizar(ServicoOferecidoEvent evento) {
        log.info("[OBSERVER MANUAL] Serviço alterado! Ação: {} | Serviço: {}", 
                evento.getTipoAcao(), 
                evento.getServico().getNome());
    }
}