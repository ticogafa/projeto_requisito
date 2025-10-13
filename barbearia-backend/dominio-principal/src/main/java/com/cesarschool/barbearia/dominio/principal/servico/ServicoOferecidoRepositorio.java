package com.cesarschool.barbearia.dominio.principal.servico;

import java.util.List;

import com.cesarschool.barbearia.dominio.compartilhado.base.Repositorio;
import com.cesarschool.barbearia.dominio.principal.servico.ServicoOferecido;
import com.cesarschool.barbearia.dominio.principal.profissional.ProfissionalId;

/**
 * Porta (interface) de persistência para ServicoOferecido.
 */
public interface ServicoOferecidoRepositorio extends Repositorio<ServicoOferecido, Integer>{
    
    List<ServicoOferecido> buscarPorProfissional(ProfissionalId profissionalId);
    
}
