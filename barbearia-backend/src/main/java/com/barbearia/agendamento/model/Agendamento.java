package com.barbearia.agendamento.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.NonNull;

import java.time.LocalDateTime;
import java.util.UUID;

import com.barbearia.common.enums.StatusAgendamento;
import com.barbearia.marketing.model.Cliente;
import com.barbearia.profissionais.model.Profissional;
import com.barbearia.profissionais.model.ServicoOferecido;

@Entity
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@Table(name = "agendamento")
public class Agendamento {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NonNull
    @Column(nullable = false)
    private LocalDateTime dataHora;

    @NonNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusAgendamento status;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;
    
    @NonNull
    @ManyToOne
    @JoinColumn(name = "profissional_id", nullable = false)
    private Profissional profissional;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "servico_id", nullable = false)
    private ServicoOferecido servico;

    @Column(nullable = true)
    private String observacoes;
    
}
    
