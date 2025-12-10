package com.cesarschool.barbearia.infraestrutura.persistencia.jpa;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cesarschool.barbearia.dominio.principal.profissional.ProfissionalId;
import com.cesarschool.barbearia.dominio.principal.profissional.avaliacao.Avaliacao;
import com.cesarschool.barbearia.dominio.principal.profissional.avaliacao.AvaliacaoRepositorio;

// Interface Spring Data
interface AvaliacaoSpringRepository extends JpaRepository<AvaliacaoJpa, String> {
    List<AvaliacaoJpa> findByProfissionalIdAndDataBetween(String profissionalId, LocalDateTime inicio, LocalDateTime fim);
}

@Repository
public class AvaliacaoJpaRepositorioImpl implements AvaliacaoRepositorio {

    @Autowired
    private AvaliacaoSpringRepository springRepo;
    
    @Autowired
    private JpaMapeador mapeador;

    @Override
    public void salvar(Avaliacao avaliacao) {
        AvaliacaoJpa jpa = AvaliacaoJpa.builder()
            .id(avaliacao.getId().toString())
            .profissionalId(avaliacao.getProfissionalId().toString())
            .nota(avaliacao.getNota().getValue()) // Extrai o int da Nota
            .data(avaliacao.getData())
            .build();
        springRepo.save(jpa);
    }

    @Override
    public List<Avaliacao> porProfissionalNoPeriodo(ProfissionalId profissionalId, LocalDateTime inicio, LocalDateTime fim) {
        return springRepo.findByProfissionalIdAndDataBetween(profissionalId.toString(), inicio, fim)
                .stream()
                .map(jpa -> mapeador.map(jpa, Avaliacao.class))
                .toList();
    }
}