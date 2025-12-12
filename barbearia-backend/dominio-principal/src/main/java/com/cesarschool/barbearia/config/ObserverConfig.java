package com.cesarschool.barbearia.config;

import org.springframework.context.annotation.Configuration;

import com.cesarschool.barbearia.aplicacao.profissional.listeners.NotificacaoProfissionalObservador;
import com.cesarschool.barbearia.dominio.principal.profissional.ProfissionalServico;
import com.cesarschool.barbearia.dominio.principal.servico.ServicoOferecidoServico;
import com.cesarschool.barbearia.infraestrutura.observadores.LogProfissionalObservador;
import com.cesarschool.barbearia.infraestrutura.observadores.LogServicoObservador;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class ObserverConfig {

    private final ProfissionalServico profissionalServico;
    private final ServicoOferecidoServico servicoOferecidoServico;
    
    private final LogProfissionalObservador logProfissionalObservador;
    private final LogServicoObservador logServicoObservador;

    private final NotificacaoProfissionalObservador notificacaoEmailObservador; 

    @PostConstruct
    public void registrarObservadores() {
        System.out.println("--- REGISTRANDO OBSERVADORES ---");
        
        profissionalServico.adicionarObservador(logProfissionalObservador);
        servicoOferecidoServico.adicionarObservador(logServicoObservador);

        profissionalServico.adicionarObservador(notificacaoEmailObservador);
    }
}