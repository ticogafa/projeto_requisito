package com.cesarschool.barbearia_backend.agendamento.mapper;

import org.springframework.stereotype.Component;

import com.cesarschool.barbearia_backend.agendamento.dto.AgendamentoDTOs.AgendamentoResponse;
import com.cesarschool.barbearia_backend.agendamento.dto.AgendamentoDTOs.CriarAgendamentoRequest;
import com.cesarschool.barbearia_backend.agendamento.model.Agendamento;
import com.cesarschool.barbearia_backend.common.enums.StatusAgendamento;
import com.cesarschool.barbearia_backend.marketing.model.Cliente;
import com.cesarschool.barbearia_backend.profissionais.model.Profissional;
import com.cesarschool.barbearia_backend.profissionais.model.ServicoOferecido;

@Component
public class AgendamentoMapper {

    public Agendamento toEntity(CriarAgendamentoRequest request, Cliente cliente, 
                               Profissional profissional, ServicoOferecido servico) {
        Agendamento agendamento = new Agendamento();
        agendamento.setDataHora(request.getDataHora());
        agendamento.setStatus(StatusAgendamento.PENDENTE); // Status inicial sempre PENDENTE
        agendamento.setCliente(cliente);
        agendamento.setProfissional(profissional);
        agendamento.setServico(servico);
        agendamento.setObservacoes(request.getObservacoes());
        return agendamento;
    }

    public AgendamentoResponse toResponse(Agendamento agendamento) {
        AgendamentoResponse response = new AgendamentoResponse();
        
        // Dados do agendamento
        response.setId(agendamento.getId());
        response.setDataHora(agendamento.getDataHora());
        response.setStatus(agendamento.getStatus());
        response.setObservacoes(agendamento.getObservacoes());
        
        // Dados do cliente
        Cliente cliente = agendamento.getCliente();
        response.setClienteId(cliente.getId());
        response.setClienteNome(cliente.getNome());
        response.setClienteEmail(cliente.getEmail());
        response.setClienteTelefone(cliente.getTelefone());
        
        // Dados do profissional
        Profissional profissional = agendamento.getProfissional();
        response.setProfissionalId(profissional.getId());
        response.setProfissionalNome(profissional.getNome());
        response.setProfissionalEmail(profissional.getEmail());
        
        // Dados do serviço
        ServicoOferecido servico = agendamento.getServico();
        response.setServicoId(servico.getId());
        response.setServicoNome(servico.getNome());
        response.setServicoPreco(servico.getPreco());
        response.setServicoDuracaoMinutos(servico.getDuracaoMinutos());
        
        return response;
    }
}
