package com.barbearia.profissionais.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.barbearia.profissionais.model.HorarioTrabalho;

public interface HorarioTrabalhoRepository extends JpaRepository<HorarioTrabalho, UUID>{

}
