package com.cesarschool.barbearia.aplicacao.profissional;

import java.time.LocalTime;

import com.cesarschool.barbearia.dominio.compartilhado.enums.DiaSemana;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JornadaResumo {
    private DiaSemana diaSemana;
    private LocalTime horaInicio;
    private LocalTime horaFim;
    private LocalTime intervaloInicio;
    private LocalTime intervaloFim;
    private boolean ativo = true;
}
