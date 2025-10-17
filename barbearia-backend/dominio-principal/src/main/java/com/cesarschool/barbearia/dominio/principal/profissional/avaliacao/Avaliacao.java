package com.cesarschool.barbearia.dominio.principal.profissional.avaliacao;

import java.time.LocalDateTime;
import java.util.Objects;

import com.cesarschool.barbearia.dominio.compartilhado.valueobjects.ExecucaoAtendimentoId;
import com.cesarschool.barbearia.dominio.principal.profissional.ProfissionalId;

public class Avaliacao {
    private final ProfissionalId profissionalId;
    private final ExecucaoAtendimentoId execucaoId; // opcional: pode ser null
    private final int nota; // 1..5
    private final LocalDateTime quando;

    public Avaliacao(ProfissionalId profissionalId, ExecucaoAtendimentoId execucaoId, int nota, LocalDateTime quando) {
        this.profissionalId = Objects.requireNonNull(profissionalId);
        if (nota < 1 || nota > 5) throw new IllegalArgumentException("Nota deve ser entre 1 e 5");
        this.execucaoId = execucaoId;
        this.nota = nota;
        this.quando = Objects.requireNonNull(quando);
    }

    public ProfissionalId getProfissionalId() { return profissionalId; }
    public ExecucaoAtendimentoId getExecucaoId() { return execucaoId; }
    public int getNota() { return nota; }
    public LocalDateTime getQuando() { return quando; }
}