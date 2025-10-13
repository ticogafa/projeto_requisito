package com.cesarschool.barbearia.dominio.principal.cliente;

import java.util.Optional;

import com.cesarschool.barbearia.dominio.compartilhado.base.Repositorio;

public interface ClienteRepositorio extends Repositorio <Cliente, Integer>{
    Optional<Cliente> buscarPorNome(String nome);
    Optional<Cliente> buscarPorEmail(String email);
    Optional<Cliente> buscarPorTelefone(String telefone);
    Optional<Cliente> buscarPorCpf(String cpf);
}
