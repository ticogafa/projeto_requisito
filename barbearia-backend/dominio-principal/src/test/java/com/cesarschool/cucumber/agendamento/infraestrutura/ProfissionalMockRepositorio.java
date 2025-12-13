package com.cesarschool.cucumber.agendamento.infraestrutura;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.cesarschool.barbearia.aplicacao.profissional.JornadaResumo;
import com.cesarschool.barbearia.dominio.compartilhado.valueobjects.Cpf;
import com.cesarschool.barbearia.dominio.principal.profissional.Profissional;
import com.cesarschool.barbearia.dominio.principal.profissional.ProfissionalId;
import com.cesarschool.barbearia.dominio.principal.profissional.ProfissionalRepositorio;
import com.cesarschool.barbearia.dominio.principal.servico.ServicoOferecidoId;

public class ProfissionalMockRepositorio implements ProfissionalRepositorio {

    @Override
    public Profissional buscarPorCpf(Cpf cpf) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean existePorCpf(Cpf cpf) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Profissional buscarPrimeiroProfissionalDisponivel(LocalDateTime dataHora, int duracaoMinutos) {
        return ProfissionalFactory.criarPadrao();
    }

    @Override
    public Profissional salvar(Profissional entity) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Profissional buscarPorId(Integer id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Profissional> listarTodos() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void remover(Integer id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Profissional> buscarQualificadosParaServico(ServicoOferecidoId servicoId) {
        return List.of(ProfissionalFactory.criarPadrao());
    }

    @Override
    public List<Profissional> buscarDisponiveisNaDataHora(LocalDateTime dataHora, Integer duracaoMinutos) {
        return List.of(ProfissionalFactory.criarPadrao());
    }
    
    @Override
    public void adicionarQualificacao(ProfissionalId profissionalId, ServicoOferecidoId servicoId) {
    }

    @Override
    public void removerQualificacao(ProfissionalId profissionalId, ServicoOferecidoId servicoId) {
    }

    @Override
    public boolean estaQualificado(ProfissionalId profissionalId, ServicoOferecidoId servicoId) {
        return true;
    }

    @Override
    public boolean temAgendamentoAtivo(String nomeServico) {
        return false;
    }

    @Override
    public boolean possuiAssociacaoServico(String nomeProfissional, String nomeServico) {
        return true;
    }

    @Override
    public void removerAssociacaoServico(String nomeProfissional, String nomeServico) {
    }

    @Override
    public void atualizarJornadas(Integer profissionalId, List<JornadaResumo> jornadas) {
    }

    @Override
    public List<JornadaResumo> listarJornadas(Integer profissionalId) {
        return Collections.emptyList();
    }

    @Override
    public Optional<Profissional> buscarPorEmail(String email) {
        return Optional.empty();
    }
}