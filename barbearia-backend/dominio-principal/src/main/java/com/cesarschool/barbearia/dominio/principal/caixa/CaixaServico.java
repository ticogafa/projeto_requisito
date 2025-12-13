package com.cesarschool.barbearia.dominio.principal.caixa;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CaixaServico {

    private final CaixaRepository caixaRepository;

    public Caixa registrarLancamento(String descricao, java.math.BigDecimal valor, Caixa.TipoLancamento tipo) {
        Caixa lancamento = new Caixa();
        lancamento.setDescricao(descricao);
        lancamento.setValor(valor);
        lancamento.setTipo(tipo);
        lancamento.setData(LocalDateTime.now());
        return caixaRepository.save(lancamento);
    }

    public List<Caixa> getLancamentos() {
        return caixaRepository.findAll();
    }
}
