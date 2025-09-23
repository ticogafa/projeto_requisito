package com.cesarschool.barbearia_backend.atendimento.controller.dto;

// Usando record para um DTO simples e imutável
public record IniciarAtendimentoRequest(Long agendamentoId) {
}