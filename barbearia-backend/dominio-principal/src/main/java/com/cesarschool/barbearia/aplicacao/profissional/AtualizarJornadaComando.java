package com.cesarschool.barbearia.aplicacao.profissional;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AtualizarJornadaComando {
    private Integer profissionalId;
    private List<JornadaResumo> novasJornadas;
}
