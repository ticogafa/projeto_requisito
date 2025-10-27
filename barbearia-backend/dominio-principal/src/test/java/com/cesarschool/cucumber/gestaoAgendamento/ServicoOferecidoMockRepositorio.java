package com.cesarschool.cucumber.gestaoAgendamento;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.cesarschool.barbearia.dominio.principal.profissional.ProfissionalId;
import com.cesarschool.barbearia.dominio.principal.servico.ServicoOferecido;
import com.cesarschool.barbearia.dominio.principal.servico.ServicoOferecidoId;
import com.cesarschool.barbearia.dominio.principal.servico.ServicoOferecidoRepositorio;

/**
 * Implementação mock do ServicoOferecidoRepositorio para testes.
 * Simula persistência em memória sem necessidade de banco de dados.
 */
public class ServicoOferecidoMockRepositorio implements ServicoOferecidoRepositorio {
    
    private final Map<Integer, ServicoOferecido> servicos = new HashMap<>();
    private final Map<Integer, Boolean> servicosAtivos = new HashMap<>();
    private final Map<String, List<String>> associacoes = new HashMap<>(); // servico -> profissionais
    
    @Override
    public ServicoOferecido salvar(ServicoOferecido servico) {
        servicos.put(servico.getId().getValor(), servico);
        return servico;
    }
    
    @Override
    public ServicoOferecido buscarPorId(Integer id) {
        ServicoOferecido servico = servicos.get(id);
        if (servico == null) {
            throw new IllegalArgumentException("Serviço não encontrado com ID: " + id);
        }
        return servico;
    }
    
    @Override
    public List<ServicoOferecido> listarTodos() {
        return new ArrayList<>(servicos.values());
    }
    
    @Override
    public void remover(Integer id) {
        servicos.remove(id);
        servicosAtivos.remove(id);
    }
    
    @Override
    public ServicoOferecido buscarPorNome(String nome) {
        return servicos.values().stream()
            .filter(s -> s.getNome().equalsIgnoreCase(nome))
            .findFirst()
            .orElse(null);
    }
    
    @Override
    public List<ServicoOferecido> buscarPorProfissional(ProfissionalId profissionalId) {
        return servicos.values().stream()
            .filter(s -> s.getProfissionalId() != null && s.getProfissionalId().equals(profissionalId))
            .collect(Collectors.toList());
    }
    
    @Override
    public List<ServicoOferecido> buscarAddOnDoServicoPrincipal(ServicoOferecidoId servicoPrincipalId) {
        return servicos.values().stream()
            .filter(s -> s.getServicoPrincipalId() != null && s.getServicoPrincipalId().equals(servicoPrincipalId))
            .collect(Collectors.toList());
    }
    
    @Override
    public void salvarAssociacao(String nomeServico, String nomeProfissional) {
        associacoes.computeIfAbsent(nomeServico, k -> new ArrayList<>()).add(nomeProfissional);
    }
    
    @Override
    public boolean estaQualificado(String nomeServico, String nomeProfissional) {
        List<String> profissionais = associacoes.get(nomeServico);
        return profissionais != null && profissionais.contains(nomeProfissional);
    }
    
    /**
     * Define se um serviço está ativo ou não.
     */
    public void definirAtivo(Integer servicoId, boolean ativo) {
        servicosAtivos.put(servicoId, ativo);
    }
    
    /**
     * Verifica se um serviço está ativo.
     */
    public boolean isAtivo(Integer servicoId) {
        return servicosAtivos.getOrDefault(servicoId, false);
    }
    
    /**
     * Limpa todos os dados do repositório.
     */
    public void limparDados() {
        servicos.clear();
        servicosAtivos.clear();
        associacoes.clear();
    }
}

