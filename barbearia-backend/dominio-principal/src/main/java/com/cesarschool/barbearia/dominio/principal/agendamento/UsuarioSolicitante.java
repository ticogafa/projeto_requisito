package com.cesarschool.barbearia.dominio.principal.agendamento;

import com.cesarschool.barbearia.dominio.compartilhado.enums.TipoUsuario;
import com.cesarschool.barbearia.dominio.compartilhado.utils.Validacoes;
import com.cesarschool.barbearia.dominio.compartilhado.valueobjects.ValueObjectId;

import lombok.Getter;

@Getter
public final class UsuarioSolicitante {
  TipoUsuario tipo;
  ValueObjectId<?> referenciaId;

    public UsuarioSolicitante(TipoUsuario tipo, ValueObjectId<?> referenciaId) {
        Validacoes.validarObjetoObrigatorio(referenciaId, "Referencia do Usuário Solicitante");
        Validacoes.validarObjetoObrigatorio(tipo, "Tipo do Usuário Solicitante");
        this.tipo = tipo;
        this.referenciaId = referenciaId;
    }

    public boolean isCliente() {
        return this.tipo == TipoUsuario.CLIENTE;
    }
    public boolean isProfissional() {
        return this.tipo == TipoUsuario.PROFISSIONAL;
    }
    public boolean isAdmin() {
        return this.tipo == TipoUsuario.ADMIN;
    }
}
 
