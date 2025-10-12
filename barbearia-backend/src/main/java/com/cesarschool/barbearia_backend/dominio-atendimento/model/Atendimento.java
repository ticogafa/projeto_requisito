package com.cesarschool.barbearia_backend.atendimento.model;

import com.cesarschool.barbearia_backend.atendimento.model.enums.StatusAtendimento;
import jakarta.persistence.*;
import lombok.Data;
import java.time.Duration;
import java.time.LocalDateTime;

@Entity
@Data // Anotação do Lombok para getters, setters, etc.
public class Atendimento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long agendamentoId;
    private LocalDateTime inicioAtendimento;
    private LocalDateTime fimAtendimento;
    private Duration duracao;

    @Enumerated(EnumType.STRING)
    private StatusAtendimento status;

    // Construtor protegido para o JPA
    protected Atendimento() {}

    private Atendimento(Long agendamentoId) {
        this.agendamentoId = agendamentoId;
        this.status = StatusAtendimento.PENDENTE;
    }

    // Método Fábrica para criar um atendimento de forma controlada
    public static Atendimento criarParaAgendamento(Long agendamentoId) {
        return new Atendimento(agendamentoId);
    }

    // Métodos de comportamento que protegem as regras de negócio
    public void iniciar() {
        if (this.status != StatusAtendimento.PENDENTE) {
            throw new IllegalStateException("Só é possível iniciar um atendimento que está pendente.");
        }
        this.inicioAtendimento = LocalDateTime.now();
        this.status = StatusAtendimento.EM_ANDAMENTO;
    }

    public void finalizar() {
        if (this.status != StatusAtendimento.EM_ANDAMENTO) {
            throw new IllegalStateException("Só é possível finalizar um atendimento que está em andamento.");
        }
        this.fimAtendimento = LocalDateTime.now();
        this.duracao = Duration.between(this.inicioAtendimento, this.fimAtendimento);
        this.status = StatusAtendimento.FINALIZADO;
    }
}