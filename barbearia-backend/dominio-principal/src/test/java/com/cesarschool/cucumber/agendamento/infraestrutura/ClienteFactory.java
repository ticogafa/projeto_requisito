package com.cesarschool.cucumber.agendamento.infraestrutura;

import com.cesarschool.barbearia.dominio.compartilhado.valueobjects.Cpf;
import com.cesarschool.barbearia.dominio.compartilhado.valueobjects.Email;
import com.cesarschool.barbearia.dominio.compartilhado.valueobjects.Telefone;
import com.cesarschool.barbearia.dominio.principal.cliente.Cliente;
import com.cesarschool.barbearia.dominio.principal.cliente.ClienteId;

public class ClienteFactory {

    public static Cliente criarPadrao() {
        return Cliente.builder()
                .id(new ClienteId(1))
                .nome("João Silva")
                .email(new Email("joao@email.com"))
                .cpf(new Cpf("53604042801"))
                .telefone(new Telefone("81999999999"))
                .build();
    }

    public static Cliente criarComId(Integer id) {
        Cliente cliente = criarPadrao();
        cliente.setId(new ClienteId(id));
        cliente.setNome(cliente.getNome() + id);
        return cliente;
    }
}
