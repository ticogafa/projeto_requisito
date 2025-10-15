package com.cesarschool.cucumber.agendamento;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.cesarschool.barbearia.dominio.principal.agendamento.Agendamento;
import com.cesarschool.barbearia.dominio.principal.agendamento.AgendamentoId;
import com.cesarschool.barbearia.dominio.principal.agendamento.AgendamentoRepositorio;
import com.cesarschool.barbearia.dominio.principal.agendamento.StatusAgendamento;
import com.cesarschool.barbearia.dominio.principal.cliente.ClienteId;
import com.cesarschool.barbearia.dominio.principal.profissional.ProfissionalId;
import com.cesarschool.barbearia.dominio.principal.servico.ServicoOferecidoId;

class AgendamentoMockRepositorio implements AgendamentoRepositorio {
    @Override
    public Agendamento salvar(Agendamento agendamento) {
        return new Agendamento(
                LocalDateTime.now().plusHours(2),
                new ClienteId(1),
                new ProfissionalId(1),
                new ServicoOferecidoId(1),
                "Nenhuma observação"
            );
    }

    @Override
    public List<Agendamento> buscarPorCliente(ClienteId clienteId) {
        return new ArrayList<>();
    }

    @Override
    public Optional<Agendamento> buscarPorId(AgendamentoId id) {
        return Optional.empty();
    }

    @Override
    public List<Agendamento> buscarPorPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        return new ArrayList<>();
    }

    @Override
    public List<Agendamento> buscarPorProfissional(ProfissionalId profissionalId) {
        return new ArrayList<>();
    }

    @Override
    public List<Agendamento> buscarPorStatus(StatusAgendamento status) {
        return new ArrayList<>();
    }

    @Override
    public boolean existeAgendamentoNoPeriodo(ProfissionalId profissionalId, LocalDateTime dataHora,
            int duracaoMinutos) {
        return false;
    }

    @Override
    public List<Agendamento> listarTodos() {
        return new ArrayList<>();
    }

    @Override
    public void remover(AgendamentoId id) {
    }
}

class AgendamentoConflitoRepositorio extends AgendamentoMockRepositorio {
    @Override
    public boolean existeAgendamentoNoPeriodo(ProfissionalId profissionalId, LocalDateTime dataHora,
            int duracaoMinutos) {
        return true;
    }

    @Override
    public Agendamento salvar(Agendamento agendamento) {
        return null;
    }
    
}