package com.cesarschool.barbearia.dominio.principal.profissional.avaliacao;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cesarschool.barbearia.dominio.principal.profissional.ProfissionalId;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AvaliacaoServico {

    private final AvaliacaoRepositorio repositorio;

    @Transactional
    public Avaliacao registrarAvaliacao(Integer profissionalId, int notaValor) {
        ProfissionalId profId = new ProfissionalId(profissionalId);
        Nota nota = new Nota(notaValor);
        
        Avaliacao avaliacao = new Avaliacao(
            AvaliacaoId.novo(),
            profId,
            nota,
            LocalDateTime.now()
        );
        
        repositorio.salvar(avaliacao);
        
        return avaliacao;
    }
}
