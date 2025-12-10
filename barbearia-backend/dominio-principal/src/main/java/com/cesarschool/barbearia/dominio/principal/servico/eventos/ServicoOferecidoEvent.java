package com.cesarschool.barbearia.dominio.principal.servico.eventos;

import org.springframework.context.ApplicationEvent;
import com.cesarschool.barbearia.dominio.principal.servico.ServicoOferecido;
import lombok.Getter;

@Getter
public class ServicoOferecidoEvent extends ApplicationEvent {

    public enum TipoAcao {
        CRIADO,
        ATUALIZADO,
        DESATIVADO,
        REMOVIDO
    }

    private final ServicoOferecido servico;
    private final TipoAcao tipoAcao;

    public ServicoOferecidoEvent(Object source, ServicoOferecido servico, TipoAcao tipoAcao) {
        super(source);
        this.servico = servico;
        this.tipoAcao = tipoAcao;
    }
}