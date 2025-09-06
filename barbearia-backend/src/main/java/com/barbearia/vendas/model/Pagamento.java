package com.barbearia.vendas.model;

import java.util.UUID;

import com.barbearia.common.enums.MeioPagamento;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Pagamento {
    private UUID id;
    private MeioPagamento meioPagamento;
}
