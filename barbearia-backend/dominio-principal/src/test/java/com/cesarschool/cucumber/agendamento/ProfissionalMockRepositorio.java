package com.cesarschool.cucumber.agendamento;

import java.time.LocalDateTime;
import java.util.List;

import com.cesarschool.barbearia.dominio.compartilhado.valueobjects.Cpf;
import com.cesarschool.barbearia.dominio.principal.profissional.Profissional;
import com.cesarschool.barbearia.dominio.principal.profissional.ProfissionalRepositorio;
import com.cesarschool.cucumber.agendamento.infraestrutura.ProfissionalFactory;

public class ProfissionalMockRepositorio implements ProfissionalRepositorio{

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

}

class ProfissionalSemDisponivelRepositorio extends ProfissionalMockRepositorio {
    @Override
    public Profissional buscarPrimeiroProfissionalDisponivel(LocalDateTime dataHora, int duracaoMinutos) {
        throw new IllegalStateException("Não há profissionais disponíveis para o horário solicitado");
    }
}

