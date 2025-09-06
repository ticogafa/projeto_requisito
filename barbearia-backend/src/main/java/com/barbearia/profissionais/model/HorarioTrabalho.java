package com.barbearia.profissionais.model;

import java.time.LocalTime;
import java.util.UUID;
import com.barbearia.common.enums.DiaSemana;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;

@Entity
@Data
@AllArgsConstructor
public class HorarioTrabalho {
    UUID id;
    private Profissional profissional; // FK Profissional(id), NOT NULL
    private DiaSemana diaSemana;// NOT NULL
    private LocalTime horaInicio;// NOT NULL
    private LocalTime horaFim;// NOT NULL
    private LocalTime inicioPausa; // opcional
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

