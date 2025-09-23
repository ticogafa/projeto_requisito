package com.cesarschool.barbearia_backend.atendimento.repository;

import com.cesarschool.barbearia_backend.atendimento.model.Atendimento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AtendimentoRepository extends JpaRepository<Atendimento, Long> {
}