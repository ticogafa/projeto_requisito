package com.cesarschool.barbearia.infraestrutura.eventos;

import org.springframework.context.ApplicationEvent;
import com.cesarschool.barbearia.dominio.principal.profissional.Profissional;
import lombok.Getter;

@Getter
public class ProfissionalEvent extends ApplicationEvent {
    
    // Define os tipos possíveis de ação
    public enum TipoAcao {
        CRIADO,
        ATUALIZADO,
        DESLIGADO
    }

    private final Profissional profissional;
    private final TipoAcao tipoAcao;

    public ProfissionalEvent(Object source, Profissional profissional, TipoAcao tipoAcao) {
        super(source);
        this.profissional = profissional;
        this.tipoAcao = tipoAcao;
    }
}