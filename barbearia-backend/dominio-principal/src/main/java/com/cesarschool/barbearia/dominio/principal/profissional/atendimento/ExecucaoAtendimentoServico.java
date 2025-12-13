package com.cesarschool.barbearia.dominio.principal.profissional.atendimento;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cesarschool.barbearia.dominio.principal.cliente.ClienteId;
import com.cesarschool.barbearia.dominio.principal.cliente.pontos.PontosFidelidadeServico;
import com.cesarschool.barbearia.dominio.principal.profissional.ProfissionalId;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExecucaoAtendimentoServico {

    private final ExecucaoAtendimentoRepositorio repositorio;
    private final PontosFidelidadeServico pontosFidelidadeServico;

    @Transactional
    public ExecucaoAtendimento registrarExecucao(Integer profissionalId, Integer clienteId, BigDecimal valor, LocalDateTime inicio, LocalDateTime fim) {
        ProfissionalId profId = new ProfissionalId(profissionalId);
        ClienteId cliId = clienteId != null ? new ClienteId(clienteId) : null;
        
        // Cria o atendimento já com o valor (que é final)
        ExecucaoAtendimento execucao = ExecucaoAtendimento.iniciar(profId, valor.doubleValue(), inicio);
        
        // Finaliza com a data fim
        execucao.finalizar(fim);
        
        repositorio.salvar(execucao);

        // Credita pontos de fidelidade: 1 ponto por real (valor inteiro)
        if (cliId != null) {
            pontosFidelidadeServico.creditar(cliId, valor);
        }
        
        return execucao;
    }
}
