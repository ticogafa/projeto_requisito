package com.cesarschool.barbearia_backend.profissionais.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;



import com.cesarschool.barbearia_backend.common.model.Cpf;
import com.cesarschool.barbearia_backend.common.model.Email;
import com.cesarschool.barbearia_backend.common.model.Telefone;

@Entity
@Data
@RequiredArgsConstructor
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "profissional")
public class Profissional {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @NonNull
    @Column(nullable = false)
    private String nome;

    @NonNull
    @Column(nullable = false, unique = true)
    private Email email;

    @NonNull
    @Column(nullable = false, unique = true)
    private Cpf cpf;
    
    @NonNull
    @Column(nullable = false, unique = true)
    private Telefone telefone;

    public String getEmail(){
        return this.email.getValue();
    }

    public String getTelefone() {
        return this.telefone.getValue();
    }

    public String getCpf() {
        return this.cpf.getValue();
    }
}