package com.cesarschool.barbearia.dominio.principal.profissional;

import java.util.List;

import static com.cesarschool.barbearia.dominio.compartilhado.utils.Validacoes.*;

import com.cesarschool.barbearia.dominio.compartilhado.valueobjects.Cpf;
import com.cesarschool.barbearia.dominio.compartilhado.valueobjects.Email;
import com.cesarschool.barbearia.dominio.compartilhado.valueobjects.Telefone;
import com.cesarschool.barbearia.dominio.principal.servico.ServicoOferecidoId;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public final class Profissional {
    private ProfissionalId id;
    private String nome;
    private Email email;
    private Cpf cpf;
    private Telefone telefone;
    private Agenda agenda = new Agenda();
    private List<ServicoOferecidoId> servicoOferecidoIds;
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
    public void setId(ProfissionalId id) {
        this.id = id;
    }
    
    public void setNome(String nome) {
        validarStringObrigatoria(nome, "Nome");
        if (nome.length() < 3 || nome.length() > 100) {
            throw new IllegalArgumentException("Nome deve ter entre 3 e 100 caracteres");
        }
        this.nome = nome;
    }
    
    // comentei aqui os Setters, pois se botei o @Setter do Lombok
    // queria deixar comentado pra caso eu tenha que botar alguma
    // validação tipo o do setNome

    // public void setEmail(Email email) {
    //     this.email = email;
    // }
    
    
    // public void setCpf(Cpf cpf) {
    //     this.cpf = cpf;
    // }
    
    // public void setTelefone(Telefone telefone) {
    //     this.telefone = telefone;
    // }

    // public void setAgenda(Agenda agenda) {
    //     this.agenda = agenda;
    // }

    // public void setSenioridade(Senioridade senioridade) {
    //     this.senioridade = senioridade;
    // }

    // public void setAtivo(boolean ativo) {
    //     this.ativo = ativo;
    // }

    // public void setMotivoInatividade(String motivoInatividade) {
    //     this.motivoInatividade = motivoInatividade;
    // }

    // public void setServicosOferecidosIds(List<ServicoOferecidoId> servicoOferecido) {
    //     this.servicoOferecido = servicoOferecido;
    // }
    
    public void atualizarContato(Email novoEmail, Telefone novoTelefone) {
        setEmail(novoEmail);
        setTelefone(novoTelefone);
    }

    public void desativar(String motivo) {
        validarObjetoObrigatorio(motivo, "Motivo");
        setAtivo(false);
        setMotivoInatividade(motivo);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Profissional that = (Profissional) obj;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}