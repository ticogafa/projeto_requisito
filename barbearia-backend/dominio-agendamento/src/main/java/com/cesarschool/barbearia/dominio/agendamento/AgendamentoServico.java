package com.cesarschool.barbearia.dominio.agendamento;

import java.time.LocalDateTime;
import java.util.List;

import static org.apache.commons.lang3.Validate.isTrue;
import static org.apache.commons.lang3.Validate.notNull;

import com.cesarschool.barbearia.dominio.compartilhado.StatusAgendamento;
import com.cesarschool.barbearia.dominio.profissionais.profissional.ProfissionalId;

/**
 * Serviço de domínio contendo as regras de negócio de Agendamento.
 */
public class AgendamentoServico {
    private final AgendamentoRepositorio repositorio;

    public AgendamentoServico(AgendamentoRepositorio repositorio) {
        notNull(repositorio, "O repositório não pode ser nulo");
        this.repositorio = repositorio;
    }

    /**
     * Cria um novo agendamento verificando disponibilidade.
     */
    public Agendamento criar(Agendamento agendamento, int duracaoServicoMinutos) {
        notNull(agendamento, "O agendamento não pode ser nulo");
        
        // Regra de negócio: não pode agendar no passado
        if (agendamento.getDataHora().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Não é possível agendar para datas passadas");
        }
        
        // Regra de negócio: verifica conflito de horário
        if (repositorio.existeAgendamentoNoPeriodo(
                agendamento.getProfissionalId(), 
                agendamento.getDataHora(), 
                duracaoServicoMinutos)) {
            throw new IllegalStateException(
                "Já existe um agendamento neste horário para o profissional"
            );
        }
        
        return repositorio.salvar(agendamento);
    }

    public Agendamento buscarPorId(AgendamentoId id) {
        notNull(id, "O ID não pode ser nulo");
        return repositorio.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException(
                    "Agendamento não encontrado com ID: " + id
                ));
    }

    public Agendamento confirmar(AgendamentoId id) {
        Agendamento agendamento = buscarPorId(id);
        agendamento.confirmar();
        return repositorio.salvar(agendamento);
    }

    public Agendamento cancelar(AgendamentoId id) {
        Agendamento agendamento = buscarPorId(id);
        agendamento.cancelar();
        return repositorio.salvar(agendamento);
    }

    public Agendamento iniciarAtendimento(AgendamentoId id) {
        Agendamento agendamento = buscarPorId(id);
        agendamento.iniciarAtendimento();
        return repositorio.salvar(agendamento);
    }

    public Agendamento concluir(AgendamentoId id) {
        Agendamento agendamento = buscarPorId(id);
        agendamento.concluir();
        return repositorio.salvar(agendamento);
    }

    public List<Agendamento> listarPorCliente(Integer clienteId) {
        notNull(clienteId, "O ID do cliente não pode ser nulo");
        return repositorio.buscarPorCliente(clienteId);
    }

    public List<Agendamento> listarPorProfissional(ProfissionalId profissionalId) {
        notNull(profissionalId, "O ID do profissional não pode ser nulo");
        return repositorio.buscarPorProfissional(profissionalId);
    }

    public List<Agendamento> listarPorStatus(StatusAgendamento status) {
        notNull(status, "O status não pode ser nulo");
        return repositorio.buscarPorStatus(status);
    }

    public List<Agendamento> listarPorPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        notNull(inicio, "A data de início não pode ser nula");
        notNull(fim, "A data de fim não pode ser nula");
        isTrue(inicio.isBefore(fim), "A data de início deve ser anterior à data de fim");
        return repositorio.buscarPorPeriodo(inicio, fim);
    }
}
