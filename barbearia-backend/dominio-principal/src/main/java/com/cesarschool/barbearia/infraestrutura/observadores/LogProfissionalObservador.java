package com.cesarschool.barbearia.infraestrutura.observadores;

import org.springframework.stereotype.Component;

import com.cesarschool.barbearia.dominio.compartilhado.observer.Observador;
import com.cesarschool.barbearia.dominio.principal.profissional.eventos.ProfissionalEvent;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class LogProfissionalObservador implements Observador<ProfissionalEvent> {

    @Override
    public void atualizar(ProfissionalEvent evento) {
        
        log.info("[OBSERVER MANUAL] Profissional alterado! Ação: {} | Nome: {}", 
                evento.getTipoAcao(), 
                evento.getProfissional().getNome());
    }
}