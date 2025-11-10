package com.cesarschool.barbearia.dominio.principal.servico;

import com.cesarschool.barbearia.dominio.compartilhado.base.Repositorio;

/**
 * Porta (interface) de persistência para ServicoOferecido.
 */
public interface ServicoOferecidoRepositorio extends Repositorio<ServicoOferecido, Integer>{
    
    ServicoOferecido buscarPorNome(String nome);

    void salvarAssociacao(String nomeServico, String nomeProfissional);

    boolean estaQualificado(String nomeServico, String nomeProfissional);
    
    /**
     * Verifica se um serviço está ativo.
     * @param servicoId ID do serviço
     * @return true se o serviço está ativo, false caso contrário
     */
    boolean isAtivo(Integer servicoId);
}
