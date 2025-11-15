package com.cesarschool.barbearia.aplicacao.profissional;

import com.cesarschool.barbearia.dominio.compartilhado.valueobjects.Cpf;
import com.cesarschool.barbearia.dominio.compartilhado.valueobjects.Email;
import com.cesarschool.barbearia.dominio.compartilhado.valueobjects.Telefone;
import com.cesarschool.barbearia.dominio.principal.profissional.ProfissionalId;


public interface ProfissionalResumo {
    public ProfissionalId getId();

    public String getNome();

    public Email getEmail();

    public Cpf getCpf();
    
    public Telefone getTelefone();
}
