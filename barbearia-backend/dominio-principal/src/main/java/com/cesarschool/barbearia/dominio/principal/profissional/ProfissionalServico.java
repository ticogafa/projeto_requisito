package com.cesarschool.barbearia.dominio.principal.profissional;

import java.util.List;

import com.cesarschool.barbearia.dominio.compartilhado.utils.Validacoes;
import com.cesarschool.barbearia.dominio.compartilhado.valueobjects.Cpf;

/**
 * Serviço de domínio contendo as regras de negócio de Profissional.
 * Coordena operações complexas e validações.
 */
public class ProfissionalServico {
    private final ProfissionalRepositorio repositorio;

    public ProfissionalServico(ProfissionalRepositorio repositorio) {
        Validacoes.validarObjetoObrigatorio(repositorio, "O repositório");
        this.repositorio = repositorio;
    }

    /**
     * Registra um novo profissional no sistema.
     * Verifica se já existe profissional com mesmo CPF.
     */
    public Profissional registrarNovo(Profissional profissional) {
        Validacoes.validarObjetoObrigatorio(profissional, "O profissional");
        
        // Regra de negócio: não pode haver profissionais duplicados por CPF
        if (repositorio.existePorCpf(profissional.getCpf())) {
            throw new IllegalArgumentException(
                "Já existe um profissional cadastrado com o CPF: " + profissional.getCpf()
            );
        }
        
        return repositorio.salvar(profissional);
    }

    /**
     * Busca um profissional por ID.
     */
    public Profissional buscarPorId(ProfissionalId id) {
        Validacoes.validarObjetoObrigatorio(id, "O ID");
        return repositorio.buscarPorId(id.getValor())
                .orElseThrow(() -> new IllegalArgumentException(
                    "Profissional não encontrado com ID: " + id
                ));
    }

    /**
     * Busca um profissional por CPF.
     */
    public Profissional buscarPorCpf(Cpf cpf) {
        Validacoes.validarObjetoObrigatorio(cpf, "O CPF");
        return repositorio.buscarPorCpf(cpf)
                .orElseThrow(() -> new IllegalArgumentException(
                    "Profissional não encontrado com CPF: " + cpf
                ));
    }

    /**
     * Lista todos os profissionais.
     */
    public List<Profissional> listarTodos() {
        return repositorio.listarTodos();
    }

    /**
     * Atualiza os dados de um profissional existente.
     */
    public Profissional atualizar(Profissional profissional) {
        Validacoes.validarObjetoObrigatorio(profissional, "O profissional");
        Validacoes.validarObjetoObrigatorio(profissional.getId(), "O ID do profissional");
        
        // Verifica se existe
        buscarPorId(profissional.getId());
        
        return repositorio.salvar(profissional);
    }

    /**
     * Remove um profissional do sistema.
     */
    public void remover(ProfissionalId id) {
        // Verifica se existe antes de remover
        buscarPorId(id);
        repositorio.remover(id.getValor());
    }
}
