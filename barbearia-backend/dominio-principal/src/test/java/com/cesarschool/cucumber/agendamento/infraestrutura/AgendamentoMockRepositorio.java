package com.cesarschool.cucumber.agendamento.infraestrutura;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.cesarschool.barbearia.dominio.principal.agendamento.Agendamento;
import com.cesarschool.barbearia.dominio.principal.agendamento.AgendamentoRepositorio;
import com.cesarschool.barbearia.dominio.principal.agendamento.StatusAgendamento;
import com.cesarschool.barbearia.dominio.principal.cliente.ClienteId;
import com.cesarschool.barbearia.dominio.principal.profissional.ProfissionalId;

public class AgendamentoMockRepositorio implements AgendamentoRepositorio {
    private Agendamento ultimoAgendamentoSalvo;

    @Override
    public Agendamento salvar(Agendamento agendamento) {
        // Simula salvamento retornando o próprio agendamento
        ultimoAgendamentoSalvo = agendamento;
        return agendamento;
    }

    @Override
    public List<Agendamento> buscarPorCliente(ClienteId clienteId) {
        return new ArrayList<>();
    }

    @Override
    public Agendamento buscarPorId(Integer id) {
        // Retorna o último agendamento salvo se existir
        return ultimoAgendamentoSalvo;
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
    public void remover(Integer id) {}
}


    