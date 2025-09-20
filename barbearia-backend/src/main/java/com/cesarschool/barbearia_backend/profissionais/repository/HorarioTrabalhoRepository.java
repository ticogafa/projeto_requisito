package com.cesarschool.barbearia_backend.profissionais.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cesarschool.barbearia_backend.profissionais.model.HorarioTrabalho;

public interface HorarioTrabalhoRepository extends JpaRepository<HorarioTrabalho, Integer> {

    /**
     * Recupera todos os horários de trabalho associados ao profissional informado pelo ID.
     *
     * @param profissionalId o ID do profissional cujos horários de trabalho serão recuperados
     * @return uma lista de objetos HorarioTrabalho relacionados ao profissional
     */
    @Query("SELECT h FROM HorarioTrabalho h WHERE h.profissional.id = :profissionalId")
    List<HorarioTrabalho> buscarHorariosPorProfissionalId(Integer profissionalId);
}
