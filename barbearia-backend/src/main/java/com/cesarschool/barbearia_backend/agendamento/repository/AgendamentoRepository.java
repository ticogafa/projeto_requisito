package com.cesarschool.barbearia_backend.agendamento.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cesarschool.barbearia_backend.agendamento.model.Agendamento;
import java.util.List;
import java.time.LocalDateTime;

@Repository
public interface AgendamentoRepository extends JpaRepository<Agendamento, Integer>{

    @Query(
        "SELECT a FROM Agendamento a WHERE a.dataHora = :dataHora AND a.profissional.id = :profissionalId"
    )
    List<Agendamento> findByDataHoraAndProfissional(
        @Param("dataHora") LocalDateTime dataHora,
        @Param("profissionalId") Integer profissionalId
    );

}
