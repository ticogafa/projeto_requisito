package com.cesarschool.barbearia_backend.profissionais.mapper;

import org.springframework.stereotype.Component;

import com.cesarschool.barbearia_backend.profissionais.dto.ServicoDTOs.AtualizarServicoRequest;
import com.cesarschool.barbearia_backend.profissionais.dto.ServicoDTOs.CriarServicoRequest;
import com.cesarschool.barbearia_backend.profissionais.dto.ServicoDTOs.ServicoResponse;
import com.cesarschool.barbearia_backend.profissionais.model.ServicoOferecido;

@Component
public class ServicoMapper {

    private ServicoMapper() {}

    public static ServicoOferecido toEntity(CriarServicoRequest request) {
        if (request == null) {
            return null;
        }
        ServicoOferecido entity = new ServicoOferecido();
        // entity.setId(null); // id ignored

        entity.setNome(request.getNome());
        entity.setDescricao(request.getDescricao());
        entity.setPreco(request.getPreco());
        entity.setDuracaoMinutos(request.getDuracaoMinutos());
        return entity;
    }

    public static ServicoResponse toResponse(ServicoOferecido servico) {
        if (servico == null) {
            return null;
        }
        ServicoResponse response = new ServicoResponse();
        response.setId(servico.getId());
        response.setNome(servico.getNome());
        response.setDescricao(servico.getDescricao());
        response.setPreco(servico.getPreco());
        response.setDuracaoMinutos(servico.getDuracaoMinutos());
        return response;
    }


    public static void updateEntityFromDto(AtualizarServicoRequest dto, ServicoOferecido entity) {
        if (dto == null || entity == null) {
            return;
        }
        // entity.setId(entity.getId()); // id ignored
        entity.setNome(dto.getNome());
        entity.setDescricao(dto.getDescricao());
        entity.setPreco(dto.getPreco());
        entity.setDuracaoMinutos(dto.getDuracaoMinutos());
    }
}
