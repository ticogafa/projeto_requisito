package com.cesarschool.barbearia.dominio.principal.profissional;

import java.util.List;
import java.util.stream.Collectors;

import com.cesarschool.barbearia.dominio.compartilhado.utils.Validacoes;
import com.cesarschool.barbearia.dominio.compartilhado.valueobjects.Cpf;

public class ProfissionalServico {
    private final ProfissionalRepositorio repositorio;

    public ProfissionalServico(ProfissionalRepositorio repositorio) {
        Validacoes.validarObjetoObrigatorio(repositorio, "O repositório");
        this.repositorio = repositorio;
    }

    public Profissional registrarNovo(Profissional profissional) {
        Validacoes.validarObjetoObrigatorio(profissional, "O profissional");
        
        if (repositorio.existePorCpf(profissional.getCpf())) {
            throw new IllegalArgumentException(
                "Já existe um profissional cadastrado com o CPF: " + profissional.getCpf().toString()
            );
        }
        
        return repositorio.salvar(profissional);
    }

    public Profissional registrarNovo(Profissional profissional, Senioridade senioridade) {
        Validacoes.validarObjetoObrigatorio(profissional, "O profissional");
        Validacoes.validarObjetoObrigatorio(senioridade, "Senioridade");

        if (repositorio.existePorCpf(profissional.getCpf())) {
            throw new IllegalArgumentException(
                "Já existe um profissional cadastrado com o CPF: " + profissional.getCpf().toString()
            );
        }
        
        profissional.setSenioridade(senioridade);
        
        return repositorio.salvar(profissional);
    }

    public Profissional buscarPorId(ProfissionalId id) {
        Validacoes.validarObjetoObrigatorio(id, "O ID");
        return repositorio.buscarPorId(id.getValor());
    }

    public Profissional buscarPorCpf(Cpf cpf) {
        Validacoes.validarObjetoObrigatorio(cpf, "O CPF");
        return repositorio.buscarPorCpf(cpf);
    }

    public List<Profissional> listarTodos() {
        return repositorio.listarTodos();
    }

    public Profissional atualizar(Profissional profissional) {
        Validacoes.validarObjetoObrigatorio(profissional, "O profissional");
        Validacoes.validarObjetoObrigatorio(profissional.getId(), "O ID do profissional");
        
        buscarPorId(profissional.getId());
        
        return repositorio.salvar(profissional);
    }

    public void remover(ProfissionalId id) {
        buscarPorId(id);
        repositorio.remover(id.getValor());
    }

    public Profissional desativar(ProfissionalId id, String motivo) {
        Validacoes.validarObjetoObrigatorio(id, "O ID do profissional");
        Validacoes.validarStringObrigatoria(motivo, "O motivo da inatividade");
        
        Profissional profissional = buscarPorId(id);
        profissional.desativar(motivo);
        
        return repositorio.salvar(profissional);
    }

    public List<Profissional> listarAtivos() {
        return repositorio.listarTodos().stream()
                .filter(Profissional::isAtivo)
                .collect(Collectors.toList());
    }
}