package com.cesarschool.barbearia.dominio.principal.profissional;

import static com.cesarschool.barbearia.dominio.compartilhado.utils.Validacoes.validarObjetoObrigatorio;
import static com.cesarschool.barbearia.dominio.compartilhado.utils.Validacoes.validarStringObrigatoria;
import com.cesarschool.barbearia.dominio.compartilhado.valueobjects.Cpf;
import com.cesarschool.barbearia.dominio.compartilhado.valueobjects.Email;
import com.cesarschool.barbearia.dominio.compartilhado.valueobjects.Telefone;

import lombok.Builder;

public final class Profissional {
    private ProfissionalId id;
    private String nome;
    private Email email;
    private Cpf cpf;
    private Telefone telefone;
    private Agenda agenda; 
    private Senioridade senioridade; 
    private boolean ativo; 
    private String motivoInatividade; 

    @Builder
    public Profissional(String nome, Email email, Cpf cpf, Telefone telefone) {
        setNome(nome);
        setEmail(email);
        setCpf(cpf);
        setTelefone(telefone);
        this.agenda = new Agenda();
        this.senioridade = Senioridade.JUNIOR;
        this.ativo = true;
        this.motivoInatividade = null;
    }

    @Builder
    public Profissional(ProfissionalId id, String nome, Email email, Cpf cpf, Telefone telefone) {
        this(nome, email, cpf, telefone);
        setId(id);
    }

    public Profissional(ProfissionalId id, String nome, Email email, Cpf cpf, Telefone telefone, Agenda agenda) {
        this(id, nome, email, cpf, telefone);
        this.agenda = agenda; 
    }

    public Profissional(ProfissionalId id, String nome, Email email, Cpf cpf, Telefone telefone, Agenda agenda, Senioridade senioridade, boolean ativo, String motivoInatividade) {
        this(id, nome, email, cpf, telefone, agenda); 
        setSenioridade(senioridade);
        this.ativo = ativo;
        this.motivoInatividade = motivoInatividade;
    }
    
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

    public void setSenioridade(Senioridade senioridade) {
        validarObjetoObrigatorio(senioridade, "Senioridade");
        this.senioridade = senioridade;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public void setMotivoInatividade(String motivoInatividade) {
        this.motivoInatividade = motivoInatividade;
    }
    
    public void atualizarContato(Email novoEmail, Telefone novoTelefone) {
        setEmail(novoEmail);
        setTelefone(novoTelefone);
    }

    public void desativar(String motivo) {
        validarObjetoObrigatorio(motivo, "Motivo");
        setAtivo(false);
        setMotivoInatividade(motivo);
    }

    public ProfissionalId getId() { return id; }
    public String getNome() { return nome; }
    public Email getEmail() { return email; }
    public Cpf getCpf() { return cpf; }
    public Telefone getTelefone() { return telefone; }
    public Agenda getAgenda() { return agenda; }
    public Senioridade getSenioridade() { return senioridade; }
    public boolean isAtivo() { return ativo; }
    public String getMotivoInatividade() { return motivoInatividade; }

}
