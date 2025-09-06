package com.barbearia.marketing.model;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "clientes")
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String nome;
    private String email;
    private int pontos;

    public Cliente(String nome, String email) {
        this.nome = nome;
        this.email = email;
        this.pontos = 0; 
    }

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