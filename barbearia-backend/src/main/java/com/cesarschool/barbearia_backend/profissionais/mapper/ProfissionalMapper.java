package com.cesarschool.barbearia_backend.profissionais.mapper;

import org.springframework.stereotype.Component;

import com.cesarschool.barbearia_backend.common.model.Cpf;
import com.cesarschool.barbearia_backend.common.model.Email;
import com.cesarschool.barbearia_backend.common.model.Telefone;
import com.cesarschool.barbearia_backend.profissionais.dto.ProfissionalDTOs.AtualizarProfissionalRequest;
import com.cesarschool.barbearia_backend.profissionais.dto.ProfissionalDTOs.CriarProfissionalRequest;
import com.cesarschool.barbearia_backend.profissionais.dto.ProfissionalDTOs.ProfissionalResponse;
import com.cesarschool.barbearia_backend.profissionais.model.Profissional;

@Component
public class ProfissionalMapper {
    private ProfissionalMapper() {}

    public static Profissional toEntity(CriarProfissionalRequest request) {
        Profissional profissional = new Profissional();
        profissional.setNome(request.getNome());
        profissional.setCpf(new Cpf(request.getCpf()));
        profissional.setCpf(new Cpf(request.getCpf()));
        profissional.setEmail(new Email(request.getEmail()));
        profissional.setTelefone(new Telefone(request.getTelefone()));
        return profissional;
    }

    public static ProfissionalResponse toResponse(Profissional entity) {
        ProfissionalResponse response = new ProfissionalResponse();
        response.setId(entity.getId());
        response.setNome(entity.getNome());
        response.setEmail(entity.getEmail());
        response.setCpf(entity.getCpf());
        response.setTelefone(entity.getTelefone());
        return response;
    }

    public static void updateEntityFromDto(AtualizarProfissionalRequest dto, Profissional entity) {
        entity.setNome(dto.getNome());
        entity.setEmail(new Email(dto.getEmail()));
        entity.setCpf(new Cpf(dto.getCpf()));
        entity.setTelefone(new Telefone(dto.getTelefone()));
    }

}
