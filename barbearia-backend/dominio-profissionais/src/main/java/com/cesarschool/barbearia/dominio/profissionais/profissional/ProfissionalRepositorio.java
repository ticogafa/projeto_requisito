package com.cesarschool.barbearia.dominio.profissionais.profissional;

import java.util.List;
import java.util.Optional;

import com.cesarschool.barbearia.dominio.compartilhado.Cpf;

/**
 * Porta (interface) que define as operações de persistência para Profissional.
 * Implementada pela camada de infraestrutura.
 * Princípio de Inversão de Dependência: o domínio define o contrato.
 */
public interface ProfissionalRepositorio {
    
    /**
     * Salva um novo profissional ou atualiza um existente.
     * @param profissional o profissional a ser persistido
     * @return o profissional persistido com ID gerado (se novo)
     */
    Profissional salvar(Profissional profissional);
    
    /**
     * Busca um profissional pelo seu ID.
     * @param id o identificador do profissional
     * @return Optional contendo o profissional se encontrado
     */
    Optional<Profissional> buscarPorId(ProfissionalId id);
    
    /**
     * Busca um profissional pelo CPF.
     * @param cpf o CPF do profissional
     * @return Optional contendo o profissional se encontrado
     */
    Optional<Profissional> buscarPorCpf(Cpf cpf);
    
    /**
     * Lista todos os profissionais (ativos e inativos).
     * @return lista completa de profissionais
     */
    List<Profissional> listarTodos();
    
    /**
     * Remove um profissional do sistema (exclusão física).
     * @param id o identificador do profissional
     */
    void remover(ProfissionalId id);
    
    /**
     * Verifica se existe um profissional com o CPF informado.
     * @param cpf o CPF a verificar
     * @return true se existe, false caso contrário
     */
    boolean existePorCpf(Cpf cpf);
}
