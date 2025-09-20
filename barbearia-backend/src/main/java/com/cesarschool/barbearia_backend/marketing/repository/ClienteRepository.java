package com.cesarschool.barbearia_backend.marketing.repository;



import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.cesarschool.barbearia_backend.marketing.model.Cliente;
import com.cesarschool.barbearia_backend.profissionais.model.Profissional;


@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Integer> {
    

    @Query("SELECT p from Cliente p WHERE LOWER(p.nome) = LOWER(:nome)")
    Optional<Cliente> findByNome(@Param("nome") String nome);

    @Query("SELECT p from Cliente p WHERE LOWER(p.email) = LOWER(:email)")
    Optional<Cliente> findByEmail(@Param("email") String email);

    @Query("SELECT p from Cliente p WHERE LOWER(p.telefone) = LOWER(:telefone)")
    Optional<Cliente> findByTelefone(@Param("telefone") String telefone);

    @Query("SELECT p from Cliente p WHERE LOWER(p.cpf) = LOWER(:cpf)")
    Optional<Cliente> findByCpf(@Param("cpf") String cpf);
}