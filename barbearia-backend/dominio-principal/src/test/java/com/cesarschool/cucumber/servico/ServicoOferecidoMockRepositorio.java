package com.cesarschool.cucumber.servico;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import com.cesarschool.barbearia.dominio.principal.profissional.ProfissionalId;
import com.cesarschool.barbearia.dominio.principal.servico.ServicoOferecido;
import com.cesarschool.barbearia.dominio.principal.servico.ServicoOferecidoId;
import com.cesarschool.barbearia.dominio.principal.servico.ServicoOferecidoRepositorio;

public class ServicoOferecidoMockRepositorio implements ServicoOferecidoRepositorio {

    private final Map<Integer, ServicoOferecido> dados = new HashMap<>();
    private final AtomicInteger sequenciadorId = new AtomicInteger(1); 

    @Override
    public ServicoOferecido salvar(ServicoOferecido servico) {
        if (servico.getId() == null) {
            Integer novoId = sequenciadorId.getAndIncrement();
            ServicoOferecidoId idGerado = new ServicoOferecidoId(novoId);
            
            ServicoOferecido servicoSalvo = new ServicoOferecido(
                idGerado,
                servico.getProfissionalId(),
                servico.getNome(),
                servico.getPreco(),
                servico.getDescricao(),
                servico.getDuracaoMinutos()
            );
            
            dados.put(novoId, servicoSalvo);
            return servicoSalvo;
        } else {
            dados.put(servico.getId().getValor(), servico);
            return servico;
        }
    }

    public ServicoOferecido buscarPorNome(String nome) {
        return dados.values().stream()
                .filter(s -> s.getNome().equalsIgnoreCase(nome))
                .findFirst()
                .orElse(null); 
    }
    
    @Override
    public List<ServicoOferecido> buscarPorProfissional(ProfissionalId profissionalId) {
        return listarTodos().stream()
                .filter(s -> s.getProfissionalId().equals(profissionalId))
                .toList();
    }
        
    @Override
    public ServicoOferecido buscarPorId(Integer id) {
        return dados.get(id); 
    }
    
    public Optional<ServicoOferecido> buscarPorIdOptional(Integer id) {
        return Optional.ofNullable(dados.get(id));
    }

    @Override
    public List<ServicoOferecido> listarTodos() {
        return new ArrayList<>(dados.values());
    }

    @Override
    public void remover(Integer id) {
        dados.remove(id);
    }

    public void limpar() {
        dados.clear();
        sequenciadorId.set(1);
    }
}