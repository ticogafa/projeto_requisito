package com.cesarschool.barbearia.dominio.vendas.venda;

import com.cesarschool.barbearia.dominio.compartilhado.valueobjects.ValueObjectId;

public class VendaId extends ValueObjectId<Integer>{
    public VendaId(Integer id){
        super(id);
    }
}
