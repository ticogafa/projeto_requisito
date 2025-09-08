package com.barbearia.marketing.model;
import java.util.UUID;

import com.barbearia.common.model.Cpf;
import com.barbearia.common.model.Email;
import com.barbearia.common.model.Telefone;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@Table(name = "cliente")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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

    @Column(nullable = true)
    private int pontos = 0;

    public void adicionarPontos(int pontosGanhos) {
        if (pontosGanhos < 0) {
            throw new IllegalArgumentException("A quantidade de pontos a adicionar não pode ser negativa.");
        }
        this.pontos += pontosGanhos;
    }

    public void usarPontos(int pontosGastos) {
        if (pontosGastos < 0) {
            throw new IllegalArgumentException("A quantidade de pontos a usar não pode ser negativa.");
        }
        if (this.pontos < pontosGastos) {
            throw new IllegalStateException("Cliente não possui pontos suficientes.");
        }
        this.pontos -= pontosGastos;
    }

}