package com.cesarschool.barbearia.dominio.principal.profissional;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cesarschool.barbearia.dominio.compartilhado.utils.Validacoes;
import com.cesarschool.barbearia.dominio.compartilhado.valueobjects.Cpf;
import com.cesarschool.barbearia.dominio.principal.profissional.eventos.ProfissionalEvent;
import com.cesarschool.barbearia.dominio.principal.profissional.eventos.ProfissionalEvent.TipoAcao;
import com.cesarschool.barbearia.dominio.principal.servico.ServicoOferecidoId;

import lombok.RequiredArgsConstructor;

/**
 * Domain Service para Profissional.
 * Integrado com Spring Boot e Padrão Observer.
 */
@Service
@RequiredArgsConstructor
public class ProfissionalServico {
    
    private final ProfissionalRepositorio repositorio;
    private final ApplicationEventPublisher publicadorEventos; // Spring Observer

    // --- MÉTODOS DE LEITURA (Delegates para o Repositório) ---

    public List<Profissional> buscarQualificadosParaServico(ServicoOferecidoId servicoId) {
        return repositorio.buscarQualificadosParaServico(servicoId);
    }

    public List<Profissional> buscarDisponiveisNaDataHora(LocalDateTime dataHora, Integer duracaoMinutos) {
        return repositorio.buscarDisponiveisNaDataHora(dataHora, duracaoMinutos);
    }

    public Profissional buscarPorId(ProfissionalId id) {
        Validacoes.validarObjetoObrigatorio(id, "O ID");
        Profissional p = repositorio.buscarPorId(id.getValor());
        if (p == null) {
            throw new IllegalArgumentException("Profissional não encontrado com ID: " + id.getValor());
        }
        return p;
    }
    
    // Sobrecarga para facilitar uso pelo Controller (recebe Integer)
    public Profissional buscarPorId(Integer id) {
        if (id == null) return null;
        return buscarPorId(new ProfissionalId(id));
    }

    public Profissional buscarPorCpf(Cpf cpf) {
        Validacoes.validarObjetoObrigatorio(cpf, "O CPF");
        return repositorio.buscarPorCpf(cpf);
    }

    public List<Profissional> listarTodos() {
        return repositorio.listarTodos();
    }

    public Profissional buscarPrimeiroProfissionalDisponivel(LocalDateTime dataHora, int duracaoServicoMinutos) {
        return repositorio.buscarPrimeiroProfissionalDisponivel(dataHora, duracaoServicoMinutos);
    }

    public boolean estaQualificado(ProfissionalId profissionalId, ServicoOferecidoId servicoId) {
        Validacoes.validarObjetoObrigatorio(profissionalId, "ID do profissional");
        Validacoes.validarObjetoObrigatorio(servicoId, "ID do serviço");
        return repositorio.estaQualificado(profissionalId.getValor(), servicoId.getValor());
    }

    // --- MÉTODOS DE ESCRITA (Com Regras e Eventos) ---

    @Transactional
    public Profissional registrarNovo(Profissional profissional) {
        Validacoes.validarObjetoObrigatorio(profissional, "O profissional");
        
        if (repositorio.existePorCpf(profissional.getCpf())) {
            throw new IllegalArgumentException(
                "Já existe um profissional cadastrado com o CPF: " + profissional.getCpf().toString()
            );
        }
        
        Profissional salvo = repositorio.salvar(profissional);

        // OBSERVER: Evento Unificado CRIADO
        if (publicadorEventos != null) {
            publicadorEventos.publishEvent(new ProfissionalEvent(this, salvo, TipoAcao.CRIADO));
        }
        
        return salvo;
    }

    @Transactional
    public Profissional registrarNovo(Profissional profissional, Senioridade senioridade) {
        Validacoes.validarObjetoObrigatorio(profissional, "O profissional");
        Validacoes.validarObjetoObrigatorio(senioridade, "Senioridade");

        // Aproveita a validação do método principal
        profissional.setSenioridade(senioridade);
        return registrarNovo(profissional);
    }

    @Transactional
    public Profissional atualizar(Profissional profissional) {
        Validacoes.validarObjetoObrigatorio(profissional, "O profissional");
        Validacoes.validarObjetoObrigatorio(profissional.getId(), "O ID do profissional");
        
        // Garante que existe antes de atualizar
        buscarPorId(profissional.getId());
        
        Profissional salvo = repositorio.salvar(profissional);

        // OBSERVER: Evento Unificado ATUALIZADO
        if (publicadorEventos != null) {
            publicadorEventos.publishEvent(new ProfissionalEvent(this, salvo, TipoAcao.ATUALIZADO));
        }

        return salvo;
    }
    
    // Sobrecarga para facilitar o Controller (PUT /profissionais/{id})
    @Transactional
    public Profissional atualizar(Integer id, Profissional dadosAtualizados) {
        Profissional existente = buscarPorId(new ProfissionalId(id));
        
        // Atualiza os dados permitidos
        existente.setNome(dadosAtualizados.getNome());
        existente.setTelefone(dadosAtualizados.getTelefone());
        existente.setEmail(dadosAtualizados.getEmail());
        existente.setAgenda(dadosAtualizados.getAgenda());
        
        if (dadosAtualizados.getServicoOferecidoIds() != null && !dadosAtualizados.getServicoOferecidoIds().isEmpty()) {
            existente.setServicoOferecidoIds(dadosAtualizados.getServicoOferecidoIds());
        }

        return atualizar(existente);
    }

    @Transactional
    public void remover(ProfissionalId id) {
        Profissional p = buscarPorId(id); // Valida existência
        repositorio.remover(id.getValor());
        
        // OBSERVER: Evento Unificado DESLIGADO (para limpar auditoria/agendas)
        if (publicadorEventos != null) {
            publicadorEventos.publishEvent(new ProfissionalEvent(this, p, TipoAcao.DESLIGADO));
        }
    }
    
    // Sobrecarga para Controller (DELETE /profissionais/{id})
    public void desligarProfissional(Integer id) {
        desativar(new ProfissionalId(id), "Desligamento solicitado via API");
    }

    @Transactional
    public Profissional desativar(ProfissionalId id, String motivo) {
        Validacoes.validarObjetoObrigatorio(id, "O ID do profissional");
        Validacoes.validarStringObrigatoria(motivo, "O motivo da inatividade");
        
        Profissional profissional = buscarPorId(id);
        profissional.desativar(motivo);
        
        Profissional salvo = repositorio.salvar(profissional);
        
        // OBSERVER: Evento Unificado DESLIGADO
        if (publicadorEventos != null) {
            publicadorEventos.publishEvent(new ProfissionalEvent(this, salvo, TipoAcao.DESLIGADO));
        }
        
        return salvo;
    }

    @Transactional
    public void removerServico(String nomeProfissional, String nomeServico) {
        if (!repositorio.possuiAssociacaoServico(nomeProfissional, nomeServico)) {
            return; 
        }

        boolean temAgendamento = this.repositorio.temAgendamentoAtivo(nomeServico);

        if (temAgendamento) {
            throw new IllegalStateException("Não é possível remover serviço com agendamentos ativos.");
        }
        
        this.repositorio.removerAssociacaoServico(nomeProfissional, nomeServico);
        
        // Não dispara evento aqui pois é uma alteração interna de associação
    }

    @Transactional
    public void configurarJornada(ProfissionalId profissionalId, Agenda novaJornada, String tipoUsuarioLogado) {
        if (tipoUsuarioLogado == null || !tipoUsuarioLogado.equals("ADMIN")) {
            throw new IllegalArgumentException("Acesso negado: apenas administradores podem configurar a jornada.");
        }
        Profissional profissional = buscarPorId(profissionalId);
        profissional.setAgenda(novaJornada);  
        
        Profissional salvo = repositorio.salvar(profissional);
        
        // OBSERVER: Evento Unificado ATUALIZADO
        if (publicadorEventos != null) {
            publicadorEventos.publishEvent(new ProfissionalEvent(this, salvo, TipoAcao.ATUALIZADO));
        }
    }
}