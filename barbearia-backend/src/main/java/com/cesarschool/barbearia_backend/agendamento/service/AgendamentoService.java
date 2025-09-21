package com.cesarschool.barbearia_backend.agendamento.service;

import java.time.Clock;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.cesarschool.barbearia_backend.agendamento.dto.AgendamentoDTOs.AgendamentoResponse;
import com.cesarschool.barbearia_backend.agendamento.dto.AgendamentoDTOs.CriarAgendamentoRequest;
import com.cesarschool.barbearia_backend.agendamento.mapper.AgendamentoMapper;
import com.cesarschool.barbearia_backend.agendamento.model.Agendamento;
import com.cesarschool.barbearia_backend.agendamento.repository.AgendamentoRepository;
import com.cesarschool.barbearia_backend.common.enums.DiaSemana;
import com.cesarschool.barbearia_backend.common.enums.StatusAgendamento;
import com.cesarschool.barbearia_backend.common.exceptions.DuplicateException;
import com.cesarschool.barbearia_backend.common.exceptions.NotFoundException;
import com.cesarschool.barbearia_backend.marketing.model.Cliente;
import com.cesarschool.barbearia_backend.marketing.service.ClienteService;
import com.cesarschool.barbearia_backend.profissionais.dto.HorarioTrabalhoDTOs.HorarioTrabalhoResponse;
import com.cesarschool.barbearia_backend.profissionais.dto.ProfissionalDTOs.ProfissionalResponse;
import com.cesarschool.barbearia_backend.profissionais.mapper.ProfissionalMapper;
import com.cesarschool.barbearia_backend.profissionais.model.Profissional;
import com.cesarschool.barbearia_backend.profissionais.model.ServicoOferecido;
import com.cesarschool.barbearia_backend.profissionais.service.ProfissionalService;
import com.cesarschool.barbearia_backend.profissionais.service.ServicoOferecidoService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class AgendamentoService {
    
    private final Clock clock;
    private final AgendamentoRepository repository;
    private final ProfissionalService profissionalService;
    private final ClienteService clienteService;
    private final ServicoOferecidoService servicoOferecidoService;
    private final AgendamentoMapper mapper;

    public Agendamento buscarEntidadePorId(Integer id){
        return repository.findById(id).orElseThrow(() -> {
            throw new NotFoundException("Agendamento não encontrado.");
        });
    }

    public AgendamentoResponse buscarPorId(Integer id) {
        Agendamento agendamento = buscarEntidadePorId(id);
        return mapper.toResponse(agendamento);
    }

    /**
     * Regras de negócio para verificar conflito de horário:
     * 1. Não pode haver dois agendamentos para o mesmo profissional no mesmo horário
     * 2. Agendamentos cancelados não são considerados conflito
     * 
     * @param dataHora o horário a ser verificado
     * @param profissionalId ID do profissional
     * @return true se existe conflito, false caso contrário
     */
    public boolean temConflitoDeHorario(LocalDateTime dataHora, Integer profissionalId) {
        List<Agendamento> agendamentosConflitantes = repository.findByDataHoraAndProfissional(dataHora, profissionalId);
        
        return agendamentosConflitantes.stream()
            .anyMatch(ag -> !ag.getStatus().equals(StatusAgendamento.CANCELADO));
    }

    /**
     * Sobrecarga do método para manter compatibilidade com código existente
     * 
     * @param agendamento o agendamento a ser verificado
     * @return true se existe conflito, false caso contrário
     */
    public boolean temConflitoDeHorario(Agendamento agendamento) {
        return temConflitoDeHorario(agendamento.getDataHora(), agendamento.getProfissional().getId());
    }

    /**
     * Versão que lança exceção da verificação de conflito de horário
     * 
     * @param agendamento o agendamento a ser verificado
     * @throws IllegalArgumentException se houver conflito de horário
     */
    public void verificarConflitoHorario(Agendamento agendamento) {
        verificarConflitoHorario(
            agendamento.getDataHora(), 
            agendamento.getProfissional().getId()
        );
    }

    /**
     * Regras de negócio para verificar se o horário está dentro da jornada de trabalho do profissional:
     * 1. O profissional deve ter horário de trabalho cadastrado para o dia da semana
     * 2. O agendamento deve estar dentro da jornada de trabalho do profissional
     * 3. O agendamento não pode conflitar com o horário de pausa/almoço
     * 
     * Nota: Este método apenas verifica a jornada de trabalho, não verifica conflitos com outros agendamentos
     * 
     * @param profissional o profissional a ser verificado
     * @param horarioAgendamento o horário do agendamento
     * @return true se o horário está dentro da jornada de trabalho, false caso contrário
     */
    public boolean horarioEstaDentroJornadaTrabalho(Profissional profissional, LocalDateTime horarioAgendamento) {
        try {
            DiaSemana diaSemana = DiaSemana.fromLocalDateTime(horarioAgendamento);
            
            HorarioTrabalhoResponse horarioEntity = profissionalService.buscarHorarioDoDia(profissional.getId(), diaSemana);
            if (horarioEntity == null) {
                return false;
            }

            LocalTime horarioSolicitado = horarioAgendamento.toLocalTime();

            // Verificar se está dentro da jornada
            if (horarioSolicitado.isBefore(horarioEntity.getHoraInicio()) || 
                horarioSolicitado.isAfter(horarioEntity.getHoraFim())) {
                return false;
            }

            // Verificar conflito com pausa
            LocalTime inicioPausa = horarioEntity.getInicioPausa();
            LocalTime fimPausa = horarioEntity.getFimPausa();

            return !(inicioPausa != null && fimPausa != null && 
                    horarioSolicitado.isAfter(inicioPausa) && horarioSolicitado.isBefore(fimPausa));
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Versão que lança exceção da verificação de jornada de trabalho
     * 
     * @param profissional o profissional a ser verificado
     * @param horarioAgendamento o horário do agendamento
     * @throws IllegalArgumentException se o horário não estiver dentro da jornada de trabalho
     */
    public void verificarConflitoProfissional(Profissional profissional, LocalDateTime horarioAgendamento) {
        if (!horarioEstaDentroJornadaTrabalho(profissional, horarioAgendamento)) {
            DiaSemana diaSemana = DiaSemana.fromLocalDateTime(horarioAgendamento);
            throw new IllegalArgumentException(
                String.format("%s não está disponível no horário solicitado no(a) %s.", 
                    profissional.getNome(), diaSemana.getNome())
            );
        }
    }

    /**
     * Versão que lança exceção da verificação de conflito de horário
     * 
     * @param dataHora horário a ser verificado
     * @param profissionalId ID do profissional
     * @throws IllegalArgumentException se houver conflito de horário
     */
    public void verificarConflitoHorario(LocalDateTime dataHora, Integer profissionalId) {
        if (temConflitoDeHorario(dataHora, profissionalId)) {
            Profissional profissional = profissionalService.buscarEntidadePorId(profissionalId);
            throw new DuplicateException(
                String.format("Já existe um agendamento com %s para %s.", profissional.getNome(), 
                    dataHora.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")))
            );
        }
    }


    /**
     * Versão que lança exceção da verificação de alteração de status
     * 
     * @param agendamento o agendamento a ser verificado
     * @throws IllegalArgumentException se a alteração de status não for válida
     */
    public void verificarAlteracaoStatus(Agendamento agendamento) {
        var status = agendamento.getStatus();
        var horasAteAgendamento = Duration.between(
            LocalDateTime.now(clock),
            agendamento.getDataHora()
        ).toHours();

        boolean cancelandoComMenosDeDuasHoras = status.equals(StatusAgendamento.CANCELADO) && horasAteAgendamento <= 2;
        boolean confirmacaoDeAgendamentoPassado = status.equals(StatusAgendamento.CONFIRMADO) && horasAteAgendamento < 0;

        if (cancelandoComMenosDeDuasHoras) {
            throw new IllegalArgumentException("Não é permitido cancelar agendamentos com menos de 2 horas de antecedência.");
        } else if (confirmacaoDeAgendamentoPassado) {
            agendamento.setStatus(StatusAgendamento.CANCELADO);
            repository.save(agendamento);
            throw new IllegalArgumentException("Não é possível confirmar um agendamento que já passou.");
        }
    }

    /**
     * Regras de negócio para criar um agendamento:
     * 1. O cliente deve existir no sistema
     * 2. O profissional deve existir no sistema  
     * 3. O serviço deve existir no sistema
     * 4. Não pode haver conflito de horário com outros agendamentos
     * 5. O profissional deve estar disponível no horário solicitado
     * 6. O status inicial deve ser válido (normalmente PENDENTE)
     * 
     * @param request dados para criação do agendamento
     * @return agendamento criado com sucesso
     * @throws IllegalArgumentException se alguma regra de negócio for violada
     */
    public AgendamentoResponse criarAgendamento(CriarAgendamentoRequest request) {
        // Buscar entidades relacionadas
        Cliente cliente = clienteService.buscarEntidadePorId(request.getClienteId());
        
        Profissional profissional = profissionalService.buscarEntidadePorId(request.getProfissionalId());
        
        ServicoOferecido servico = servicoOferecidoService.buscarEntidadePorId(request.getServicoId());
        
        // Converter DTO para entidade
        Agendamento agendamento = mapper.toEntity(request, cliente, profissional, servico);
        
        // Apply full validation for new agendamentos
        this.verificarConflitoHorario(agendamento.getDataHora(), agendamento.getProfissional().getId());
        this.verificarConflitoProfissional(agendamento.getProfissional(), agendamento.getDataHora());
        this.verificarAlteracaoStatus(agendamento);
        
        // Salvar o agendamento
        Agendamento agendamentoSalvo = repository.save(agendamento);
        
        // Converter para DTO de resposta
        return mapper.toResponse(agendamentoSalvo);
    }


    /**
     * Regras de negócio para confirmar um agendamento:
     * 1. O agendamento deve existir
     * 2. O agendamento não pode estar no passado
     * 3. O agendamento deve estar em status que permita confirmação
     * 
     * @param id identificador do agendamento
     * @return agendamento confirmado
     * @throws IllegalArgumentException se a confirmação não for válida
     */
    public AgendamentoResponse confirmarAgendamento(Integer id) {
        Agendamento agendamento = buscarEntidadePorId(id);
        agendamento.setStatus(StatusAgendamento.CONFIRMADO);
        
        // Only validate status change rules for confirmation
        this.verificarAlteracaoStatus(agendamento);
        
        Agendamento agendamentoSalvo = repository.save(agendamento);
        return mapper.toResponse(agendamentoSalvo);
    }

    /**
     * Regras de negócio para cancelar um agendamento:
     * 1. O agendamento deve existir
     * 2. O cancelamento deve ser feito com pelo menos 2 horas de antecedência
     * 3. O agendamento deve estar em status que permita cancelamento
     * 
     * @param id identificador do agendamento
     * @return agendamento cancelado
     * @throws IllegalArgumentException se o cancelamento não for válido
     */
    public AgendamentoResponse cancelarAgendamento(Integer id) {
        Agendamento agendamento = buscarEntidadePorId(id);
        agendamento.setStatus(StatusAgendamento.CANCELADO);
        
        // Only validate status change rules for cancellation
        this.verificarAlteracaoStatus(agendamento);
        
        Agendamento agendamentoSalvo = repository.save(agendamento);
        return mapper.toResponse(agendamentoSalvo);
    }


/**
 * Lista todos os horários disponíveis para um serviço em uma data específica
 * (independente do profissional)
 * 
 * Regras de negócio:
 * 1. Deve haver pelo menos um profissional disponível no horário
 * 2. O horário deve estar dentro do funcionamento da barbearia
 * 3. Considera slots de 30 minutos
 * 4. Não retorna horários no passado (se for hoje)
 * 
 * @param data data no formato yyyy-MM-dd
 * @param servicoId identificador do serviço
 * @return lista de horários disponíveis no formato HH:mm
 */
public List<String> listarHorariosDisponiveis(String data, Integer servicoId) {
    LocalDate dataConsulta = LocalDate.parse(data);
    ServicoOferecido servico = servicoOferecidoService.buscarEntidadePorId(servicoId);
    
    // Buscar todos os profissionais que oferecem este serviço
    List<Profissional> profissionaisDoServico = profissionalService.buscarProfissionaisPorServico(servicoId)
    .stream()
    .map(item-> item.getProfissional())
    .toList();
    
    // Definir horário de funcionamento geral da barbearia (definir melhor dps)
    LocalTime inicioFuncionamento = LocalTime.of(8, 0);  // 08:00
    LocalTime fimFuncionamento = LocalTime.of(18, 00);    // 18:00
    
    List<String> horariosDisponiveis = new ArrayList<>();
    
    LocalTime horarioAtual = null;

    if(dataConsulta.equals(LocalDate.now(clock))){
        horarioAtual = LocalTime.now(clock);
        // como arredondar para o próximo slot de tempo?
    } else{
        horarioAtual = inicioFuncionamento;
    }

    // Gerar slots de 30 em 30 minutos
    boolean antesDoFechamento = horarioAtual.plusMinutes(servico.getDuracaoMinutos()).isBefore(fimFuncionamento) || 
           horarioAtual.plusMinutes(servico.getDuracaoMinutos()).equals(fimFuncionamento);

    while (antesDoFechamento) {
        LocalDateTime dataHoraConsulta = LocalDateTime.of(dataConsulta, horarioAtual);

        // Verificar se pelo menos um profissional está disponível neste horário
        boolean temProfissionalDisponivel = profissionaisDoServico.stream()
            .anyMatch(prof -> isProfissionalDisponivelNoHorario(prof, dataHoraConsulta));
        
        if (temProfissionalDisponivel) {
            horariosDisponiveis.add(horarioAtual.format(DateTimeFormatter.ofPattern("HH:mm")));
        }
        
        horarioAtual = horarioAtual.plusMinutes(30); // Slots de 30 minutos
        antesDoFechamento = horarioAtual.plusMinutes(servico.getDuracaoMinutos()).isBefore(fimFuncionamento) || 
               horarioAtual.plusMinutes(servico.getDuracaoMinutos()).equals(fimFuncionamento);
    }
    
    return horariosDisponiveis;
}

    /**
     * Verifica se um profissional está disponível em um horário específico
     * Combina verificação de jornada de trabalho + conflitos de agendamento
     * 
     * @param profissional o profissional a ser verificado
     * @param dataHora a data e horário do agendamento
     * @return true se o profissional está completamente disponível
     */
    private boolean isProfissionalDisponivelNoHorario(Profissional profissional, LocalDateTime dataHora) {
        try {
            // 1. Verificar se horário está dentro da jornada de trabalho
            if (!horarioEstaDentroJornadaTrabalho(profissional, dataHora)) {
                return false;
            }
            
            // 2. Verificar se não tem agendamento conflitante usando método otimizado
            return !temConflitoDeHorario(dataHora, profissional.getId());
        } catch (Exception e) {
            return false; // Se der erro, considera indisponível
        }
    }

    /**
     * Lista os profissionais disponíveis para um horário específico
     * 
     * Regras de negócio:
     * 1. O profissional deve oferecer o serviço solicitado
     * 2. O profissional deve estar disponível no horário (jornada + pausa)
     * 3. O profissional não pode ter agendamento conflitante
     * 
     * @param data data no formato yyyy-MM-dd
     * @param horario horário no formato HH:mm
     * @param servicoId identificador do serviço
     * @return lista de profissionais disponíveis
     */
    public List<ProfissionalResponse> listarProfissionaisDisponiveis(String data, String horario, Integer servicoId) {
        LocalDate dataConsulta = LocalDate.parse(data);
        LocalTime horarioConsulta = LocalTime.parse(horario);
        LocalDateTime dataHoraConsulta = LocalDateTime.of(dataConsulta, horarioConsulta);
        
        List<Profissional> profissionaisDoServico = profissionalService.buscarProfissionaisPorServico(servicoId)
            .stream()
            .map(item -> item.getProfissional())
            .toList();
        
        return profissionaisDoServico
        .stream()
        .filter(prof -> isProfissionalDisponivelNoHorario(prof, dataHoraConsulta))
        .map(ProfissionalMapper::toResponse)
        .toList();
    }
}
