package com.cesarschool.barbearia_backend.profissionais.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.cesarschool.barbearia_backend.profissionais.dto.HorarioTrabalhoDTOs.AtualizarHorarioTrabalhoRequest;
import com.cesarschool.barbearia_backend.profissionais.dto.HorarioTrabalhoDTOs.CriarHorarioTrabalhoRequest;
import com.cesarschool.barbearia_backend.profissionais.dto.HorarioTrabalhoDTOs.HorarioTrabalhoResponse;
import com.cesarschool.barbearia_backend.profissionais.model.HorarioTrabalho;

@Mapper(componentModel = "spring")
public interface HorarioTrabalhoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "profissional", ignore = true) // será setado no service
    HorarioTrabalho toEntity(CriarHorarioTrabalhoRequest request);

    @Mapping(source = "profissional.id", target = "profissionalId")
    @Mapping(source = "profissional.nome", target = "profissionalNome")
    HorarioTrabalhoResponse toResponse(HorarioTrabalho horarioTrabalho);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "profissional", ignore = true) // será mantido o existente
    void updateEntityFromDto(AtualizarHorarioTrabalhoRequest dto, @MappingTarget HorarioTrabalho entity);
}
