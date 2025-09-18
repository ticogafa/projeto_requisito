package com.cesarschool.barbearia_backend.profissionais.mapper;

import org.springframework.stereotype.Component;

import com.cesarschool.barbearia_backend.profissionais.dto.HorarioTrabalhoDTOs.AtualizarHorarioTrabalhoRequest;
import com.cesarschool.barbearia_backend.profissionais.dto.HorarioTrabalhoDTOs.CriarHorarioTrabalhoRequest;
import com.cesarschool.barbearia_backend.profissionais.dto.HorarioTrabalhoDTOs.HorarioTrabalhoResponse;
import com.cesarschool.barbearia_backend.profissionais.model.HorarioTrabalho;

@Component
public class HorarioTrabalhoMapper {

    public HorarioTrabalho toEntity(CriarHorarioTrabalhoRequest request) {
        if (request == null) {
            return null;
        }
        HorarioTrabalho entity = new HorarioTrabalho();
        // entity.setId(null); // id will be set elsewhere
        // entity.setProfissional(null); // will be set in service

        entity.setDiaSemana(request.getDiaSemana());
        entity.setHoraInicio(request.getHoraInicio());
        entity.setHoraFim(request.getHoraFim());
        return entity;
    }

    public HorarioTrabalhoResponse toResponse(HorarioTrabalho horarioTrabalho) {
        if (horarioTrabalho == null) {
            return null;
        }
        HorarioTrabalhoResponse response = new HorarioTrabalhoResponse();
        response.setId(horarioTrabalho.getId());
        response.setProfissionalId(horarioTrabalho.getProfissional().getId());
        response.setProfissionalNome(horarioTrabalho.getProfissional().getNome());
        response.setDiaSemana(horarioTrabalho.getDiaSemana());
        response.setHoraInicio(horarioTrabalho.getHoraInicio());
        response.setHoraFim(horarioTrabalho.getHoraFim());
        return response;
    }

    public void updateEntityFromDto(AtualizarHorarioTrabalhoRequest dto, HorarioTrabalho entity) {
        if (dto == null || entity == null) {
            return;
        }
        // entity.setId(entity.getId()); // keep existing id
        // entity.setProfissional(entity.getProfissional()); // keep existing profissional

        entity.setDiaSemana(dto.getDiaSemana());
        entity.setHoraInicio(dto.getHoraInicio());
        entity.setHoraFim(dto.getHoraFim());
    }
}
