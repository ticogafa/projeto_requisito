package com.cesarschool.barbearia_backend.agendamento.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cesarschool.barbearia_backend.agendamento.model.Agendamento;
import java.util.List;
import java.time.LocalDateTime;

public interface AgendamentoRepositorio{

    boolean existeAgendamentoParaProfissional(Integer profissionalId, LocalDateTime dataHora);

    List<Agendamento> buscarPorData(LocalDateTime dataHora);
}
