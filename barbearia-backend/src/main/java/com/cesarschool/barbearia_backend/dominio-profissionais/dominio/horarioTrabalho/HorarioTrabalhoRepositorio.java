package com.cesarschool.barbearia_backend.dominio_profissionais.dominio.horarioTrabalho;

import java.util.List;

import com.cesarschool.barbearia_backend.profissionais.model.HorarioTrabalho;

public interface HorarioTrabalhoRepositorio {

    /**
     * Recupera todos os horários de trabalho associados ao profissional informado pelo ID.
     *
     * @param profissionalId o ID do profissional cujos horários de trabalho serão recuperados
     * @return uma lista de objetos HorarioTrabalho relacionados ao profissional
     */
    List<HorarioTrabalho> buscarHorariosPorProfissionalId(Integer profissionalId);
}
