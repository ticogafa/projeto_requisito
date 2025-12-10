package com.cesarschool.barbearia.infraestrutura.eventos;

import java.time.LocalDateTime;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.cesarschool.barbearia.dominio.principal.profissional.eventos.ProfissionalEvent;
import com.cesarschool.barbearia.dominio.principal.servico.eventos.ServicoOferecidoEvent;

@Component
public class AuditoriaListener {

    // --- OUVINTE DE PROFISSIONAIS ---
    @Async
    @EventListener
    public void registrarAuditoria(ProfissionalEvent evento) {
        if (evento.getProfissional() == null) return;

        String nome = evento.getProfissional().getNome();
        String id = (evento.getProfissional().getId() != null) 
            ? evento.getProfissional().getId().getValor().toString() : "N/A";
        String acao = evento.getTipoAcao().toString();

        System.out.println(String.format("[AUDITORIA PROFISSIONAL] Acao: %s | ID: %s | Nome: %s | Data: %s", 
            acao, id, nome, LocalDateTime.now()));

    if (evento.getTipoAcao() == ProfissionalEvent.TipoAcao.DESLIGADO) {
            System.out.println("   --> ALERTA DE RH: Profissional desligado. Motivo: " + 
                evento.getProfissional().getMotivoInatividade());
        }
    }

    // --- OUVINTE DE SERVIÇOS ---
    @Async
    @EventListener
    public void registrarAuditoriaServico(ServicoOferecidoEvent evento) {
        // Proteção contra NullPointerException
        if (evento.getServico() == null) {
            System.out.println("[AUDITORIA SERVICO] ERRO: Evento recebido sem dados do servico.");
            return;
        }

        String nome = evento.getServico().getNome();
        String id = (evento.getServico().getId() != null) 
            ? evento.getServico().getId().getValor().toString() : "N/A";
        String acao = evento.getTipoAcao().toString();

        System.out.println(String.format("[AUDITORIA SERVICO] Acao: %s | ID: %s | Servico: %s | Preco: %s", 
            acao, id, nome, evento.getServico().getPreco()));
            
        if (evento.getTipoAcao() == ServicoOferecidoEvent.TipoAcao.DESATIVADO) {
             System.out.println("   --> Servico desativado do catalogo.");
        }
    }
}