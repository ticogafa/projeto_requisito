package com.cesarschool.barbearia.dominio.principal.horariotrabalho;

import static com.cesarschool.barbearia.dominio.compartilhado.utils.Validacoes.*;

import java.time.LocalTime;

import com.cesarschool.barbearia.dominio.compartilhado.enums.DiaSemana;
import com.cesarschool.barbearia.dominio.principal.profissional.ProfissionalId;

/**
 * Entidade de domínio representando o horário de trabalho de um profissional.
 * Define quando um profissional está disponível para atendimento.
 * Adaptado do código original com suporte a pausas.
 */
public final class HorarioTrabalho {
    private HorarioTrabalhoId id;
    private ProfissionalId profissionalId;
    private DiaSemana diaSemana;
    private LocalTime horaInicio;
    private LocalTime horaFim;
    private LocalTime inicioPausa; // opcional
    private LocalTime fimPausa; // opcional

    // Construtor para criação (sem ID)
    public HorarioTrabalho(
            ProfissionalId profissionalId, 
            DiaSemana diaSemana,
            LocalTime horaInicio, 
            LocalTime horaFim) {
        setProfissionalId(profissionalId);
        setDiaSemana(diaSemana);
        setHorarios(horaInicio, horaFim);
    }

    // Construtor para reconstrução (com ID)
    public HorarioTrabalho(
            HorarioTrabalhoId id,
            ProfissionalId profissionalId,
            DiaSemana diaSemana,
            LocalTime horaInicio,
            LocalTime horaFim) {
        this(profissionalId, diaSemana, horaInicio, horaFim);
        setId(id);
    }
    
    // Métodos de negócio
    public void definirPausa(LocalTime inicioPausa, LocalTime fimPausa) {
        validarObjetoObrigatorio(inicioPausa, "Início da pausa");
        validarObjetoObrigatorio(fimPausa, "Fim da pausa");
        
        if (!inicioPausa.isAfter(horaInicio) || !inicioPausa.isBefore(horaFim)) {
            throw new IllegalArgumentException("Pausa deve estar dentro do horário de trabalho");
        }
        if (!fimPausa.isAfter(inicioPausa) || !fimPausa.isBefore(horaFim)) {
            throw new IllegalArgumentException("Fim da pausa deve ser após o início e antes do fim do expediente");
        }
        
        this.inicioPausa = inicioPausa;
        this.fimPausa = fimPausa;
    }
    
    public void removerPausa() {
        this.inicioPausa = null;
        this.fimPausa = null;
    }
    
    private void setHorarios(LocalTime horaInicio, LocalTime horaFim) {
        validarObjetoObrigatorio(horaInicio, "Hora de início");
        validarObjetoObrigatorio(horaFim, "Hora de fim");
        
        if (!horaInicio.isBefore(horaFim)) {
            throw new IllegalArgumentException("Hora de início deve ser anterior à hora de fim");
        }
        
        this.horaInicio = horaInicio;
        this.horaFim = horaFim;
    }

    // Getters
    public HorarioTrabalhoId getId() { return id; }
    public ProfissionalId getProfissionalId() { return profissionalId; }
    public DiaSemana getDiaSemana() { return diaSemana; }
    public LocalTime getHoraInicio() { return horaInicio; }
    public LocalTime getHoraFim() { return horaFim; }
    public LocalTime getInicioPausa() { return inicioPausa; }
    public LocalTime getFimPausa() { return fimPausa; }

    // Setters com validações
    public void setId(HorarioTrabalhoId id) {
        validarObjetoObrigatorio(id, "ID");
        this.id = id;
    }

    public void setProfissionalId(ProfissionalId profissionalId) {
        validarObjetoObrigatorio(profissionalId, "ID do profissional");
        this.profissionalId = profissionalId;
    }

    public void setDiaSemana(DiaSemana diaSemana) {
        validarObjetoObrigatorio(diaSemana, "Dia da semana");
        this.diaSemana = diaSemana;
    }

    public void setHoraInicio(LocalTime horaInicio) {
        setHorarios(horaInicio, this.horaFim);
    }

    public void setHoraFim(LocalTime horaFim) {
        setHorarios(this.horaInicio, horaFim);
    }
    
}
