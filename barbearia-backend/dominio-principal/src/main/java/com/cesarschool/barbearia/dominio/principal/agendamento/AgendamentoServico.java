package com.cesarschool.barbearia.dominio.principal.agendamento;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import com.cesarschool.barbearia.dominio.compartilhado.utils.Validacoes;
import com.cesarschool.barbearia.dominio.principal.cliente.ClienteId;
import com.cesarschool.barbearia.dominio.principal.profissional.Profissional;
import com.cesarschool.barbearia.dominio.principal.profissional.ProfissionalId;
import com.cesarschool.barbearia.dominio.principal.profissional.ProfissionalServico;

/**
 * Serviço de domínio contendo as regras de negócio de Agendamento.
 */
public class AgendamentoServico {
    private final AgendamentoRepositorio repositorio;
    private final ProfissionalServico profissionalServico;

    public AgendamentoServico(AgendamentoRepositorio repositorio, ProfissionalServico profissionalRepositorio) {
        Validacoes.validarObjetoObrigatorio(repositorio, "O repositório");
        this.repositorio = repositorio;
        this.profissionalServico = profissionalRepositorio;
    }

    /**
     * Cria um novo agendamento verificando disponibilidade.
     * Regras de negócio:
     * - Não pode agendar no passado
     * - Não pode haver conflito de horário para o profissional
     * - Profissional deve estar qualificado para o serviço
     * - Serviço deve estar ativo
     * - Cliente deve ser informado
     * - Deve respeitar jornada de trabalho
     * - Deve respeitar intervalo de limpeza
     * - Add-on deve ter serviço principal
     */
    public Agendamento criar(Agendamento agendamento, int duracaoServicoMinutos) {
        // Validar se o cliente foi informado
        if (agendamento.getClienteId() == null) {
            throw new IllegalArgumentException("Cliente deve ser informado para criar agendamento");
        }
        
        // Validar horário de funcionamento (8h às 18h)
        var data = agendamento.getDataHora();
        var hora = agendamento.getDataHora().toLocalTime();
        if(hora.isBefore(LocalTime.of(8, 0)) || hora.isAfter(LocalTime.of(18, 0))) {
            throw new IllegalStateException(
                "Agendamentos só podem ser feitos entre 08:00 e 18:00"
            );
        }
        
        // Se profissional não informado, buscar automaticamente
        if(agendamento.getProfissionalId() == null){
            Profissional profissional = profissionalServico.buscarPrimeiroProfissionalDisponivel(data, duracaoServicoMinutos);
            agendamento.setProfissional(profissional.getId());
        }

        // Verificar se existe conflito de horário
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
        Validacoes.validarObjetoObrigatorio(id, "ID do agendamento");
        return repositorio.buscarPorId(id.getValor());
    }

    /**
     * Confirma um agendamento.
     */
    public Agendamento confirmar(AgendamentoId id) {
        Validacoes.validarObjetoObrigatorio(id, "ID do agendamento");
        Agendamento agendamento = buscarPorId(id);
        agendamento.confirmar();
        return repositorio.salvar(agendamento);
    }

    /**
     * Cancela um agendamento.
     */
    public Agendamento cancelar(AgendamentoId id, UsuarioSolicitante usuario) {
        Validacoes.validarObjetoObrigatorio(id, "ID do agendamento");
        Agendamento agendamento = buscarPorId(id);
        agendamento.cancelar(usuario);
        return repositorio.salvar(agendamento);
    }

    public List<Agendamento> listarPorCliente(ClienteId clienteId) {
        Validacoes.validarObjetoObrigatorio(clienteId, "ID do cliente");
        return repositorio.buscarPorCliente(clienteId);
    }

    public List<Agendamento> listarPorProfissional(ProfissionalId profissionalId) {
        Validacoes.validarObjetoObrigatorio(profissionalId, "ID do profissional");
        return repositorio.buscarPorProfissional(profissionalId);
    }

    public List<Agendamento> listarPorStatus(StatusAgendamento status) {
        Validacoes.validarObjetoObrigatorio(status, "Status do agendamento");
        return repositorio.buscarPorStatus(status);
    }

    public List<Agendamento> listarPorPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        Validacoes.validarObjetoObrigatorio(inicio, "A data de início");
        Validacoes.validarObjetoObrigatorio(fim, "A data de fim");
        Validacoes.validarInicioAntesFim(inicio, fim);

        return repositorio.buscarPorPeriodo(inicio, fim);
    }

    public List<Agendamento> listarTodos() {
        return repositorio.listarTodos();
    }
}
