package com.cesarschool.cucumber.gestaoAgendamento;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.cesarschool.barbearia.dominio.principal.agendamento.Agendamento;
import com.cesarschool.barbearia.dominio.principal.agendamento.AgendamentoId;
import com.cesarschool.barbearia.dominio.principal.agendamento.AgendamentoRepositorio;
import com.cesarschool.barbearia.dominio.principal.agendamento.StatusAgendamento;
import com.cesarschool.barbearia.dominio.principal.cliente.Cliente;
import com.cesarschool.barbearia.dominio.principal.cliente.ClienteId;
import com.cesarschool.barbearia.dominio.principal.profissional.Profissional;
import com.cesarschool.barbearia.dominio.principal.profissional.ProfissionalId;
import com.cesarschool.barbearia.dominio.principal.servico.ServicoOferecido;
import com.cesarschool.barbearia.dominio.principal.servico.ServicoOferecidoId;

public class GestaoAgendamentoMockRepositorio implements AgendamentoRepositorio {
    
    private Map<AgendamentoId, Agendamento> agendamentos = new HashMap<>();
    public  Map<ProfissionalId, Profissional> profissionais = new HashMap<>();
    private Map<ServicoOferecidoId, ServicoOferecido> servicos = new HashMap<>();
    private Map<ClienteId, Cliente> clientes = new HashMap<>();
    private Map<ProfissionalId, LocalTime> jornadas = new HashMap<>();
    private Map<ServicoOferecidoId, List<ProfissionalId>> servicoProfissionais = new HashMap<>();
    private Map<ServicoOferecidoId, Boolean> servicosAtivos = new HashMap<>();
    private Map<ServicoOferecidoId, Integer> duracaoServicos = new HashMap<>();
    private Map<ServicoOferecidoId, Integer> intervalosLimpeza = new HashMap<>();
    private Map<ServicoOferecidoId, ServicoOferecidoId> addOns = new HashMap<>();
    private int proximoId = 1;
    
    // Métodos de setup para testes
    public void adicionarProfissional(ProfissionalId id, Profissional profissional) {
        profissionais.put(id, profissional);
        jornadas.put(id, LocalTime.of(18, 0)); // Jornada padrão até 18h
    }
    
    public void adicionarServico(ServicoOferecidoId id, ServicoOferecido servico, boolean ativo) {
        servicos.put(id, servico);
        servicosAtivos.put(id, ativo);
        duracaoServicos.put(id, 60); // Duração padrão de 60 minutos
        intervalosLimpeza.put(id, 0); // Sem intervalo por padrão
    }
    
    public void adicionarCliente(ClienteId id, Cliente cliente) {
        clientes.put(id, cliente);
    }
    
    public void associarServicoAProfissional(ServicoOferecidoId servicoId, ProfissionalId profissionalId) {
        servicoProfissionais.computeIfAbsent(servicoId, k -> new ArrayList<>()).add(profissionalId);
    }
    
    public void definirDuracaoServico(ServicoOferecidoId servicoId, int duracaoMinutos) {
        duracaoServicos.put(servicoId, duracaoMinutos);
    }
    
    public void definirIntervaloLimpeza(ServicoOferecidoId servicoId, int intervaloMinutos) {
        intervalosLimpeza.put(servicoId, intervaloMinutos);
    }
    
    public void definirAddOn(ServicoOferecidoId addOnId, ServicoOferecidoId servicoPrincipalId) {
        addOns.put(addOnId, servicoPrincipalId);
    }
    
    public void definirJornada(ProfissionalId profissionalId, LocalTime horaFim) {
        jornadas.put(profissionalId, horaFim);
    }
    
    public void limparDados() {
        agendamentos.clear();
        profissionais.clear();
        servicos.clear();
        clientes.clear();
        jornadas.clear();
        servicoProfissionais.clear();
        servicosAtivos.clear();
        duracaoServicos.clear();
        intervalosLimpeza.clear();
        addOns.clear();
        proximoId = 1;
    }
    
    // Métodos de validação
    public boolean profissionalQualificado(ProfissionalId profissionalId, ServicoOferecidoId servicoId) {
        List<ProfissionalId> profissionaisDoServico = servicoProfissionais.get(servicoId);
        return profissionaisDoServico != null && profissionaisDoServico.contains(profissionalId);
    }
    
    public boolean servicoAtivo(ServicoOferecidoId servicoId) {
        return servicosAtivos.getOrDefault(servicoId, false);
    }
    
    public boolean dentroJornada(ProfissionalId profissionalId, LocalDateTime dataHora) {
        LocalTime horaFim = jornadas.get(profissionalId);
        return horaFim != null && dataHora.toLocalTime().isBefore(horaFim);
    }
    
    public boolean respeitaIntervaloLimpeza(ServicoOferecidoId servicoId, ProfissionalId profissionalId, LocalDateTime novoHorario) {
        Integer intervalo = intervalosLimpeza.get(servicoId);
        if (intervalo == null || intervalo == 0) return true;
        
        return agendamentos.values().stream()
            .filter(a -> a.getProfissionalId().equals(profissionalId))
            .filter(a -> a.getStatus() != StatusAgendamento.CANCELADO)
            .noneMatch(a -> {
                LocalDateTime fimAgendamento = a.getDataHora().plusMinutes(duracaoServicos.getOrDefault(a.getServicoId(), 60));
                LocalDateTime inicioPermitido = fimAgendamento.plusMinutes(intervalo);
                return novoHorario.isBefore(inicioPermitido) && novoHorario.isAfter(a.getDataHora());
            });
    }
    
    public boolean isAddOn(ServicoOferecidoId servicoId) {
        return addOns.containsKey(servicoId);
    }
    
    public boolean clienteExiste(ClienteId clienteId) {
        return clientes.containsKey(clienteId);
    }
    
    public boolean agendamentoEmAndamento(AgendamentoId agendamentoId) {
        // Verificar se existe algum agendamento que está atualmente em andamento
        LocalDateTime agora = LocalDateTime.now();
        
        return agendamentos.values().stream()
            .filter(a -> a.getStatus() != StatusAgendamento.CANCELADO)
            .anyMatch(a -> {
                LocalDateTime inicio = a.getDataHora();
                LocalDateTime fim = inicio.plusMinutes(duracaoServicos.getOrDefault(a.getServicoId(), 60));
                // Agendamento está em andamento se o horário atual está entre início e fim
                return agora.isAfter(inicio) && agora.isBefore(fim);
            });
    }
    
    @Override
    public Agendamento salvar(Agendamento agendamento) {
        AgendamentoId id = new AgendamentoId(proximoId++);
        Agendamento novoAgendamento = new Agendamento(
            agendamento.getDataHora(),
            agendamento.getClienteId(),
            agendamento.getProfissionalId(),
            agendamento.getServicoId(),
            agendamento.getObservacoes()
        );
        agendamentos.put(id, novoAgendamento);
        return novoAgendamento;
    }

    @Override
    public List<Agendamento> buscarPorCliente(ClienteId clienteId) {
        return agendamentos.values().stream()
            .filter(a -> a.getClienteId().equals(clienteId))
            .toList();
    }

    @Override
    public Optional<Agendamento> buscarPorId(AgendamentoId id) {
        return Optional.ofNullable(agendamentos.get(id));
    }

    @Override
    public List<Agendamento> buscarPorPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        return agendamentos.values().stream()
            .filter(a -> !a.getDataHora().isBefore(inicio) && !a.getDataHora().isAfter(fim))
            .toList();
    }

    @Override
    public List<Agendamento> buscarPorProfissional(ProfissionalId profissionalId) {
        return agendamentos.values().stream()
            .filter(a -> a.getProfissionalId().equals(profissionalId))
            .toList();
    }

    @Override
    public List<Agendamento> buscarPorStatus(StatusAgendamento status) {
        return agendamentos.values().stream()
            .filter(a -> a.getStatus().equals(status))
            .toList();
    }

    @Override
    public boolean existeAgendamentoNoPeriodo(ProfissionalId profissionalId, LocalDateTime dataHora, int duracaoMinutos) {
        LocalDateTime fimNovoAgendamento = dataHora.plusMinutes(duracaoMinutos);
        
        return agendamentos.values().stream()
            .filter(a -> a.getProfissionalId().equals(profissionalId))
            .filter(a -> a.getStatus() != StatusAgendamento.CANCELADO)
            .anyMatch(a -> {
                LocalDateTime inicioExistente = a.getDataHora();
                LocalDateTime fimExistente = inicioExistente.plusMinutes(duracaoServicos.getOrDefault(a.getServicoId(), 60));
                
                return (dataHora.isBefore(fimExistente) && fimNovoAgendamento.isAfter(inicioExistente));
            });
    }

    @Override
    public List<Agendamento> listarTodos() {
        return new ArrayList<>(agendamentos.values());
    }

    @Override
    public void remover(AgendamentoId id) {
        agendamentos.remove(id);
    }
}
