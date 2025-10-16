package com.cesarschool.barbearia.dominio.principal.servico;

import java.math.BigDecimal;

import static com.cesarschool.barbearia.dominio.compartilhado.utils.Validacoes.validarInteiroPositivo;
import static com.cesarschool.barbearia.dominio.compartilhado.utils.Validacoes.validarObjetoObrigatorio;
import static com.cesarschool.barbearia.dominio.compartilhado.utils.Validacoes.validarStringObrigatoria;
import static com.cesarschool.barbearia.dominio.compartilhado.utils.Validacoes.validarValorPositivo;
import com.cesarschool.barbearia.dominio.principal.profissional.ProfissionalId;

/**
 * Entidade de domínio representando um serviço oferecido por um profissional.
 */
public final class ServicoOferecido {
    private Integer pontosFidelidade;
    private ServicoOferecidoId id;
    private ProfissionalId profissionalId;
    private String nome;
    private BigDecimal preco;
    private String descricao;
    private Integer duracaoMinutos;
    private ServicoOferecidoId servicoPrincipalId;
    private Integer intervaloLimpezaMinutos;
    private boolean ativo;
    private String motivoInatividade;

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
        this.ativo = true;
        this.motivoInatividade = null;
    }
    
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

    public ServicoOferecido(
            ServicoOferecidoId id,
            ProfissionalId profissionalId,
            String nome,
            BigDecimal preco,
            String descricao,
            Integer duracaoMinutos,
            ServicoOferecidoId servicoPrincipalId) { 
        this(id, profissionalId, nome, preco, descricao, duracaoMinutos);
        setServicoPrincipalId(servicoPrincipalId);
    }

    public ServicoOferecido(
        ServicoOferecidoId id,
        ProfissionalId profissionalId,
        String nome,
        BigDecimal preco,
        String descricao,
        Integer duracaoMinutos,
        ServicoOferecidoId servicoPrincipalId,
        Integer intervaloLimpezaMinutos) {
        this(id, profissionalId, nome, preco, descricao, duracaoMinutos, servicoPrincipalId);
        setIntervaloLimpezaMinutos(intervaloLimpezaMinutos);
    }

    public ServicoOferecido(
        ServicoOferecidoId id,
        ProfissionalId profissionalId,
        String nome,
        BigDecimal preco,
        String descricao,
        Integer duracaoMinutos,
        ServicoOferecidoId servicoPrincipalId,
        Integer intervaloLimpezaMinutos,
        boolean ativo,
        String motivoInatividade) {
        this(id, profissionalId, nome, preco, descricao, duracaoMinutos, servicoPrincipalId, intervaloLimpezaMinutos);
        this.ativo = ativo;
        this.motivoInatividade = motivoInatividade;
    }
    
    //Setters
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

    public void setServicoPrincipalId(ServicoOferecidoId servicoPrincipalId) {
        this.servicoPrincipalId = servicoPrincipalId;
    }

    public void setIntervaloLimpezaMinutos(Integer intervaloLimpezaMinutos) {
        validarObjetoObrigatorio(intervaloLimpezaMinutos, "Intervalo de limpeza");
        validarInteiroPositivo(intervaloLimpezaMinutos, "Intervalo de limpeza");
        if (intervaloLimpezaMinutos > 60) {
            throw new IllegalArgumentException("Intervalo de limpeza não pode exceder 60 minutos");
        }
        this.intervaloLimpezaMinutos = intervaloLimpezaMinutos;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public void setMotivoInatividade(String motivoInatividade) {
        this.motivoInatividade = motivoInatividade;
    }

    // Métodos de negócio
    public void atualizarPreco(BigDecimal novoPreco) {
        setPreco(novoPreco);
    }

    public void definirComoAddonDe(ServicoOferecidoId principalId) {
        validarObjetoObrigatorio(principalId, "ID do serviço principal");
        if (principalId.equals(this.id)) {
            throw new IllegalArgumentException("Um serviço não pode ser addon de si mesmo");
        }
        setServicoPrincipalId(principalId);
    }
    
    public void atualizarDuracao(Integer novaDuracao) {
        setDuracaoMinutos(novaDuracao);
    }

    public void definirIntervaloLimpeza(Integer intervaloMinutos) {
        setIntervaloLimpezaMinutos(intervaloMinutos);
    }

    public void desativar(String motivo) {
        validarStringObrigatoria(motivo, "Motivo da inatividade");
        setAtivo(false);
        setMotivoInatividade(motivo);
    }

    public void reativar() {
        setAtivo(true);
        setMotivoInatividade(null);
    }
    
    // Getters
    public ServicoOferecidoId getId() { return id; }
    public ProfissionalId getProfissionalId() { return profissionalId; }
    public String getNome() { return nome; }
    public BigDecimal getPreco() { return preco; }
    public String getDescricao() { return descricao; }
    public Integer getDuracaoMinutos() { return duracaoMinutos; }

    public ServicoOferecidoId getServicoPrincipalId() { return servicoPrincipalId; }
    public Integer getIntervaloLimpezaMinutos() { return intervaloLimpezaMinutos; }
    public boolean isAtivo() { return ativo; }
    public String getMotivoInatividade() { return motivoInatividade; }

    //falta fazer uma classe que atribua pontos de fidelidade ao cliente conforme o serviço prestado, tem que atribuir valor de pontos ao serviço
    //tambem pode ser uma propia tabela no banco de dados
    //pensar se vale mais a pena usar um banco de dados ou apenas um enum serve
}
