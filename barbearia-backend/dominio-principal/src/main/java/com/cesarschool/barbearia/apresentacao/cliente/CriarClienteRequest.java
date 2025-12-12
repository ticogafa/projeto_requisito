package com.cesarschool.barbearia.apresentacao.cliente;

import lombok.Data;

@Data
public class CriarClienteRequest {
    private String nome;
    private String email;
    private String cpf;
    private String telefone;
}
