package com.cesarschool.barbearia.infraestrutura.eventos;

import com.cesarschool.barbearia.dominio.principal.profissional.Profissional;

import lombok.Getter;

@Getter
public class ProfissionalEvent {
    private final Object source; 
    private final Profissional profissional;
    private final TipoAcao tipoAcao;

    public ProfissionalEvent(Object source, Profissional profissional, TipoAcao tipoAcao) {
        this.source = source;
        this.profissional = profissional;
        this.tipoAcao = tipoAcao;
    }

    public enum TipoAcao {
        CRIADO,
        ATUALIZADO,
        REMOVIDO,
        DESLIGADO
    }
}