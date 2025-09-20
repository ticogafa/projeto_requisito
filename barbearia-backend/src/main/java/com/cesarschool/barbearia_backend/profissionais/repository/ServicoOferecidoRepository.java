package com.cesarschool.barbearia_backend.profissionais.repository;



import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cesarschool.barbearia_backend.profissionais.model.ServicoOferecido;

public interface ServicoOferecidoRepository extends JpaRepository<ServicoOferecido, Integer>{

    Optional<ServicoOferecido> findByNome(String nome);
}
