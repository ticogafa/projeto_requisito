package com.barbearia.profissionais.model;

import java.time.LocalTime;
import java.util.UUID;
import com.barbearia.common.enums.DiaSemana;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;

@Entity
@Data
@AllArgsConstructor
@Table(name = "horario_trabalho")
public class HorarioTrabalho {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    UUID id;

    @ManyToOne
    @JoinColumn(name="profissional_id")
    private Profissional profissional;
    
    @Column(nullable = false)
    private DiaSemana diaSemana;
    
    @Column(nullable = false)
    private LocalTime horaInicio;
    
    @Column(nullable = false)
    private LocalTime horaFim;
    
    @Column(nullable = true)
    private LocalTime inicioPausa; // opcional

    @Column(nullable = true)
    private LocalTime fimPausa; // opcional

    public HorarioTrabalho(
        Profissional profissional,
        DiaSemana diaSemana,
        LocalTime horaInicio,
        LocalTime horaFim
    ) {
        this.profissional = profissional;
        this.diaSemana = diaSemana;
        this.horaInicio = horaInicio;
        this.horaFim = horaFim;
    }
}

