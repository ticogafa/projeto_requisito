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
        if (comando.getProfissionalId() == null) {
            throw new IllegalArgumentException("ID do profissional é obrigatório");
        }
        
        if (comando.getNovasJornadas() != null) {
            for (JornadaResumo jornada : comando.getNovasJornadas()) {
                if (jornada.isAtivo()) {
                    if (jornada.getHoraInicio() == null || jornada.getHoraFim() == null) {
                        throw new IllegalArgumentException("Horário de início e fim são obrigatórios");
                    }
                    if (!jornada.getHoraInicio().isBefore(jornada.getHoraFim())) {
                        throw new IllegalArgumentException("Horário de fim deve ser posterior ao início");
                    }
                    
                    if (jornada.getIntervaloInicio() != null && jornada.getIntervaloFim() != null) {
                        if (!jornada.getIntervaloInicio().isBefore(jornada.getIntervaloFim())) {
                            throw new IllegalArgumentException("Início do intervalo deve ser antes do fim");
                        }
                        if (jornada.getIntervaloInicio().isBefore(jornada.getHoraInicio()) || 
                            jornada.getIntervaloFim().isAfter(jornada.getHoraFim())) {
                            throw new IllegalArgumentException("Intervalo deve estar dentro do horário de trabalho");
                        }
                    }
                }
            }
        }

        repositorio.atualizarJornadas(comando.getProfissionalId(), comando.getNovasJornadas());
    }

    public List<JornadaResumo> obterJornada(Integer profissionalId) {
        return repositorio.listarJornadas(profissionalId);
    }
}
