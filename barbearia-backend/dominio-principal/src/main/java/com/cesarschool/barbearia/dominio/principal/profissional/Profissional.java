package com.cesarschool.barbearia.dominio.principal.profissional;

import static com.cesarschool.barbearia.dominio.compartilhado.utils.Validacoes.*;

import com.cesarschool.barbearia.dominio.compartilhado.valueobjects.Cpf;
import com.cesarschool.barbearia.dominio.compartilhado.valueobjects.Email;
import com.cesarschool.barbearia.dominio.compartilhado.valueobjects.Telefone;

/**
 * Entidade de domínio representando um Profissional da barbearia.
 * Sem anotações JPA - domínio puro.
 * Adaptado do código original mantendo as validações e estrutura.
 */
public final class Profissional {
    private ProfissionalId id;
    private String nome;
    private Email email;
    private Cpf cpf;
    private Telefone telefone;

    // Construtor para criação (sem ID)
    public Profissional(String nome, Email email, Cpf cpf, Telefone telefone) {
        setNome(nome);
        setEmail(email);
        setCpf(cpf);
        setTelefone(telefone);
    }

    public Profissional(ProfissionalId id, String nome, Email email, Cpf cpf, Telefone telefone) {
        this(nome, email, cpf, telefone);
        setId(id);
    }

    // Getters e Setters com validações
    
    public void setId(ProfissionalId id) {
        validarObjetoObrigatorio(id, "Id");
        this.id = id;
    }
    
    public void setNome(String nome) {
        validarStringObrigatoria(nome, "Nome");
        if (nome.length() < 3 || nome.length() > 100) {
            throw new IllegalArgumentException("Nome deve ter entre 3 e 100 caracteres");
        }
        this.nome = nome;
    }
    
    
    public void setEmail(Email email) {
        validarObjetoObrigatorio(email, "Email");
        this.email = email;
    }
    
    
    public void setCpf(Cpf cpf) {
        validarObjetoObrigatorio(cpf, "Cpf");
        this.cpf = cpf;
    }
    
    public void setTelefone(Telefone telefone) {
        validarObjetoObrigatorio(telefone, "Telefone");
        this.telefone = telefone;
    }
    
    // Métodos de negócio
    public void atualizarContato(Email novoEmail, Telefone novoTelefone) {
        setEmail(novoEmail);
        setTelefone(novoTelefone);
    }

    public ProfissionalId getId() { return id; }
    public String getNome() { return nome; }
    public Email getEmail() { return email; }
    public Cpf getCpf() { return cpf; }
    public Telefone getTelefone() { return telefone; }

}
