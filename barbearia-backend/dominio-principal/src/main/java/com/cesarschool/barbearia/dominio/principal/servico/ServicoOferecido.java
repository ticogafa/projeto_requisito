package com.cesarschool.barbearia.dominio.principal.servico;

import java.math.BigDecimal;

import static com.cesarschool.barbearia.dominio.compartilhado.utils.Validacoes.validarInteiroPositivo;
import static com.cesarschool.barbearia.dominio.compartilhado.utils.Validacoes.validarObjetoObrigatorio;
import static com.cesarschool.barbearia.dominio.compartilhado.utils.Validacoes.validarStringObrigatoria;
import static com.cesarschool.barbearia.dominio.compartilhado.utils.Validacoes.validarValorPositivo;

/**
 * Entidade de domínio representando um serviço oferecido pela barbearia.
 */
public final class ServicoOferecido {
    private ServicoOferecidoId id;
    private String nome;
    private BigDecimal preco;
    private String descricao;
    private Integer duracaoMinutos;
    private boolean ativo;
    private String motivoInatividade;    

    public ServicoOferecido(
            String nome,
            BigDecimal preco,
            String descricao,
            Integer duracaoMinutos) {
        setNome(nome);
        setPreco(preco);
        setDescricao(descricao);
        setDuracaoMinutos(duracaoMinutos);
        this.ativo = true;
        this.motivoInatividade = null;
    }
    
    public ServicoOferecido(
            ServicoOferecidoId id,
            String nome,
            BigDecimal preco,
            String descricao,
            Integer duracaoMinutos) {
        this(nome, preco, descricao, duracaoMinutos);
        setId(id);
    }
    

    public void setId(ServicoOferecidoId id) {
        validarObjetoObrigatorio(id, "ID");
        this.id = id;
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

    public void setDuracaoMinutos(Integer duracaoMinutos) {
        validarObjetoObrigatorio(duracaoMinutos, "Duração");
        validarInteiroPositivo(duracaoMinutos, "Duração");
        if (duracaoMinutos > 480) {
            throw new IllegalArgumentException("Duração não pode exceder 480 minutos (8 horas)");
        }
        this.duracaoMinutos = duracaoMinutos;
    }

    
    public void atualizarPreco(BigDecimal novoPreco) {
        setPreco(novoPreco);
    }
    
    public void atualizarDuracao(Integer novaDuracao) {
        setDuracaoMinutos(novaDuracao);
    }
    
    public void desativar(String motivo) {
        validarStringObrigatoria(motivo, "Motivo da inatividade");
        this.ativo = false;
        this.motivoInatividade = motivo;
    }
    
    public void reativar() {
        this.ativo = true;
        this.motivoInatividade = null;
    }

    public ServicoOferecidoId getId() { return id; }
    public String getNome() { return nome; }
    public BigDecimal getPreco() { return preco; }
    public String getDescricao() { return descricao; }
    public Integer getDuracaoMinutos() { return duracaoMinutos; }
    public boolean isAtivo() { return ativo; }
    public String getMotivoInatividade() { return motivoInatividade; }
}