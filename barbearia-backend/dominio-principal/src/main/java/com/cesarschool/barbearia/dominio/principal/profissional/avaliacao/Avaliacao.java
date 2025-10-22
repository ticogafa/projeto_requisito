package com.cesarschool.barbearia.dominio.principal.profissional.avaliacao;

import java.time.LocalDateTime;
import java.util.Objects;

import com.cesarschool.barbearia.dominio.compartilhado.valueobjects.ExecucaoAtendimentoId;
import com.cesarschool.barbearia.dominio.principal.profissional.ProfissionalId;

public class Avaliacao {
    private final ProfissionalId profissionalId;
    private final ExecucaoAtendimentoId execucaoId; // opcional
    private final Nota nota;
    private final LocalDateTime quando;

    public Avaliacao(ProfissionalId profissionalId, ExecucaoAtendimentoId execucaoId, Nota nota, LocalDateTime quando) {
        this.profissionalId = Objects.requireNonNull(profissionalId, "profissionalId");
        this.execucaoId = execucaoId;
        this.nota = Objects.requireNonNull(nota, "nota");
        this.quando = Objects.requireNonNull(quando, "quando");
    }

    public ProfissionalId getProfissionalId() { return profissionalId; }
    public ExecucaoAtendimentoId getExecucaoId() { return execucaoId; }
    public Nota getNota() { return nota; }
    public LocalDateTime getQuando() { return quando; }
}