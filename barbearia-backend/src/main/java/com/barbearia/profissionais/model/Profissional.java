package com.barbearia.profissionais.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

import com.barbearia.common.model.Cpf;
import com.barbearia.common.model.Email;
import com.barbearia.common.model.Telefone;

@Entity
@Data
@AllArgsConstructor
@Table(name = "profissional")
public class Profissional {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private Email email;

    @Column(nullable = false)
    private Cpf cpf;
    
    @Column(nullable = false)
    private Telefone telefone;

}