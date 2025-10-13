package com.cesarschool.barbearia.dominio.principal.produto;

import com.cesarschool.barbearia.dominio.compartilhado.valueobjects.ValueObjectId;

public class ProdutoId extends ValueObjectId<Integer>{
    public ProdutoId(Integer id){
        super(id);
    }
}