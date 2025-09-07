package com.barbearia.profissionais.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.barbearia.profissionais.model.Profissional;

public interface ProfissionalRepository extends JpaRepository<Profissional, UUID>{

}
