package com.cesarschool.barbearia.dominio.principal.servico;

import java.util.List;

import com.cesarschool.barbearia.dominio.compartilhado.base.Repositorio;
import com.cesarschool.barbearia.dominio.principal.profissional.ProfissionalId;

/**
 * Porta (interface) de persistência para ServicoOferecido.
 */
public interface ServicoOferecidoRepositorio extends Repositorio<ServicoOferecido, Integer>{
    
    List<ServicoOferecido> buscarPorProfissional(ProfissionalId profissionalId);
    
    ServicoOferecido buscarPorNome(String nome);

    List<ServicoOferecido> buscarAddOnDoServicoPrincipal(ServicoOferecidoId servicoPrincipalId);

    void salvarAssociacao(String nomeServico, String nomeProfissional);

    boolean estaQualificado(String nomeServico, String nomeProfissional);
}
