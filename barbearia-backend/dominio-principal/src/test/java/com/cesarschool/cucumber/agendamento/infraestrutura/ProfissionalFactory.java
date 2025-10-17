package com.cesarschool.cucumber.agendamento.infraestrutura;

import com.cesarschool.barbearia.dominio.compartilhado.valueobjects.Cpf;
import com.cesarschool.barbearia.dominio.compartilhado.valueobjects.Email;
import com.cesarschool.barbearia.dominio.compartilhado.valueobjects.Telefone;
import com.cesarschool.barbearia.dominio.principal.profissional.Profissional;
import com.cesarschool.barbearia.dominio.principal.profissional.ProfissionalId;

public class ProfissionalFactory {
    public static Profissional criarPadrao() {
        return Profissional.builder()
                .id(new ProfissionalId(1))
                .nome("João Silva")
                .email(new Email("joao@email.com"))
                .cpf(new Cpf("53604042801"))
                .telefone(new Telefone("81999999999"))
                .build();
    }

    public static Profissional criarComId(Integer id) {
        Profissional padrao = ProfissionalFactory.criarPadrao();
        return Profissional.builder()
                .id(new ProfissionalId(id))
                .nome(padrao.getNome() + id)
                .email(padrao.getEmail())
                .cpf(padrao.getCpf())
                .telefone(padrao.getTelefone())
                .build();
    }
}
