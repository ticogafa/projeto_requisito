package com.cesarschool.barbearia_backend.profissionais.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cesarschool.barbearia_backend.common.enums.DiaSemana;
import com.cesarschool.barbearia_backend.profissionais.model.HorarioTrabalho;
import com.cesarschool.barbearia_backend.profissionais.model.Profissional;

public interface ProfissionalRepository extends JpaRepository<Profissional, UUID>{

    @Query(
        "SELECT h FROM horario_trabalho h WHERE h.profissional_id = :profissional_id AND h.dia_semana = :dia_semana"
    )
    Optional<HorarioTrabalho> findHorarioTrabalhoByProfissionalAndDiaSemana(
        @Param ("profissional_id") UUID profissionalId,
        @Param ("dia_semana") DiaSemana diaSemana);
}
