package com.cesarschool.barbearia.dominio.profissionais.profissional;

import java.util.List;

import static org.apache.commons.lang3.Validate.notNull;

import com.cesarschool.barbearia.dominio.compartilhado.Cpf;

/**
 * Serviço de domínio contendo as regras de negócio de Profissional.
 * Coordena operações complexas e validações.
 */
public class ProfissionalServico {
    private final ProfissionalRepositorio repositorio;

    public ProfissionalServico(ProfissionalRepositorio repositorio) {
        notNull(repositorio, "O repositório não pode ser nulo");
        this.repositorio = repositorio;
    }

    /**
     * Registra um novo profissional no sistema.
     * Verifica se já existe profissional com mesmo CPF.
     */
    public Profissional registrarNovo(Profissional profissional) {
        notNull(profissional, "O profissional não pode ser nulo");
        
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
        notNull(id, "O ID não pode ser nulo");
        return repositorio.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException(
                    "Profissional não encontrado com ID: " + id
                ));
    }

    /**
     * Busca um profissional por CPF.
     */
    public Profissional buscarPorCpf(Cpf cpf) {
        notNull(cpf, "O CPF não pode ser nulo");
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
        notNull(profissional, "O profissional não pode ser nulo");
        notNull(profissional.getId(), "O ID do profissional não pode ser nulo");
        
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
        repositorio.remover(id);
    }
}
