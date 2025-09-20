package com.cesarschool.barbearia_backend.agendamento.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


import org.springframework.stereotype.Service;

import com.cesarschool.barbearia_backend.agendamento.dto.AgendamentoDTOs.AgendamentoResponse;
import com.cesarschool.barbearia_backend.agendamento.dto.AgendamentoDTOs.CriarAgendamentoRequest;
import com.cesarschool.barbearia_backend.agendamento.mapper.AgendamentoMapper;
import com.cesarschool.barbearia_backend.agendamento.model.Agendamento;
import com.cesarschool.barbearia_backend.agendamento.repository.AgendamentoRepository;
import com.cesarschool.barbearia_backend.common.enums.DiaSemana;
import com.cesarschool.barbearia_backend.common.enums.StatusAgendamento;
import com.cesarschool.barbearia_backend.marketing.model.Cliente;
import com.cesarschool.barbearia_backend.marketing.repository.ClienteRepository;
import com.cesarschool.barbearia_backend.profissionais.model.Profissional;
import com.cesarschool.barbearia_backend.profissionais.model.ServicoOferecido;
import com.cesarschool.barbearia_backend.profissionais.repository.ProfissionalRepository;
import com.cesarschool.barbearia_backend.profissionais.repository.ServicoOferecidoRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class AgendamentoService {
    private final AgendamentoRepository repository;
    private final ProfissionalRepository profissionalRepository;
    private final ClienteRepository clienteRepository;
    private final ServicoOferecidoRepository servicoRepository;
    private final AgendamentoMapper mapper;

    public Agendamento findById(Integer id){
        return repository.findById(id).orElseThrow(() -> {
            throw new IllegalArgumentException("Agendamento não encontrado.");
        });
    }

        public List<Agendamento> findAll(){
        // regras de negócio....
        return repository.findAll();
    }

    public void verificarConflitoHorario(Agendamento agendamento) {
    var dataHora = agendamento.getDataHora();
    var profissionalId = agendamento.getProfissional().getId();

    boolean existeConflito = !repository.findByDataHoraAndProfissional(dataHora, profissionalId).isEmpty();

    if (existeConflito) {
        String nomeProfissional = agendamento.getProfissional().getNome();
        throw new IllegalArgumentException(
            String.format("Já existe um agendamento com %s no para %s.", nomeProfissional, dataHora.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")))
        );
    }
    }

    /**
     * Regras de negócio para verificar se o profissional está disponível no horário solicitado
     * -- • Um Agendamento não pode ser criado em um horário fora da jornada de trabalho do profissional
     * -- • Se um Agendamento não especificar o profissional, o sistema deve atribuir o primeiro com horário livre
     */
    public void verificarConflitoProfissional(Profissional profissional, LocalDateTime horarioAgendamento) {
        var diaSemana = DiaSemana.fromLocalDateTime(horarioAgendamento);
        profissionalRepository.findHorarioTrabalhoByProfissionalAndDiaSemana(profissional.getId(), diaSemana)
            .ifPresentOrElse(value -> {
                var inicioPausa = value.getFimPausa();
                var fimPausa = value.getInicioPausa();
                var horario = horarioAgendamento.toLocalTime();
                var profissionalTemPausa = inicioPausa != null && fimPausa != null;
                if (profissionalTemPausa && (horario.isAfter(inicioPausa) && horario.isBefore(fimPausa))) {
                    throw new IllegalArgumentException(
                        String.format("%s não está disponível no horário solicitado.", profissional.getNome())
                    );
                }
            }, () -> {
                throw new IllegalArgumentException(
                    String.format(
                        "O profissional %s não possui horário de trabalho cadastrado para %s.", profissional.getNome(), diaSemana.getNome()
                    )
                );
            });
        }


    public void verificarAlteracaoStatus(Agendamento agendamento) {
        var status = agendamento.getStatus();
        var horasAteAgendamento = Duration.between(
            LocalDateTime.now(),
            agendamento.getDataHora()
        ).toHours();
        if(status.equals(StatusAgendamento.CANCELADO) && horasAteAgendamento <= 2)
            throw new IllegalArgumentException("Não é permitido cancelar agendamentos com menos de 2 horas de antecedência.");
        else if(status.equals(StatusAgendamento.CONFIRMADO) && horasAteAgendamento < 0) {
            agendamento.setStatus(StatusAgendamento.CANCELADO);
            repository.save(agendamento);
            throw new IllegalArgumentException("Não é possível confirmar um agendamento que já passou.");
        }
    }

    /**
     * Simple save method without validations
     * Validations should be applied in the business methods that need them
     */
    public Agendamento save(Agendamento agendamento) {
        return repository.save(agendamento);
    }

    public void delete(Agendamento agendamento){
        repository.delete(agendamento);
    }

    // Métodos que trabalham com DTOs
    public AgendamentoResponse criarAgendamento(CriarAgendamentoRequest request) {
        // Buscar entidades relacionadas
        Cliente cliente = clienteRepository.findById(request.getClienteId())
            .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado"));
        
        Profissional profissional = profissionalRepository.findById(request.getProfissionalId())
            .orElseThrow(() -> new IllegalArgumentException("Profissional não encontrado"));
        
        ServicoOferecido servico = servicoRepository.findById(request.getServicoId())
            .orElseThrow(() -> new IllegalArgumentException("Serviço não encontrado"));
        
        // Converter DTO para entidade
        Agendamento agendamento = mapper.toEntity(request, cliente, profissional, servico);
        
        // Apply full validation for new agendamentos
        this.verificarConflitoHorario(agendamento);
        this.verificarConflitoProfissional(agendamento.getProfissional(), agendamento.getDataHora());
        this.verificarAlteracaoStatus(agendamento);
        
        // Salvar o agendamento
        Agendamento agendamentoSalvo = save(agendamento);
        
        // Converter para DTO de resposta
        return mapper.toResponse(agendamentoSalvo);
    }

    public AgendamentoResponse buscarPorId(Integer id) {
        Agendamento agendamento = findById(id);
        return mapper.toResponse(agendamento);
    }

    public AgendamentoResponse confirmarAgendamento(Integer id) {
        Agendamento agendamento = findById(id);
        agendamento.setStatus(StatusAgendamento.CONFIRMADO);
        
        // Only validate status change rules for confirmation
        this.verificarAlteracaoStatus(agendamento);
        
        Agendamento agendamentoSalvo = save(agendamento);
        return mapper.toResponse(agendamentoSalvo);
    }

    public AgendamentoResponse cancelarAgendamento(Integer id) {
        Agendamento agendamento = findById(id);
        agendamento.setStatus(StatusAgendamento.CANCELADO);
        
        // Only validate status change rules for cancellation
        this.verificarAlteracaoStatus(agendamento);
        
        Agendamento agendamentoSalvo = save(agendamento);
        return mapper.toResponse(agendamentoSalvo);
    }
}
