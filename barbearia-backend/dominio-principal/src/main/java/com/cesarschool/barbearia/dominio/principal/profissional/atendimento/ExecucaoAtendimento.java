package com.cesarschool.barbearia.dominio.principal.profissional.atendimento;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

import com.cesarschool.barbearia.dominio.compartilhado.valueobjects.ExecucaoAtendimentoId;
import com.cesarschool.barbearia.dominio.principal.profissional.ProfissionalId;

public class ExecucaoAtendimento {

    private final ExecucaoAtendimentoId id;
    private final ProfissionalId profissionalId;
    private final BigDecimal valor; // valor do serviço
    private final LocalDateTime inicio;
    private LocalDateTime fim; // null até finalizar

    private ExecucaoAtendimento(ExecucaoAtendimentoId id,
                                ProfissionalId profissionalId,
                                BigDecimal valor,
                                LocalDateTime inicio,
                                LocalDateTime fim) {
        if (valor.compareTo(BigDecimal.ZERO) < 0) throw new IllegalArgumentException("Valor não pode ser negativo");
        this.id = Objects.requireNonNull(id, "id");
        this.profissionalId = Objects.requireNonNull(profissionalId, "profissionalId");
        this.valor = valor;
        this.inicio = Objects.requireNonNull(inicio, "inicio");
        this.fim = fim;
    }

    public static ExecucaoAtendimento iniciar(ProfissionalId profissionalId, double valor, LocalDateTime inicio) {
        // CORREÇÃO: Converter o double para BigDecimal na criação
        return new ExecucaoAtendimento(ExecucaoAtendimentoId.novo(), profissionalId, BigDecimal.valueOf(valor), inicio, null);
    }

    public void finalizar(LocalDateTime fim) {
        if (this.fim != null) throw new IllegalStateException("Atendimento já finalizado");
        if (fim.isBefore(inicio)) throw new IllegalArgumentException("Fim antes do início");
        this.fim = fim;
    }

    public boolean estaConcluido() { return fim != null; }

    public Duration duracao() {
        if (fim == null) return Duration.ZERO;
        return Duration.between(inicio, fim);
    }

    public ExecucaoAtendimentoId getId() { return id; }
    public ProfissionalId getProfissionalId() { return profissionalId; }
    // CORREÇÃO: O getter deve retornar BigDecimal
    public BigDecimal getValor() { return valor; }
    public LocalDateTime getInicio() { return inicio; }
    public LocalDateTime getFim() { return fim; }
}