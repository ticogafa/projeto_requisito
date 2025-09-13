package com.cesarschool.barbearia_backend.profissionais.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cesarschool.barbearia_backend.profissionais.model.Profissional;

public interface ProfissionalRepository extends JpaRepository<Profissional, UUID>{

}
