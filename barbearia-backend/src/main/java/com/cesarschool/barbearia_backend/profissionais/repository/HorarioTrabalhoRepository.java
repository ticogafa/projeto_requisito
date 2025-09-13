package com.cesarschool.barbearia_backend.profissionais.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cesarschool.barbearia_backend.profissionais.model.HorarioTrabalho;

public interface HorarioTrabalhoRepository extends JpaRepository<HorarioTrabalho, UUID>{

}
