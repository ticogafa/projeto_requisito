package com.cesarschool.barbearia_backend.agendamento.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cesarschool.barbearia_backend.agendamento.model.Agendamento;
import java.util.List;
import java.time.LocalDateTime;

@Repository
public interface AgendamentoRepository extends JpaRepository<Agendamento, UUID>{

    @Query(
        "SELECT a FROM agendamento a WHERE a.data_hora = :data_hora AND a.profissional.id = :profissional_id"
    )
    List<Agendamento> findByDataHoraAndProfissional(
        @Param("data_hora") LocalDateTime dataHora,
        @Param("profissional_id") UUID profissionalId
    );

}
