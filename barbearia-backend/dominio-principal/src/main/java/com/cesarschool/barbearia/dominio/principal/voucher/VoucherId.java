package com.cesarschool.barbearia.dominio.principal.voucher;

import com.cesarschool.barbearia.dominio.compartilhado.valueobjects.ValueObjectId;

public class VoucherId extends ValueObjectId<Integer> {
    public VoucherId(Integer valor) {
        super(valor);
    }
}
