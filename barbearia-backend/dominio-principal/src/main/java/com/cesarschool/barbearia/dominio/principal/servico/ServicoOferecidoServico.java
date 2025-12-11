package com.cesarschool.barbearia.dominio.principal.servico;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cesarschool.barbearia.dominio.compartilhado.utils.Validacoes;
import com.cesarschool.barbearia.dominio.principal.servico.eventos.ServicoOferecidoEvent;
import com.cesarschool.barbearia.dominio.principal.servico.eventos.ServicoOferecidoEvent.TipoAcao;

import lombok.RequiredArgsConstructor;

/**
 * Domain Service para ServicoOferecido.
 * Responsável pelas regras de negócio de serviços, validações e disparo de eventos.
 */
@Service
@RequiredArgsConstructor
public class ServicoOferecidoServico {
    
    private final ServicoOferecidoRepositorio repositorio;
    private final ApplicationEventPublisher publicadorEventos;

    public ServicoOferecido buscarPorId(Integer id) {
        Validacoes.validarObjetoObrigatorio(id, "ID do serviço");
        ServicoOferecido servico = repositorio.buscarPorId(id);
        
        if (servico == null) {
            throw new IllegalArgumentException("Serviço não encontrado com ID: " + id);
        }
        return servico;
    }

    @Transactional
    public ServicoOferecido registrar(ServicoOferecido servico) {
        Validacoes.validarObjetoObrigatorio(servico, "O serviço");
        
        if (repositorio.buscarPorNome(servico.getNome()) != null) {
            throw new IllegalArgumentException("Já existe um serviço com o nome: " + servico.getNome());
        }

        if (servico.getDuracaoMinutos() > 480) {
            throw new IllegalArgumentException("Duração não pode exceder 480 minutos (8 horas).");
        }
        
        ServicoOferecido salvo = repositorio.salvar(servico);
        
        if (publicadorEventos != null) {
            publicadorEventos.publishEvent(new ServicoOferecidoEvent(this, salvo, TipoAcao.CRIADO));
        }
        
        return salvo;
    }

    @Transactional
    public void associarProfissional(String nomeServico, String nomeProfissional) {
        Validacoes.validarStringObrigatoria(nomeServico, "Nome do serviço");
        Validacoes.validarStringObrigatoria(nomeProfissional, "Nome do profissional");

        boolean estaQualificado = repositorio.estaQualificado(nomeServico, nomeProfissional);

        if (!estaQualificado) {
             throw new IllegalArgumentException("O profissional " + nomeProfissional + " não está qualificado para o serviço " + nomeServico);
        }
    }

    public List<ServicoOferecido> listarTodos() {
        return repositorio.listarTodos();
    }
    
    @Transactional
    public ServicoOferecido atualizar(Integer id, ServicoOferecido servico) {
        Validacoes.validarObjetoObrigatorio(id, "ID do serviço");
        Validacoes.validarObjetoObrigatorio(servico, "O serviço");
        Validacoes.validarStringObrigatoria(servico.getNome(), "Nome do serviço");
        Validacoes.validarTamanhoMinimoString(servico.getNome(), 3, "Nome do serviço");
        Validacoes.validarTamanhoMaximoString(servico.getNome(), 100, "Nome do serviço");
        Validacoes.validarValorPositivo(servico.getPreco(), "Preço do serviço");
        Validacoes.validarStringObrigatoria(servico.getDescricao(), "Descrição do serviço");
        Validacoes.validarTamanhoMaximoString(servico.getDescricao(), 255, "Descrição do serviço");
        Validacoes.validarInteiroPositivo(servico.getDuracaoMinutos(), "Duração do serviço");
        
        if (servico.getDuracaoMinutos() > 480) {
            throw new IllegalArgumentException("Duração não pode exceder 480 minutos.");
        }
        
        ServicoOferecido existente = buscarPorId(id);
        
        existente.setNome(servico.getNome());
        existente.setPreco(servico.getPreco());
        existente.setDescricao(servico.getDescricao());
        existente.setDuracaoMinutos(servico.getDuracaoMinutos());
        existente.setAtivo(servico.isAtivo()); 
        
        ServicoOferecido salvo = repositorio.salvar(existente);

        if (publicadorEventos != null) {
            publicadorEventos.publishEvent(new ServicoOferecidoEvent(this, salvo, TipoAcao.ATUALIZADO));
        }
        
        return salvo;
    }

    @Transactional
    public ServicoOferecido atualizarPreco(Integer id, BigDecimal novoPreco) {
        Validacoes.validarObjetoObrigatorio(id, "ID do serviço");
        Validacoes.validarValorPositivo(novoPreco, "Novo preço");
        
        ServicoOferecido servico = buscarPorId(id);
        servico.atualizarPreco(novoPreco);
        
        ServicoOferecido salvo = repositorio.salvar(servico);
        
        if (publicadorEventos != null) {
            publicadorEventos.publishEvent(new ServicoOferecidoEvent(this, salvo, TipoAcao.ATUALIZADO));
        }
        
        return salvo;
    }

    @Transactional
    public ServicoOferecido atualizarDuracao(Integer id, Integer novaDuracao) {
        Validacoes.validarObjetoObrigatorio(id, "ID do serviço");
        
        if (novaDuracao == null || novaDuracao <= 0) {
            throw new IllegalArgumentException("A duração deve ser um número positivo.");
        }

        if (novaDuracao > 480) {
            throw new IllegalArgumentException("Duração não pode exceder 480 minutos.");
        }
        
        ServicoOferecido servico = buscarPorId(id);
        servico.atualizarDuracao(novaDuracao);
        ServicoOferecido salvo = repositorio.salvar(servico);
        
        if (publicadorEventos != null) {
            publicadorEventos.publishEvent(new ServicoOferecidoEvent(this, salvo, TipoAcao.ATUALIZADO));
        }
        return salvo;
    }

    @Transactional
    public void remover(Integer id) {
        Validacoes.validarObjetoObrigatorio(id, "ID do serviço");
        ServicoOferecido s = buscarPorId(id);
        repositorio.remover(id);
        
        if (publicadorEventos != null) {
            publicadorEventos.publishEvent(new ServicoOferecidoEvent(this, s, TipoAcao.REMOVIDO));
        }
    }

    @Transactional
    public ServicoOferecido desativar(Integer id, String motivo) {
        Validacoes.validarObjetoObrigatorio(id, "ID do serviço");
        Validacoes.validarStringObrigatoria(motivo, "Motivo da inatividade");
        
        ServicoOferecido servico = buscarPorId(id);
        servico.desativar(motivo);

        ServicoOferecido salvo = repositorio.salvar(servico);
        
        if (publicadorEventos != null) {
            publicadorEventos.publishEvent(new ServicoOferecidoEvent(this, salvo, TipoAcao.DESATIVADO));
        }
        
        return salvo;
    }

    public boolean isAtivo(Integer id) {
        Validacoes.validarObjetoObrigatorio(id, "ID do serviço");
        return repositorio.isAtivo(id);
    }
}