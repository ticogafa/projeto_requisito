package com.barbearia.common.model;

import lombok.Data;

@Data
public class Telefone {

    private String value;
    public Telefone(String value){
        if(value.length() != 10 ){
            throw new IllegalArgumentException("Número de telefone inválido");
        }
        this.value=value;
    }
}
