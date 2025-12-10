package com.cesarschool.barbearia.infraestrutura.persistencia.jpa;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cesarschool.barbearia.dominio.principal.profissional.ProfissionalId;
import com.cesarschool.barbearia.dominio.principal.profissional.atendimento.ExecucaoAtendimento;
import com.cesarschool.barbearia.dominio.principal.profissional.atendimento.ExecucaoAtendimentoRepositorio;

// Interface Spring Data
interface ExecucaoAtendimentoSpringRepository extends JpaRepository<ExecucaoAtendimentoJpa, String> {
    // Busca atendimentos onde o INICIO está entre as datas fornecidas
    List<ExecucaoAtendimentoJpa> findByProfissionalIdAndInicioBetween(String profissionalId, LocalDateTime inicio, LocalDateTime fim);
}

@Repository
public class ExecucaoAtendimentoJpaRepositorioImpl implements ExecucaoAtendimentoRepositorio {

    @Autowired
    private ExecucaoAtendimentoSpringRepository springRepo;
    
    @Autowired
    private JpaMapeador mapeador;

    @Override
    public void salvar(ExecucaoAtendimento execucao) {
        ExecucaoAtendimentoJpa jpa = ExecucaoAtendimentoJpa.builder()
            .id(execucao.getId().toString())
            .profissionalId(execucao.getProfissionalId().toString())
            .valor(execucao.getValor())
            .inicio(execucao.getInicio())
            .fim(execucao.getFim())
            .build();
        springRepo.save(jpa);
    }

    @Override
    public List<ExecucaoAtendimento> porProfissionalNoPeriodo(Prof
        return springRepo.findByProfissionalIdAndInicioBetween(profissionalId.toString(), inicio, fim)
                .stream()
                .map(jpa -> mapeador.map(jpa, ExecucaoAtendimento.class)) // Converte JPA -> Dominio
                .toList();
    }
}
