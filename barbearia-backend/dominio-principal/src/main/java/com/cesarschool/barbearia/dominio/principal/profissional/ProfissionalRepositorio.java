package com.cesarschool.barbearia.dominio.principal.profissional;

import java.time.LocalDateTime;

import com.cesarschool.barbearia.dominio.compartilhado.base.Repositorio;
import com.cesarschool.barbearia.dominio.compartilhado.valueobjects.Cpf;

/**
 * Porta (interface) que define as operações de persistência para Profissional.
 * Implementada pela camada de infraestrutura.
 * Princípio de Inversão de Dependência: o domínio define o contrato.
 */
public interface ProfissionalRepositorio extends Repositorio<Profissional, Integer>{
    Profissional buscarPorCpf(Cpf cpf);
    boolean existePorCpf(Cpf cpf);
    Profissional buscarPrimeiroProfissionalDisponivel(LocalDateTime dataHora, int duracaoMinutos);
}
