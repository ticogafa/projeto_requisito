package com.cesarschool.barbearia_backend.atendimento.service;

import com.cesarschool.barbearia_backend.atendimento.model.Atendimento;
import com.cesarschool.barbearia_backend.atendimento.repository.AtendimentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AtendimentoService {

    @Autowired
    private AtendimentoRepository atendimentoRepository;

    @Transactional
    public Atendimento iniciarAtendimento(Long agendamentoId) {
        // A lógica de negócio de criação e início está na própria entidade
        Atendimento novoAtendimento = Atendimento.criarParaAgendamento(agendamentoId);
        novoAtendimento.iniciar();
        return atendimentoRepository.save(novoAtendimento);
    }

    @Transactional
    public Atendimento finalizarAtendimento(Long atendimentoId) {
        Atendimento atendimento = atendimentoRepository.findById(atendimentoId)
                .orElseThrow(() -> new RuntimeException("Atendimento não encontrado!"));
        
        atendimento.finalizar(); // Chama a regra de negócio da entidade
        return atendimentoRepository.save(atendimento);
    }
}