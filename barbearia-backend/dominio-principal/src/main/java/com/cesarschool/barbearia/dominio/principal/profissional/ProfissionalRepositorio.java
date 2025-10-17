package com.cesarschool.barbearia.dominio.principal.profissional;

import java.time.LocalDateTime;

import com.cesarschool.barbearia.dominio.compartilhado.base.Repositorio;
import com.cesarschool.barbearia.dominio.compartilhado.valueobjects.Cpf;

public interface ProfissionalRepositorio extends Repositorio<Profissional, Integer>{
    Profissional buscarPorCpf(Cpf cpf);
    boolean existePorCpf(Cpf cpf);
    
    Profissional buscarPrimeiroProfissionalDisponivel(LocalDateTime dataHora, int duracaoServicoMinutos);
}