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

    @ManyToMany
    @JoinColumn(name = "cliente_id")
    Cliente cliente;
    
    @ManyToMany
    @JoinColumn(name = "profissional_id")
    Profissional profissional;

    @Column(nullable = false)
    ServicoOferecido servico;

    @Column(nullable = true)
    String observacoes; // opcional
}