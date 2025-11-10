package com.cesarschool.barbearia.infraestrutura.persistencia.jpa;

import static jakarta.persistence.GenerationType.*;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cesarschool.barbearia.dominio.principal.servico.ServicoOferecido;
import com.cesarschool.barbearia.dominio.principal.servico.ServicoOferecidoRepositorio;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidade JPA representando um serviço oferecido pela barbearia.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "SERVICO_OFERECIDO")
class ServicoOferecidoJpa {
    
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "NOME", nullable = false, length = 100)
    private String nome;

    @Column(name = "PRECO", nullable = false, precision = 10, scale = 2)
    private BigDecimal preco;

    @Column(name = "DESCRICAO", nullable = false, length = 500)
    private String descricao;

    @Column(name = "DURACAO_MINUTOS", nullable = false)
    private Integer duracaoMinutos;

    public ServicoOferecidoJpa(
            String nome,
            BigDecimal preco,
            String descricao,
            Integer duracaoMinutos
        ) {
        setNome(nome);
        setPreco(preco);
        setDescricao(descricao);
        setDuracaoMinutos(duracaoMinutos);
    }
}

interface ServicoOferecidoJpaRepository extends JpaRepository<ServicoOferecidoJpa, Integer> {    
}

@Repository
class ServicoOferecidoJpaRepositorioImpl implements ServicoOferecidoRepositorio {

    @Autowired
    private ServicoOferecidoJpaRepository servicoOferecidoJpaRepository;
    
    @Autowired 
    JpaMapeador mapeador;

    @Override
    public List<ServicoOferecido> listarTodos() {
        return servicoOferecidoJpaRepository
        .findAll()
        .stream()
        .map(jpa -> mapeador.map(jpa, ServicoOferecido.class))
        .toList();
    }

    @Override
    public ServicoOferecido buscarPorNome(String nome) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean estaQualificado(String nomeServico, String nomeProfissional) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void salvarAssociacao(String nomeServico, String nomeProfissional) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public ServicoOferecido buscarPorId(Integer id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void remover(Integer id) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public ServicoOferecido salvar(ServicoOferecido entity) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isAtivo(Integer servicoId) {
        // TODO: Implementar lógica de verificação de status ativo
        // Por enquanto, retorna true para não bloquear operações
        return true;
    }

    
}