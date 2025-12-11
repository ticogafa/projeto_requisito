package com.cesarschool.barbearia.dominio.principal.profissional;

import java.time.LocalDateTime;
import java.util.List;

import com.cesarschool.barbearia.dominio.compartilhado.base.Repositorio;
import com.cesarschool.barbearia.dominio.compartilhado.valueobjects.Cpf;
import com.cesarschool.barbearia.dominio.principal.servico.ServicoOferecidoId;

public interface ProfissionalRepositorio extends Repositorio<Profissional, Integer>{
    Profissional buscarPorCpf(Cpf cpf);
    boolean existePorCpf(Cpf cpf);
    Profissional buscarPrimeiroProfissionalDisponivel(LocalDateTime dataHora, int duracaoServicoMinutos);

    /**
     * Busca profissionais qualificados para ofertar um serviço específico.
     * @param servicoId ID do serviço
     * @return Lista de profissionais que podem executar o serviço
     */
    List<Profissional> buscarQualificadosParaServico(ServicoOferecidoId servicoId);
    
    /**
     * Busca profissionais disponíveis em uma data/hora específica.
     * Verifica se o horário está dentro da jornada de trabalho.
     * @param dataHora Data e hora do agendamento
     * @param duracaoMinutos Duração do serviço em minutos
     * @return Lista de profissionais disponíveis
     */
    List<Profissional> buscarDisponiveisNaDataHora(LocalDateTime dataHora, Integer duracaoMinutos);
    
    /**
     * Adiciona qualificação de um serviço para um profissional.
     * @param profissionalId ID do profissional
     * @param servicoId ID do serviço
     */
    void adicionarQualificacao(ProfissionalId profissionalId, ServicoOferecidoId servicoId);
    
    /**
     * Remove qualificação de um serviço de um profissional.
     * @param profissionalId ID do profissional
     * @param servicoId ID do serviço
     */
    void removerQualificacao(ProfissionalId profissionalId, ServicoOferecidoId servicoId);
    
    /**
     * Verifica se um profissional está qualificado para um serviço.
     * @param profissionalId ID do profissional
     * @param servicoId ID do serviço
     * @return true se estiver qualificado
     */
    boolean estaQualificado(ProfissionalId profissionalId, ServicoOferecidoId servicoId);

    boolean temAgendamentoAtivo(String nomeServico);
    boolean possuiAssociacaoServico(String nomeProfissional, String nomeServico);
    void removerAssociacaoServico(String nomeProfissional, String nomeServico);
}