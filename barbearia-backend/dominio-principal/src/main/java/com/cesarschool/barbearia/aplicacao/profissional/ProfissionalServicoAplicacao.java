package com.cesarschool.barbearia.aplicacao.profissional;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cesarschool.barbearia.dominio.principal.profissional.ProfissionalRepositorio;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProfissionalServicoAplicacao {
    
    private final ProfissionalRepositorio repositorio;
    
    @Transactional
    public void atualizarJornada(AtualizarJornadaComando comando) {

        validarProfissional(comando);

        if (comando.getNovasJornadas() == null || comando.getNovasJornadas().isEmpty()) {
            return;
        }

        for (JornadaResumo jornada : comando.getNovasJornadas()) {

            if (jornadaInativa(jornada)) {
                continue;
            }

            validarHorarioTrabalho(jornada);
            validarIntervalo(jornada);
        }

        repositorio.atualizarJornadas(
            comando.getProfissionalId(),
            comando.getNovasJornadas()
        );
    }

    private void validarProfissional(AtualizarJornadaComando comando) {
        if (comando.getProfissionalId() == null) {
            throw new IllegalArgumentException("ID do profissional é obrigatório");
        }
    }

    private boolean jornadaInativa(JornadaResumo jornada) {
        return !jornada.isAtivo();
    }

    private void validarHorarioTrabalho(JornadaResumo jornada) {

        boolean horarioNaoInformado =
            jornada.getHoraInicio() == null || jornada.getHoraFim() == null;

        boolean horarioInvalido =
            jornada.getHoraInicio() != null &&
            jornada.getHoraFim() != null &&
            !jornada.getHoraInicio().isBefore(jornada.getHoraFim());

        if (horarioNaoInformado) {
            throw new IllegalArgumentException("Horário de início e fim são obrigatórios");
        }

        if (horarioInvalido) {
            throw new IllegalArgumentException("Horário de fim deve ser posterior ao início");
        }
    }

    private void validarIntervalo(JornadaResumo jornada) {

        boolean possuiIntervalo =
            jornada.getIntervaloInicio() != null &&
            jornada.getIntervaloFim() != null;

        if (!possuiIntervalo) {
            return;
        }

        boolean intervaloInvalido =
            !jornada.getIntervaloInicio().isBefore(jornada.getIntervaloFim());

        boolean intervaloForaDoHorario =
            jornada.getIntervaloInicio().isBefore(jornada.getHoraInicio()) ||
            jornada.getIntervaloFim().isAfter(jornada.getHoraFim());

        if (intervaloInvalido) {
            throw new IllegalArgumentException("Início do intervalo deve ser antes do fim");
        }

        if (intervaloForaDoHorario) {
            throw new IllegalArgumentException("Intervalo deve estar dentro do horário de trabalho");
        }
    }

    public List<JornadaResumo> obterJornada(Integer profissionalId) {
        return repositorio.listarJornadas(profissionalId);
    }
}
