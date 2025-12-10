package com.cesarschool.cucumber.gestaoProfissionais;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import com.cesarschool.barbearia.dominio.compartilhado.valueobjects.Cpf;
import com.cesarschool.barbearia.dominio.principal.profissional.Profissional;
import com.cesarschool.barbearia.dominio.principal.profissional.ProfissionalId;
import com.cesarschool.barbearia.dominio.principal.profissional.ProfissionalRepositorio;
import com.cesarschool.barbearia.dominio.principal.servico.ServicoOferecidoId;

/**
 * Mock (Fake) Repositório para a entidade Profissional, utilizando memória (HashMap)
 */
public class ProfissionalMockRepositorio implements ProfissionalRepositorio {

    private final Map<Integer, Profissional> dados = new HashMap<>();
    private final AtomicInteger sequenciadorId = new AtomicInteger(1); 
    private final Map<String, Profissional> cachePorCpf = new HashMap<>();
    
    private final Map<String, List<String>> associacoesServico = new HashMap<>(); 
    private final Map<String, Boolean> servicoTemAgendamentoAtivo = new HashMap<>(); 

    @Override
    public Profissional salvar(Profissional profissional) {
        if (profissional.getId() == null) {
            Integer novoId = sequenciadorId.getAndIncrement();
            ProfissionalId idGerado = new ProfissionalId(novoId);
            
            Profissional profissionalSalvo = new Profissional(
                idGerado,
                profissional.getNome(),
                profissional.getEmail(),
                profissional.getCpf(),
                profissional.getTelefone(),
                profissional.getAgenda(),
                profissional.getServicoOferecidoIds() != null ? profissional.getServicoOferecidoIds() : new ArrayList<>(),
                profissional.getSenioridade(),
                profissional.isAtivo(),
                profissional.getMotivoInatividade()
            );
            
            dados.put(novoId, profissionalSalvo);
            cachePorCpf.put(profissional.getCpf().toString(), profissionalSalvo);
            return profissionalSalvo;
        } else {
            dados.put(profissional.getId().getValor(), profissional);
            cachePorCpf.put(profissional.getCpf().toString(), profissional);
            return profissional;
        }
    }
    
    @Override
    public Profissional buscarPrimeiroProfissionalDisponivel(LocalDateTime dataHora, int duracaoServicoMinutos) {
        return dados.values().stream().findFirst().orElse(null);
    }

    @Override
    public Profissional buscarPorCpf(Cpf cpf) {
        return cachePorCpf.get(cpf.toString());
    }

    @Override
    public Profissional buscarPorId(Integer id) {
        return dados.get(id);
    }

    @Override
    public boolean existePorCpf(Cpf cpf) {
        return cachePorCpf.containsKey(cpf.toString());
    }

    @Override
    public List<Profissional> listarTodos() {
        return new ArrayList<>(dados.values());
    }

    @Override
    public void remover(Integer id) {
        Profissional p = dados.remove(id);
        if (p != null) {
            cachePorCpf.remove(p.getCpf().toString());
        }
    }
    
    @Override
    public List<Profissional> buscarQualificadosParaServico(ServicoOferecidoId servicoId) {
        return listarTodos();
    }

    @Override
    public List<Profissional> buscarDisponiveisNaDataHora(LocalDateTime dataHora, Integer duracaoMinutos) {
        return listarTodos();
    }

    private final Map<Integer, List<Integer>> qualificacoes = new HashMap<>();

    @Override
    public void adicionarQualificacao(Integer profissionalId, Integer servicoId) {
        qualificacoes.computeIfAbsent(profissionalId, k -> new ArrayList<>()).add(servicoId);
    }

    @Override
    public void removerQualificacao(Integer profissionalId, Integer servicoId) {
        List<Integer> servicos = qualificacoes.get(profissionalId);
        if (servicos != null) {
            servicos.remove(servicoId);
        }
    }

    @Override
    public boolean estaQualificado(Integer profissionalId, Integer servicoId) {
        List<Integer> servicos = qualificacoes.get(profissionalId);
        return servicos != null && servicos.contains(servicoId);
    }
    
    public void salvarAssociacaoServico(String nomeProfissional, String nomeServico) {
        associacoesServico.computeIfAbsent(nomeProfissional, k -> new ArrayList<>()).add(nomeServico);
    }

    public boolean possuiAssociacaoServico(String nomeProfissional, String nomeServico) {
        List<String> servicos = associacoesServico.get(nomeProfissional);
        return servicos != null && servicos.contains(nomeServico);
    }

    public void removerAssociacaoServico(String nomeProfissional, String nomeServico) {
        List<String> servicos = associacoesServico.get(nomeProfissional);
        if (servicos != null) {
            servicos.remove(nomeServico);
        }
    }

    public void simularAgendamentoAtivo(String nomeServico, boolean ativo) {
        servicoTemAgendamentoAtivo.put(nomeServico, ativo);
    }

    public boolean temAgendamentoAtivo(String nomeServico) {
        return servicoTemAgendamentoAtivo.getOrDefault(nomeServico, false);
    }

    public void limpar() {
        dados.clear();
        cachePorCpf.clear();
        sequenciadorId.set(1);
        associacoesServico.clear();
        servicoTemAgendamentoAtivo.clear();
    }
}