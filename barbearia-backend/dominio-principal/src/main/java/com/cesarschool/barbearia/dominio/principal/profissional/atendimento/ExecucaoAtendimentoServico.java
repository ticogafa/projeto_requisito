package com.cesarschool.barbearia.dominio.principal.profissional.atendimento;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cesarschool.barbearia.dominio.principal.profissional.ProfissionalId;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExecucaoAtendimentoServico {

    private final ExecucaoAtendimentoRepositorio repositorio;

    @Transactional
    public ExecucaoAtendimento registrarExecucao(Integer profissionalId, BigDecimal valor, LocalDateTime inicio, LocalDateTime fim) {
        ProfissionalId profId = new ProfissionalId(profissionalId);
        
        // Cria o atendimento já com o valor (que é final)
        ExecucaoAtendimento execucao = ExecucaoAtendimento.iniciar(profId, valor.doubleValue(), inicio);
        
        // Finaliza com a data fim
        execucao.finalizar(fim);
        
        repositorio.salvar(execucao);
        
        return execucao;
    }
}
