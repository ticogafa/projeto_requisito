package com.cesarschool.barbearia.infraestrutura.persistencia.jpa;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cesarschool.barbearia.dominio.principal.cliente.ClienteId;
import com.cesarschool.barbearia.dominio.principal.cliente.caixa.Lancamento;
import com.cesarschool.barbearia.dominio.principal.cliente.caixa.LancamentoId;
import com.cesarschool.barbearia.dominio.principal.cliente.caixa.LancamentoRepositorio;
import com.cesarschool.barbearia.dominio.principal.cliente.caixa.StatusLancamento;

// Interface interna do Spring Data
interface LancamentoSpringRepository extends JpaRepository<LancamentoJpa, String> {
    List<LancamentoJpa> findByClienteIdAndStatus(String clienteId, StatusLancamento status);
    List<LancamentoJpa> findByClienteId(String clienteId);
}

@Repository
public class LancamentoJpaRepositorioImpl implements LancamentoRepositorio {

    @Autowired
    private LancamentoSpringRepository springRepo;

    @Autowired
    private JpaMapeador mapeador; // Assumindo que você usa o mesmo mapeador do Profissional

    @Override
    public void salvar(Lancamento lancamento) {
        // Converte Domínio -> JPA
        LancamentoJpa jpa = LancamentoJpa.builder()
                .id(lancamento.getId().toString())
                .clienteId(lancamento.getClienteId() != null ? lancamento.getClienteId().toString() : null)
                .status(lancamento.getStatus())
                .descricao(lancamento.getDescricao())
                .valor(lancamento.getValor())
                .meioPagamento(lancamento.getMeioPagamento())
                .quando(lancamento.getQuando())
                .build();

        springRepo.save(jpa);
    }

    @Override
    public Optional<Lancamento> buscarPorId(LancamentoId id) {
        return springRepo.findById(id.toString())
                .map(this::converterParaDominio);
    }

    @Override
    public List<Lancamento> buscarTodos() {
        return springRepo.findAll().stream()
                .map(this::converterParaDominio)
                .toList();
    }

    @Override
    public List<Lancamento> buscarPendentesPorCliente(ClienteId clienteId) {
        return springRepo.findByClienteIdAndStatus(clienteId.toString(), StatusLancamento.PENDENTE).stream()
                .map(this::converterParaDominio)
                .toList();
    }

    // Método auxiliar para converter JPA -> Domínio
    // Como o construtor de Lancamento é privado, precisamos usar o Mapeador ou Reflection
    private Lancamento converterParaDominio(LancamentoJpa jpa) {
        // Opção 1: Se o seu JpaMapeador funcionar para Lancamento:
        return mapeador.map(jpa, Lancamento.class);
        
        /* Opção 2 (Se não tiver mapeador): 
           Você precisará criar um método 'reconstituir' estático na classe Lancamento 
           ou usar Reflection aqui, pois o construtor é privado.
        */
    }
}

