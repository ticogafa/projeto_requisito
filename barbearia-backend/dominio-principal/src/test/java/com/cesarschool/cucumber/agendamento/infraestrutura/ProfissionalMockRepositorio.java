package com.cesarschool.cucumber.agendamento.infraestrutura;

import java.time.LocalDateTime;
import java.util.List;

import com.cesarschool.barbearia.dominio.compartilhado.valueobjects.Cpf;
import com.cesarschool.barbearia.dominio.principal.profissional.Profissional;
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
    public void adicionarQualificacao(Integer profissionalId, Integer servicoId) {
        // Mock implementation - não faz nada
    }

    @Override
    public void removerQualificacao(Integer profissionalId, Integer servicoId) {
        // Mock implementation - não faz nada
    }

    @Override
    public boolean estaQualificado(Integer profissionalId, Integer servicoId) {
        // Mock implementation - sempre retorna true
        return true;
    }
}


