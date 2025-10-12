package com.cesarschool.barbearia.dominio.profissionais.profissional;

import java.util.Objects;

import com.cesarschool.barbearia.dominio.compartilhado.Cpf;
import com.cesarschool.barbearia.dominio.compartilhado.Email;
import com.cesarschool.barbearia.dominio.compartilhado.Telefone;
import static com.cesarschool.barbearia.dominio.compartilhado.Validacoes.validarObjetoObrigatorio;
import static com.cesarschool.barbearia.dominio.compartilhado.Validacoes.validarStringObrigatoria;

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
    public ProfissionalId getId() {
        return id;
    }

    public void setId(ProfissionalId id) {
        validarObjetoObrigatorio(id, "Id");
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        validarStringObrigatoria(nome, "Nome");
        if (nome.length() < 3 || nome.length() > 100) {
            throw new IllegalArgumentException("Nome deve ter entre 3 e 100 caracteres");
        }
        this.nome = nome;
    }

    public Email getEmail() {
        return email;
    }

    public void setEmail(Email email) {
        validarObjetoObrigatorio(email, "Email");
        this.email = email;
    }

    public Cpf getCpf() {
        return cpf;
    }

    public void setCpf(Cpf cpf) {
        validarObjetoObrigatorio(cpf, "Cpf");
        this.cpf = cpf;
    }

    public Telefone getTelefone() {
        return telefone;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Profissional)) return false;
        Profissional that = (Profissional) o;
        return Objects.equals(id, that.id) && Objects.equals(cpf, that.cpf);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, cpf);
    }

    @Override
    public String toString() {
        return "Profissional{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", cpf=" + cpf +
                ", email=" + email +
                '}';
    }
}
