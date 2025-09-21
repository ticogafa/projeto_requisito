package com.cesarschool.barbearia_backend.helper;

import com.cesarschool.barbearia_backend.common.enums.DiaSemana;
import com.cesarschool.barbearia_backend.common.enums.StatusAgendamento;
import com.cesarschool.barbearia_backend.common.model.Cpf;
import com.cesarschool.barbearia_backend.common.model.Email;
import com.cesarschool.barbearia_backend.common.model.Telefone;
import com.cesarschool.barbearia_backend.agendamento.model.Agendamento;
import com.cesarschool.barbearia_backend.marketing.model.Cliente;
import com.cesarschool.barbearia_backend.profissionais.model.Profissional;
import com.cesarschool.barbearia_backend.profissionais.model.ProfissionalServico;
import com.cesarschool.barbearia_backend.profissionais.model.HorarioTrabalho;
import com.cesarschool.barbearia_backend.profissionais.model.ServicoOferecido;

import com.cesarschool.barbearia_backend.marketing.repository.ClienteRepository;
import com.cesarschool.barbearia_backend.profissionais.repository.*;

import lombok.RequiredArgsConstructor;

import com.cesarschool.barbearia_backend.agendamento.repository.AgendamentoRepository;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.*;
import java.time.temporal.TemporalAdjusters;

@Component
@Transactional
@RequiredArgsConstructor
public class TestEntityFactory {

    private final ClienteRepository clienteRepo;
    private final ProfissionalRepository profissionalRepo;
    private final HorarioTrabalhoRepository horarioRepo;
    private final ServicoOferecidoRepository servicoRepo;
    private final AgendamentoRepository agendamentoRepo;
    private final ProfissionalServicoRepository profissionalServicoRepository;
    private final Clock clock;


    public Cliente saveCliente(String nome, String email, String cpf, String tel) {
        Cliente c = new Cliente();
        c.setNome(nome);
        c.setEmail(new Email(email));
        c.setTelefone(new Telefone(tel));
        c.setCpf(new Cpf(cpf));

        return clienteRepo.save(c);
    }

    public Profissional saveProfissionalComJornada(String nome, String email, String cpf, String tel,
            LocalTime inicio, LocalTime fim,
            DiaSemana... dias) {
        Profissional p = new Profissional();
        p.setNome(nome);
        p.setEmail(new Email(email));
        p.setTelefone(new Telefone(tel));
        p.setCpf(new Cpf(cpf));
        p = profissionalRepo.save(p);

        for (DiaSemana d : dias) {
            HorarioTrabalho h = new HorarioTrabalho();
            h.setProfissional(p);
            h.setDiaSemana(d); // ajuste tipo conforme entidade (Enum/Integer)
            h.setHoraInicio(inicio);
            h.setHoraFim(fim);
            horarioRepo.save(h);
            System.out.println("Horário salvo pra joão barbeiro");
        }
        return p;
    }

    public ServicoOferecido saveServico(String nome, BigDecimal preco, int duracaoMin, String descricao) {
        ServicoOferecido s = new ServicoOferecido();
        s.setNome(nome);
        s.setPreco(preco);
        s.setDuracaoMinutos(duracaoMin);
        s.setDescricao(descricao);
        return servicoRepo.save(s);
    }

    public Agendamento saveAgendamento(Cliente c, Profissional p, ServicoOferecido s, LocalDateTime dataHora,
            StatusAgendamento status, String obs) {
        Agendamento a = new Agendamento();
        a.setCliente(c);
        a.setProfissional(p);
        a.setServico(s);
        a.setDataHora(dataHora);
        a.setStatus(status);
        a.setObservacoes(obs);
        return agendamentoRepo.save(a);
    }

    public ProfissionalServico saveProfissionalServico(Profissional p, ServicoOferecido s){
        var sp = new ProfissionalServico();
        sp.setProfissional(p);
        sp.setServicoOferecido(s);
        return profissionalServicoRepository.save(sp);

    }

    public LocalDateTime proximaData(DayOfWeek dia, int hora, int minuto) {
        LocalDate base = LocalDate.now(clock);
        LocalDate d = base.with(TemporalAdjusters.nextOrSame(dia));
        return d.atTime(hora, minuto);
    }
}
