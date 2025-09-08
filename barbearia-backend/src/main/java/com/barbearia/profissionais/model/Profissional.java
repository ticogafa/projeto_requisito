package com.barbearia.profissionais.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

import com.barbearia.common.model.Cpf;
import com.barbearia.common.model.Email;
import com.barbearia.common.model.Telefone;

@Entity
@Data
@RequiredArgsConstructor
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "profissional")
public class Profissional {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NonNull
    @Column(nullable = false)
    private String nome;

    @NonNull
    @Column(nullable = false)
    private Email email;

    @NonNull
    @Column(nullable = false)
    private Cpf cpf;
    
    @NonNull
    @Column(nullable = false)
    private Telefone telefone;

}