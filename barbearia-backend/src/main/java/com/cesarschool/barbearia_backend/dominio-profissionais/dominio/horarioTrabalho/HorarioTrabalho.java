package com.cesarschool.barbearia_backend.profissionais.model;

import java.time.LocalTime;

import com.cesarschool.barbearia_backend.common.enums.DiaSemana;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.NotBlank;
import org.apache.commons.lang3.Validate;

public class HorarioTrabalho {

    private Integer id;

    private Profissional profissional;
    
    private DiaSemana diaSemana;
    
    private LocalTime horaInicio;
    
    private LocalTime horaFim;
    
    private LocalTime inicioPausa; // opcional

    private LocalTime fimPausa; // opcional

    public Integer getId() {
        return id;
    }

    public HorarioTrabalho(
        Integer id,
        Profissional profissional,
        DiaSemana diaSemana,
        LocalTime horaInicio,
        LocalTime horaFim
    ) {
        setId(id);
        setProfissional(profissional);
        setDiaSemana(diaSemana);
        setHoraInicio(horaInicio);
        setHoraFim(horaFim);
    }

    public void setId(Integer id) {
        Validate.notNull(id, "O id não pode ser nulo");
        this.id = id;
    }

    public Profissional getProfissional() {
        return profissional;
    }

    public void setProfissional(Profissional profissional) {
        Validate.notNull(profissional, "O profissional não pode ser nulo");
        this.profissional = profissional;
    }

    public DiaSemana getDiaSemana() {
        return diaSemana;
    }

    public void setDiaSemana(DiaSemana diaSemana) {
        Validate.notNull(diaSemana, "O dia da semana não pode ser nulo");
        this.diaSemana = diaSemana;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(LocalTime horaInicio) {
        Validate.notNull(horaInicio, "A hora de início não pode ser nula");
        this.horaInicio = horaInicio;
    }

    public LocalTime getHoraFim() {
        return horaFim;
    }

    public void setHoraFim(LocalTime horaFim) {
        Validate.notNull(horaFim, "A hora de fim não pode ser nula");
        this.horaFim = horaFim;
    }

    public LocalTime getInicioPausa() {
        return inicioPausa;
    }

    public void setInicioPausa(LocalTime inicioPausa) {
        this.inicioPausa = inicioPausa;
    }

    public LocalTime getFimPausa() {
        return fimPausa;
    }

    public void setFimPausa(LocalTime fimPausa) {
        this.fimPausa = fimPausa;
    }
}
