package com.cesarschool.barbearia_backend.agendamento.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.cesarschool.barbearia_backend.agendamento.model.Agendamento;
import com.cesarschool.barbearia_backend.agendamento.repository.AgendamentoRepository;
import com.cesarschool.barbearia_backend.common.enums.DiaSemana;
import com.cesarschool.barbearia_backend.common.enums.StatusAgendamento;
import com.cesarschool.barbearia_backend.profissionais.model.Profissional;
import com.cesarschool.barbearia_backend.profissionais.repository.ProfissionalRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class AgendamentoService {
    private final AgendamentoRepository repository;
    private final ProfissionalRepository profissionalRepository;

    public Agendamento findById(UUID id){
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
        String horario = dataHora.toString();
        throw new IllegalArgumentException(
            String.format("Já existe um agendamento com %s no horário %s.", nomeProfissional, horario)
        );
    }
    }

    public void verificarConflitoProfissional(Profissional profissional, LocalDateTime horarioAgendamento) {
        var diaSemana = DiaSemana.fromLocalDateTime(horarioAgendamento);
        profissionalRepository.findHorarioTrabalhoByProfissionalAndDiaSemana(profissional.getId(), diaSemana)
            .ifPresentOrElse(value -> {
                var inicioPausa = value.getFimPausa();
                var fimPausa = value.getInicioPausa();
                var horario = horarioAgendamento.toLocalTime();
                if (horario.isAfter(inicioPausa) && horario.isBefore(fimPausa)) {
                    throw new IllegalArgumentException(
                        String.format("%s não está disponível no horário solicitado.", profissional.getNome())
                    );
                }
            }, () -> {
                throw new IllegalArgumentException(
                    String.format("O profissional %s não possui horário de trabalho cadastrado para o dia selecionado.", profissional.getNome())
                );
            });
    }


    public void verificarAlteracaoStatus(Agendamento agendamento) {
        if(!agendamento.getStatus().equals(StatusAgendamento.CANCELADO)){
            return;
        }
        var horasAteAgendamento = Duration.between(
            LocalDateTime.now(),
            agendamento.getDataHora()
        ).toHours();
        if(horasAteAgendamento <= 2)
            throw new IllegalArgumentException("Não é permitido cancelar agendamentos com menos de 2 horas de antecedência.");
    }

    public Agendamento save(Agendamento agendamento) throws IllegalArgumentException{
        // -- • Um Agendamento só pode ser criado se o Horário Disponível estiver livre
        // -- • Um Agendamento não pode ser criado em um horário fora da jornada de trabalho do profissional
        // -- • Um Agendamento só pode ser cancelado até 2 horas antes do horário
        // -- • Se um Agendamento não especificar o profissional, o sistema deve atribuir o primeiro com horário livre

        this.verificarConflitoHorario(agendamento);
        this.verificarConflitoProfissional(agendamento.getProfissional(), agendamento.getDataHora());
        this.verificarAlteracaoStatus(agendamento);
        
        // Gera um token de confirmação para novos agendamentos
        if (agendamento.getId() == null) {
            agendamento.setTokenConfirmacao(UUID.randomUUID().toString());
        }
        
        return repository.save(agendamento);
    }

    public void delete(Agendamento agendamento){
        repository.delete(agendamento);
    }
}
