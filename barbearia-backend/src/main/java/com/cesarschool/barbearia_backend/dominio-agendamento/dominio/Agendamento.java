package com.cesarschool.barbearia_backend.agendamento.model;

import java.time.LocalDateTime;


import com.cesarschool.barbearia_backend.common.enums.StatusAgendamento;
import com.cesarschool.barbearia_backend.marketing.model.Cliente;
import com.cesarschool.barbearia_backend.profissionais.model.Profissional;
import com.cesarschool.barbearia_backend.profissionais.model.ServicoOferecido;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.Validate;

public class Agendamento {

    private Integer id;

    private LocalDateTime dataHora;

    private StatusAgendamento status;

    private Cliente cliente;
    
    private Profissional profissional;

    private ServicoOferecido servico;

    private String observacoes;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        Validate.notNull(id, "Id não pode ser nulo");
        this.id = id;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        Validate.notNull(dataHora, "Data e hora não podem ser nulos");
        this.dataHora = dataHora;
    }

    public StatusAgendamento getStatus() {
        return status;
    }

    public void setStatus(StatusAgendamento status) {
        Validate.notNull(status, "Status não pode ser nulo");
        this.status = status;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        Validate.notNull(cliente, "Cliente não pode ser nulo");
        this.cliente = cliente;
    }

    public Profissional getProfissional() {
        return profissional;
    }

    public void setProfissional(Profissional profissional) {
        Validate.notNull(profissional, "Profissional não pode ser nulo");
        this.profissional = profissional;
    }

    public ServicoOferecido getServico() {
        return servico;
    }

    public void setServico(ServicoOferecido servico) {
        Validate.notNull(servico, "Serviço não pode ser nulo");
        this.servico = servico;
    }

    public String getObservacoes() {
        return observacoes;
    }
}
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public Agendamento(Integer id, LocalDateTime dataHora, StatusAgendamento status, Cliente cliente,
            Profissional profissional, ServicoOferecido servico, String observacoes) {
        this.id = id;
        setDataHora(dataHora);
        setStatus(status);
        setCliente(cliente);
        setProfissional(profissional);
        setServico(servico);
        setObservacoes(observacoes);
    }
    
}
    
