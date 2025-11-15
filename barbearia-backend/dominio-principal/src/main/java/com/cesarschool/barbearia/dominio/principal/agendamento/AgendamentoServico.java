package com.cesarschool.barbearia.dominio.principal.agendamento;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import com.cesarschool.barbearia.dominio.compartilhado.utils.Validacoes;
import com.cesarschool.barbearia.dominio.principal.cliente.ClienteId;
import com.cesarschool.barbearia.dominio.principal.profissional.Profissional;
import com.cesarschool.barbearia.dominio.principal.profissional.ProfissionalId;
import com.cesarschool.barbearia.dominio.principal.profissional.ProfissionalServico;
import com.cesarschool.barbearia.dominio.principal.servico.ServicoOferecidoId;
import com.cesarschool.barbearia.dominio.principal.servico.ServicoOferecidoRepositorio;

/**
 * Serviço de domínio contendo as regras de negócio de Agendamento.
 */
public class AgendamentoServico {
    private final AgendamentoRepositorio repositorio;
    private final ProfissionalServico profissionalServico;
    private final ServicoOferecidoRepositorio servicoRepositorio;

    public AgendamentoServico(
            AgendamentoRepositorio repositorio, 
            ProfissionalServico profissionalServico) {
        Validacoes.validarObjetoObrigatorio(repositorio, "O repositório");
        this.repositorio = repositorio;
        this.profissionalServico = profissionalServico;
        this.servicoRepositorio = null; // Opcional para manter compatibilidade
    }

    public AgendamentoServico(
            AgendamentoRepositorio repositorio, 
            ProfissionalServico profissionalServico,
            ServicoOferecidoRepositorio servicoRepositorio) {
        Validacoes.validarObjetoObrigatorio(repositorio, "O repositório");
        Validacoes.validarObjetoObrigatorio(profissionalServico, "O serviço de profissional");
        this.repositorio = repositorio;
        this.profissionalServico = profissionalServico;
        this.servicoRepositorio = servicoRepositorio;
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
        
        // Validar se o serviço está ativo (se repositório estiver disponível)
        if (servicoRepositorio != null && agendamento.getServicoId() != null) {
            validarServicoAtivo(agendamento.getServicoId());
        }
        
        // Se profissional não informado, buscar automaticamente
        if(agendamento.getProfissionalId() == null){
            Profissional profissional = profissionalServico.buscarPrimeiroProfissionalDisponivel(data, duracaoServicoMinutos);
            agendamento.setProfissional(profissional.getId());
        }
        
        // Validar se o profissional está qualificado para o serviço (se repositório estiver disponível)
        if (servicoRepositorio != null && agendamento.getProfissionalId() != null && agendamento.getServicoId() != null) {
            validarProfissionalQualificado(agendamento.getProfissionalId(), agendamento.getServicoId());
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

    /**
     * Valida se o serviço está ativo para agendamento.
     * 
     * @param servicoId ID do serviço a validar
     * @throws IllegalStateException se o serviço estiver inativo
     */
    private void validarServicoAtivo(ServicoOferecidoId servicoId) {
        if (!servicoRepositorio.isAtivo(servicoId.getValor())) {
            throw new IllegalStateException("Serviço está inativo");
        }
    }

    /**
     * Valida se o profissional está qualificado para realizar o serviço.
     * 
     * @param profissionalId ID do profissional
     * @param servicoId ID do serviço
     * @throws IllegalStateException se o profissional não estiver qualificado
     */
    private void validarProfissionalQualificado(ProfissionalId profissionalId, ServicoOferecidoId servicoId) {
        // Validar se profissional está qualificado usando IDs diretamente
        boolean qualificado = profissionalServico.estaQualificado(profissionalId, servicoId);
        if (!qualificado) {
            throw new IllegalStateException(
                "Profissional não está qualificado para este serviço"
            );
        }
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
