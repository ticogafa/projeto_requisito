package com.cesarschool.barbearia.dominio.profissionais.servico;

import com.cesarschool.barbearia.dominio.profissionais.profissional.ProfissionalId;

import java.util.List;
import java.util.Optional;

/**
 * Porta (interface) de persistência para ServicoOferecido.
 */
public interface ServicoOferecidoRepositorio {
    
    ServicoOferecido salvar(ServicoOferecido servico);
    
    Optional<ServicoOferecido> buscarPorId(ServicoOferecidoId id);
    
    List<ServicoOferecido> buscarPorProfissional(ProfissionalId profissionalId);
    
    List<ServicoOferecido> listarAtivos();
    
    void remover(ServicoOferecidoId id);
}
