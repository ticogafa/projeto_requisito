package com.cesarschool.barbearia.dominio.principal.servico;

import java.math.BigDecimal;

import static com.cesarschool.barbearia.dominio.compartilhado.utils.Validacoes.validarInteiroPositivo;
import static com.cesarschool.barbearia.dominio.compartilhado.utils.Validacoes.validarObjetoObrigatorio;
import static com.cesarschool.barbearia.dominio.compartilhado.utils.Validacoes.validarStringObrigatoria;
import static com.cesarschool.barbearia.dominio.compartilhado.utils.Validacoes.validarValorPositivo;
import com.cesarschool.barbearia.dominio.principal.profissional.ProfissionalId;

/**
 * Entidade de domínio representando um serviço oferecido por um profissional.
 * Adaptado do código original sem anotações JPA.
 */
public final class ServicoOferecido {
    private Integer pontosFidelidade;
    private ServicoOferecidoId id;
    private ProfissionalId profissionalId;
    private String nome;
    private BigDecimal preco;
    private String descricao;
    private Integer duracaoMinutos;

    // Construtor para criação (sem ID)
    public ServicoOferecido(
            ProfissionalId profissionalId,
            String nome,
            BigDecimal preco,
            String descricao,
            Integer duracaoMinutos) {
        setProfissionalId(profissionalId);
        setNome(nome);
        setPreco(preco);
        setDescricao(descricao);
        setDuracaoMinutos(duracaoMinutos);
    }

    // Construtor para reconstrução (com ID)
    public ServicoOferecido(
            ServicoOferecidoId id,
            ProfissionalId profissionalId,
            String nome,
            BigDecimal preco,
            String descricao,
            Integer duracaoMinutos) {
        this(profissionalId, nome, preco, descricao, duracaoMinutos);
        setId(id);
    }
    
    // Getters e Setters com validações
    
    public void setId(ServicoOferecidoId id) {
        validarObjetoObrigatorio(id, "ID");
        this.id = id;
    }
    
    
    public void setProfissionalId(ProfissionalId profissionalId) {
        validarObjetoObrigatorio(profissionalId, "ID do profissional");
        this.profissionalId = profissionalId;
    }
    
    
    public void setNome(String nome) {
        validarStringObrigatoria(nome, "Nome");
        if (nome.length() < 3 || nome.length() > 100) {
            throw new IllegalArgumentException("Nome deve ter entre 3 e 100 caracteres");
        }
        this.nome = nome;
    }
    
    
    public void setPreco(BigDecimal preco) {
        validarValorPositivo(preco, "Preço");
        this.preco = preco;
    }
    
    
    public void setDescricao(String descricao) {
        validarStringObrigatoria(descricao, "Descrição");
        if (descricao.length() > 255) {
            throw new IllegalArgumentException("Descrição deve ter no máximo 255 caracteres");
        }
        this.descricao = descricao;
    }

    public void setPontosFidelidade(Integer pontosFidelidade) {
        validarInteiroPositivo(pontosFidelidade, "Pontos de fidelidade");
        this.pontosFidelidade = pontosFidelidade;
    }
    
    
    public void setDuracaoMinutos(Integer duracaoMinutos) {
        validarObjetoObrigatorio(duracaoMinutos, "Duração");
        validarInteiroPositivo(duracaoMinutos, "Duração");
        if (duracaoMinutos > 480) {
            throw new IllegalArgumentException("Duração não pode exceder 480 minutos (8 horas)");
        }
        this.duracaoMinutos = duracaoMinutos;
    }
    
    // Métodos de negócio
    public void atualizarPreco(BigDecimal novoPreco) {
        setPreco(novoPreco);
    }
    
    public void atualizarDuracao(Integer novaDuracao) {
        setDuracaoMinutos(novaDuracao);
    }

    public ServicoOferecidoId getId() { return id; }
    public ProfissionalId getProfissionalId() { return profissionalId; }
    public String getNome() { return nome; }
    public BigDecimal getPreco() { return preco; }
    public String getDescricao() { return descricao; }
    public Integer getDuracaoMinutos() { return duracaoMinutos; }


    //falta fazer uma classe que atribua pontos de fidelidade ao cliente conforme o serviço prestado, tem que atribuir valor de pontos ao serviço
    //tambem pode ser uma propia tabela no banco de dados
    //pensar se vale mais a pena usar um banco de dados ou apenas um enum serve
}
