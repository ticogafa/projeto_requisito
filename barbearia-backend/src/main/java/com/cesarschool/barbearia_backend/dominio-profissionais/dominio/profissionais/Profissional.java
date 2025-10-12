package com.cesarschool.barbearia_backend.profissionais.dominio;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import org.apache.commons.lang3.Validate;

import com.cesarschool.barbearia_backend.common.model.Cpf;
import com.cesarschool.barbearia_backend.common.model.Email;
import com.cesarschool.barbearia_backend.common.model.Telefone;

public class Profissional {

    private ProfissionalId id;

    private String nome;

    private Email email;

    private Cpf cpf;
    
    private Telefone telefone;

    public ProfissionalId getId() {
        return id;
    }

    public void setId(ProfissionalId id) {
        Validate.notNull(nome, "Id não pode ser nulo");
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        Validate.notNull(nome, "Nome não pode ser nulo");
        Validate.notBlank(nome, "Nome não ser vazio");
        this.nome = nome;
    }

    public Email getEmail() {
        return email;
    }

    public void setEmail(Email email) {
        Validate.notNull(nome, "Email não pode ser nulo");
        Validate.notBlank(nome, "Email não ser vazio");
        this.email = email;
    }

    public Cpf getCpf() {
        return cpf;
    }

    public void setCpf(Cpf cpf) {
        Validate.notNull(nome, "Cpf não pode ser nulo");
        Validate.notBlank(nome, "Cpf não ser vazio");
        this.cpf = cpf;
    }

    public Telefone getTelefone() {
        return telefone;
    }

    public void setTelefone(Telefone telefone) {
        Validate.notNull(nome, "Telefone não pode ser nulo");
        Validate.notBlank(nome, "Telefone não ser vazio");
        this.telefone = telefone;
    }

    public Profissional(ProfissionalId id, String nome, Email email, Cpf cpf, Telefone telefone) {
        this(nome, email, cpf, telefone);
        setId(id);
    }

    public Profissional(String nome, Email email, Cpf cpf, Telefone telefone) {
        setNome(nome);
        setEmail(email);
        setCpf(cpf);
        setTelefone(telefone);
    }
}