package com.cesarschool.barbearia.infraestrutura.persistencia.jpa;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cesarschool.barbearia.dominio.principal.cliente.Cliente;
import com.cesarschool.barbearia.dominio.principal.cliente.ClienteRepositorio;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidade JPA para Cliente.
 * Representa um cliente da barbearia.
 */
@Entity
@Table(name = "CLIENTE")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClienteJpa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "NOME", nullable = false, length = 100)
    private String nome;

    @Column(name = "CPF", nullable = false, unique = true, length = 11)
    private String cpf;

    @Column(name = "EMAIL", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "TELEFONE", nullable = false, length = 20)
    private String telefone;

    @Column(name = "DATA_NASCIMENTO")
    private LocalDate dataNascimento;

    @Column(name = "ENDERECO", length = 255)
    private String endereco;

    @Column(name = "PONTOS", nullable = false)
    @Builder.Default
    private Integer pontos = 0;

    @Column(name = "ATIVO", nullable = false)
    @Builder.Default
    private Boolean ativo = true;
}

/**
 * Repositório Spring Data JPA para ClienteJpa.
 */
@Repository
interface ClienteJpaRepository extends JpaRepository<ClienteJpa, Integer> {
    Optional<ClienteJpa> findByCpf(String cpf);
    Optional<ClienteJpa> findByEmail(String email);
    Optional<ClienteJpa> findByTelefone(String telefone);
    List<ClienteJpa> findByNomeContainingIgnoreCase(String nome);
    boolean existsByCpf(String cpf);
    boolean existsByEmail(String email);
}

/**
 * Implementação do repositório de domínio para Cliente.
 * Realiza a conversão entre entidades JPA e entidades de domínio.
 */
@Repository
class ClienteRepositorioImpl implements ClienteRepositorio {
    
    @Autowired
    private ClienteJpaRepository repositorio;
    
    @Autowired
    private JpaMapeador mapeador;
    
    @Override
    public Cliente salvar(Cliente cliente) {
        var clienteJpa = mapeador.map(cliente, ClienteJpa.class);
        var salvo = repositorio.save(clienteJpa);
        return mapeador.map(salvo, Cliente.class);
    }
    
    @Override
    public Cliente buscarPorId(Integer id) {
        var clienteJpa = repositorio.findById(id).orElse(null);
        return clienteJpa != null ? mapeador.map(clienteJpa, Cliente.class) : null;
    }
    
    @Override
    public List<Cliente> listarTodos() {
        var clientesJpa = repositorio.findAll();
        return clientesJpa.stream()
            .map(cj -> mapeador.map(cj, Cliente.class))
            .toList();
    }
    
    @Override
    public void remover(Integer id) {
        repositorio.deleteById(id);
    }
    
    @Override
    public Optional<Cliente> buscarPorNome(String nome) {
        var clientesJpa = repositorio.findByNomeContainingIgnoreCase(nome);
        if (clientesJpa.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(mapeador.map(clientesJpa.get(0), Cliente.class));
    }
    
    @Override
    public Optional<Cliente> buscarPorEmail(String email) {
        return repositorio.findByEmail(email)
            .map(cj -> mapeador.map(cj, Cliente.class));
    }
    
    @Override
    public Optional<Cliente> buscarPorTelefone(String telefone) {
        return repositorio.findByTelefone(telefone)
            .map(cj -> mapeador.map(cj, Cliente.class));
    }
    
    @Override
    public Optional<Cliente> buscarPorCpf(String cpf) {
        return repositorio.findByCpf(cpf)
            .map(cj -> mapeador.map(cj, Cliente.class));
    }
}

