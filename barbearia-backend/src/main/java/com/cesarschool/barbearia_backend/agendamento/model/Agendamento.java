package com.cesarschool.barbearia_backend.agendamento.model;

import java.time.LocalDateTime;


import com.cesarschool.barbearia_backend.common.enums.StatusAgendamento;
import com.cesarschool.barbearia_backend.marketing.model.Cliente;
import com.cesarschool.barbearia_backend.profissionais.model.Profissional;
import com.cesarschool.barbearia_backend.profissionais.model.ServicoOferecido;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@Table(name = "agendamento")
public class Agendamento {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

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
    
