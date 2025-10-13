package com.cesarschool.barbearia.dominio.principal.horariotrabalho;

import java.util.List;

import com.cesarschool.barbearia.dominio.compartilhado.base.Repositorio;
import com.cesarschool.barbearia.dominio.compartilhado.enums.DiaSemana;
import com.cesarschool.barbearia.dominio.principal.profissional.ProfissionalId;

/**
 * Porta (interface) de persistência para HorarioTrabalho.
 */
public interface HorarioTrabalhoRepositorio extends Repositorio<HorarioTrabalho, Integer>{    

    List<HorarioTrabalho> buscarPorProfissional(ProfissionalId profissionalId);
    
    List<HorarioTrabalho> buscarPorProfissionalEDia(ProfissionalId profissionalId, DiaSemana dia);
    
    List<HorarioTrabalho> listarAtivos();
    
}
