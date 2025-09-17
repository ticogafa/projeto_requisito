package com.cesarschool.barbearia_backend.profissionais.model;

import java.time.LocalTime;

import com.cesarschool.barbearia_backend.common.enums.DiaSemana;

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
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@Table(name = "horario_trabalho")
public class HorarioTrabalho {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Integer id;

    @NonNull
    @ManyToOne
    @JoinColumn(name="profissional_id")
    private Profissional profissional;
    
    @NonNull
    @Column(nullable = false)
    private DiaSemana diaSemana;
    
    @NonNull
    @Column(nullable = false)
    private LocalTime horaInicio;
    
    @NonNull
    @Column(nullable = false)
    private LocalTime horaFim;
    
    @Column(nullable = true)
    private LocalTime inicioPausa; // opcional

    @Column(nullable = true)
    private LocalTime fimPausa; // opcional

}

