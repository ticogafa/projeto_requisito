package com.cesarschool.barbearia.infraestrutura.persistencia.jpa;

import java.time.LocalDateTime;
import java.util.List;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cesarschool.barbearia.dominio.principal.profissional.ProfissionalId;
import com.cesarschool.barbearia.dominio.principal.profissional.atendimento.ExecucaoAtendimento;
import com.cesarschool.barbearia.dominio.principal.profissional.atendimento.ExecucaoAtendimentoRepositorio;

import com.cesarschool.barbearia.dominio.compartilhado.valueobjects.ExecucaoAtendimentoId;

// Interface Spring Data
interface ExecucaoAtendimentoSpringRepository extends JpaRepository<ExecucaoAtendimentoJpa, String> {
    // Busca atendimentos onde o INICIO está entre as datas fornecidas
    List<ExecucaoAtendimentoJpa> findByProfissionalIdAndInicioBetween(String profissionalId, LocalDateTime inicio, LocalDateTime fim);
}

@Repository
public class ExecucaoAtendimentoJpaRepositorioImpl implements ExecucaoAtendimentoRepositorio {

    private static final Logger logger = LoggerFactory.getLogger(ExecucaoAtendimentoJpaRepositorioImpl.class);

    @Autowired
    private ExecucaoAtendimentoSpringRepository springRepo;
    
    @Autowired
    private JpaMapeador mapeador;

    @Override
    public ExecucaoAtendimento salvar(ExecucaoAtendimento execucao) {
        logger.info("Salvando execucao de atendimento: {}", execucao);
        ExecucaoAtendimentoJpa jpa = ExecucaoAtendimentoJpa.builder()
            .id(execucao.getId() != null ? execucao.getId().toString() : null)
            .profissionalId(execucao.getProfissionalId().getValor())
            .valor(execucao.getValor())
            .inicio(execucao.getInicio())
            .fim(execucao.getFim())
            .build();
        ExecucaoAtendimentoJpa saved = springRepo.save(jpa);
        return mapeador.map(saved, ExecucaoAtendimento.class);
    }

    @Override
    public Optional<ExecucaoAtendimento> porId(ExecucaoAtendimentoId id) {
        return springRepo.findById(id.toString())
                .map(jpa -> mapeador.map(jpa, ExecucaoAtendimento.class));
    }

    @Override
    public List<ExecucaoAtendimento> porProfissionalNoPeriodo(ProfissionalId profissionalId, LocalDateTime inicio, LocalDateTime fim) {
        logger.info("Buscando execucoes por profissional {} no periodo {} a {}", profissionalId, inicio, fim);
        return springRepo.findByProfissionalIdAndInicioBetween(profissionalId.toString(), inicio, fim)
                .stream()
                .map(jpa -> mapeador.map(jpa, ExecucaoAtendimento.class)) // Converte JPA -> Dominio
                .toList();
    }
}
