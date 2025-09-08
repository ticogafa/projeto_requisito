package com.barbearia.agendamento.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException.NotImplemented;

import com.barbearia.agendamento.model.Agendamento;
import com.barbearia.agendamento.repository.AgendamentoRepository;
import com.barbearia.profissionais.model.Profissional;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class AgendamentoService {
    private final AgendamentoRepository repository;

    public Optional<Agendamento> findById(UUID id){
        // regras de negócio....
        return repository.findById(id);
    }

        public List<Agendamento> findAll(){
        // regras de negócio....
        return repository.findAll();
    }

    public void verificarConflitoProfissional(Profissional profissional) {
        throw new UnsupportedOperationException("Metodo não implementado");
    }

    public void verificarConflitoHorario(Agendamento agendamento) {
        throw new UnsupportedOperationException("Metodo não implementado");
    }

    public void verificarAlteracaoStatus(Agendamento agendamento) {
        throw new UnsupportedOperationException("Metodo não implementado");
    }

    public Agendamento save(Agendamento agendamento) throws Exception{
        // -- • Um Agendamento só pode ser criado se o Horário Disponível estiver livre
        // -- • Um Agendamento não pode ser criado em um horário fora da jornada de trabalho do    profissional
        // -- • Um Agendamento só pode ser cancelado até 2 horas antes do horário

        this.verificarConflitoHorario(agendamento);
        this.verificarConflitoProfissional(agendamento.getProfissional());
        this.verificarAlteracaoStatus(agendamento);
        // regras de negócio....
        return repository.save(agendamento);
    }

    public void delete(Agendamento agendamento){
        // regras de negócio....
        repository.delete(agendamento);
    }
}
