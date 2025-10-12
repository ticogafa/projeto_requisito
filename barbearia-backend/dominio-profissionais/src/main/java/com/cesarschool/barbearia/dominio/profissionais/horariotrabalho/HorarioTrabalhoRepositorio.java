package com.cesarschool.barbearia.dominio.profissionais.horariotrabalho;

import com.cesarschool.barbearia.dominio.compartilhado.DiaSemana;
import com.cesarschool.barbearia.dominio.profissionais.profissional.ProfissionalId;

import java.util.List;
import java.util.Optional;

/**
 * Porta (interface) de persistência para HorarioTrabalho.
 */
public interface HorarioTrabalhoRepositorio {
    
    HorarioTrabalho salvar(HorarioTrabalho horario);
    
    Optional<HorarioTrabalho> buscarPorId(HorarioTrabalhoId id);
    
    List<HorarioTrabalho> buscarPorProfissional(ProfissionalId profissionalId);
    
    List<HorarioTrabalho> buscarPorProfissionalEDia(ProfissionalId profissionalId, DiaSemana dia);
    
    List<HorarioTrabalho> listarAtivos();
    
    void remover(HorarioTrabalhoId id);
}
