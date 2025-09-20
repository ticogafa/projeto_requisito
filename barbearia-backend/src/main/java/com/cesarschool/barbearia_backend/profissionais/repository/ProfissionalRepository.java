package com.cesarschool.barbearia_backend.profissionais.repository;

import java.util.List;
import java.util.Optional;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cesarschool.barbearia_backend.common.enums.DiaSemana;
import com.cesarschool.barbearia_backend.profissionais.model.HorarioTrabalho;
import com.cesarschool.barbearia_backend.profissionais.model.Profissional;
import com.cesarschool.barbearia_backend.profissionais.model.ProfissionalServico;

public interface ProfissionalRepository extends JpaRepository<Profissional, Integer>{
    @Query(
        "SELECT h FROM HorarioTrabalho h WHERE h.profissional.id = :profissionalId AND h.diaSemana = :diaSemana"
    )
    Optional<HorarioTrabalho> findHorarioTrabalhoByProfissionalAndDiaSemana(
        @Param("profissionalId") Integer profissionalId,
        @Param("diaSemana") DiaSemana diaSemana);

        
    @Query(
        "SELECT p from ProfissionalServico p WHERE p.servicoOferecido.id = :servicoId"
    )
    List<ProfissionalServico> buscarProfissionaisPorServico(@Param("servicoId") Integer servicoId);
    

}



