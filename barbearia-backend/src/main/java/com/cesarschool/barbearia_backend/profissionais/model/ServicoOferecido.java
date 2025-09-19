package com.cesarschool.barbearia_backend.profissionais.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;


@Entity
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor
@Table(name = "servico")
public class ServicoOferecido {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @NonNull
    @Column(nullable = false, unique = true, length = 100)
    private String nome;

    @NonNull
    @Column(nullable = false)
    private BigDecimal preco;

    @NonNull
    @Column(nullable = false, length = 255)
    private String descricao;

    @Column(nullable = false)
    private int duracaoMinutos;

}

