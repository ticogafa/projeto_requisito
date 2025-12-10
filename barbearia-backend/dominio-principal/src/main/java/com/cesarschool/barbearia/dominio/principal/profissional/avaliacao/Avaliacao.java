package com.cesarschool.barbearia.dominio.principal.profissional.avaliacao;

import java.time.LocalDateTime;

import com.cesarschool.barbearia.dominio.principal.profissional.ProfissionalId;

import lombok.Getter; // Se usar Lombok

// Opção 1: Usando Lombok (Recomendado se o projeto já usa)
@Getter 
public class Avaliacao {
    
    private AvaliacaoId id;
    private ProfissionalId profissionalId;
    private Nota nota;
    private LocalDateTime data;

    public Avaliacao(AvaliacaoId id, ProfissionalId profissionalId, Nota nota, LocalDateTime data) {
        this.id = id;
        this.profissionalId = profissionalId;
        this.nota = nota;
        this.data = data;
    }
}