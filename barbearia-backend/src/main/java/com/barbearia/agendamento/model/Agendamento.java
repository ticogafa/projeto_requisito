package com.barbearia.agendamento.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

import com.barbearia.marketing.model.Cliente;
import com.barbearia.profissionais.model.Profissional;
import com.barbearia.profissionais.model.ServicoOferecido;

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
    private com.barbearia.common.enums.StatusAgendamento status;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;
    
    @ManyToOne
    @JoinColumn(name = "profissional_id", nullable = false)
    private Profissional profissional;

    @ManyToOne
    @JoinColumn(name = "servico_id", nullable = false)
    private ServicoOferecido servico;

    @Column(nullable = true)
    private String observacoes; // opcional
}