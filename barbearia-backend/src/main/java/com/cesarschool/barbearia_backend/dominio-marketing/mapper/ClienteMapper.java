package com.cesarschool.barbearia_backend.marketing.mapper;

import com.cesarschool.barbearia_backend.common.model.Cpf;
import com.cesarschool.barbearia_backend.common.model.Email;
import com.cesarschool.barbearia_backend.common.model.Telefone;
import com.cesarschool.barbearia_backend.marketing.dto.ClienteDTOs.AtualizarClienteRequest;
import com.cesarschool.barbearia_backend.marketing.dto.ClienteDTOs.ClienteResponse;
import com.cesarschool.barbearia_backend.marketing.dto.ClienteDTOs.CriarClienteRequest;
import com.cesarschool.barbearia_backend.marketing.model.Cliente;

public class ClienteMapper {
    private ClienteMapper() {}

    public static Cliente toEntity(CriarClienteRequest dto) {
        if (dto == null) return null;
        Cliente cliente = new Cliente();
        cliente.setEmail(new Email(dto.getEmail()));
        cliente.setCpf(new Cpf(dto.getCpf()));
        cliente.setTelefone(new Telefone(dto.getTelefone()));
        cliente.setNome(dto.getNome());
        cliente.setPontos(0);
        // id is ignored
        return cliente;
    }

    public static ClienteResponse toResponse(Cliente entity) {
        if (entity == null) return null;
        ClienteResponse response = new ClienteResponse();
        response.setId(entity.getId());
        response.setNome(entity.getNome());
        response.setEmail(entity.getEmail());
        response.setCpf(entity.getCpf());
        response.setTelefone(entity.getTelefone());
        response.setPontos(entity.getPontos());
        return response;
    }


    public static void updateEntityFromDto(AtualizarClienteRequest dto, Cliente entity) {
        if (dto == null || entity == null) return;
        
        // id is ignored
        // cpf is ignored
        // pontos is ignored

        entity.setNome(dto.getNome());
        entity.setEmail(new Email(dto.getEmail()));
        entity.setTelefone(new Telefone(dto.getTelefone()));
    }

}
