package com.barbearia.agendamento.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

import com.barbearia.agendamento.enums.StatusAgendamento;
import com.barbearia.marketing.model.Cliente;
import com.barbearia.profissionais.model.Profissional;
import com.barbearia.profissionais.model.Servico;

@Entity
@Data
@NoArgsConstructor
@Table(name = "agendamento")
public class Agendamento {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private LocalDateTime dataHora;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusAgendamento status;

    @Column(nullable = false)
    Cliente cliente; // FK Cliente(id), NOT NULL
    
    @Column(nullable = false)
    Profissional profissional; // FK Profissional(id), NOT NULL
    Servico servico; // FK Servico(id), NOT NULL
    String observacoes; // opcional
}