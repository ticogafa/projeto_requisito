package com.cesarschool.barbearia.dominio.principal.servico.eventos;

import com.cesarschool.barbearia.dominio.principal.servico.ServicoOferecido;

import lombok.Getter;

@Getter
public class ServicoOferecidoEvent {
    private final Object source;
    private final ServicoOferecido servico;
    private final TipoAcao tipoAcao;

    public ServicoOferecidoEvent(Object source, ServicoOferecido servico, TipoAcao tipoAcao) {
        this.source = source;
        this.servico = servico;
        this.tipoAcao = tipoAcao;
    }

    public enum TipoAcao {
        CRIADO,
        ATUALIZADO,
        REMOVIDO,
        DESATIVADO
    }
}