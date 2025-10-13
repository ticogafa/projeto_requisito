package com.cesarschool.barbearia.dominio.principal.cliente;

import com.cesarschool.barbearia.dominio.compartilhado.utils.Validacoes;
import com.cesarschool.barbearia.dominio.compartilhado.valueobjects.Cpf;
import com.cesarschool.barbearia.dominio.compartilhado.valueobjects.Email;
import com.cesarschool.barbearia.dominio.compartilhado.valueobjects.Telefone;


public final class Cliente {
    private ClienteId id;
    private String nome;
    private Email email;
    private Cpf cpf;
    private Telefone telefone;
    private int pontos;

    public Cliente(String nome, Email email, Cpf cpf, Telefone telefone) {
        Validacoes.validarObjetoObrigatorio(email, "Email");
        Validacoes.validarObjetoObrigatorio(cpf, "CPF");
        Validacoes.validarObjetoObrigatorio(telefone, "Telefone");
        Validacoes.validarStringObrigatoria(nome, "Nome");
        if (nome.length() < 3) {
            throw new IllegalArgumentException("Nome deve ter no mínimo 3 caracteres");
        }
        this.nome=nome;
        this.email=email;
        this.cpf=cpf;
        this.telefone=telefone;
        this.pontos = 0;
    }
    
    public Cliente(ClienteId id, String nome, Email email, Cpf cpf, Telefone telefone, int pontos) {
        this(nome, email, cpf, telefone);
        Validacoes.validarObjetoObrigatorio(id, "ID");
        Validacoes.validarInteiroNaoNegativo(pontos, "Pontos");
        this.setId(id);
        this.setPontos(pontos);
    }

    public void adicionarPontos(int pontosGanhos) {
        Validacoes.validarInteiroNaoNegativo(pontosGanhos, "Pontos a adicionar");
        this.pontos += pontosGanhos;
    }

    public void usarPontos(int pontosGastos) {
        Validacoes.validarInteiroNaoNegativo(pontosGastos, "Pontos a usar");
        if (this.pontos < pontosGastos) {
            throw new IllegalStateException("Cliente não possui pontos suficientes");
        }
        this.pontos -= pontosGastos;
    }

    public boolean possuiPontosSuficientes(int pontosNecessarios) {
        Validacoes.validarInteiroNaoNegativo(pontosNecessarios, "Pontos necessários");
        return this.pontos >= pontosNecessarios;
    }

    public void setNome(String nome) {
        Validacoes.validarStringObrigatoria(nome, "Nome");
        if (nome.length() < 3) {
            throw new IllegalArgumentException("Nome deve ter no mínimo 3 caracteres");
        }
        this.nome = nome;
    }

    public void setEmail(Email email) {
        Validacoes.validarObjetoObrigatorio(email, "Email");
        this.email = email;
    }

    public void setCpf(Cpf cpf) {
        Validacoes.validarObjetoObrigatorio(cpf, "CPF");
        this.cpf = cpf;
    }

    public void setId(ClienteId id) {
        this.id = id;
    }
  
    public void setTelefone(Telefone telefone) {
        Validacoes.validarObjetoObrigatorio(telefone, "Telefone");
        this.telefone = telefone;
    }

    public void setPontos(int pontos) {
        Validacoes.validarInteiroNaoNegativo(pontos, "Pontos");
        this.pontos = pontos;
    }

    public String getNome() { return nome; }
    public Email getEmail() { return email; }
    public Cpf getCpf() { return cpf; }
    public Telefone getTelefone() { return telefone; }
    public int getPontos() { return pontos; }
    public ClienteId getId() { return id; }
}
