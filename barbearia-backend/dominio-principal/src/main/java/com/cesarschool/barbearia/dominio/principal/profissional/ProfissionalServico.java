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

@Service
@RequiredArgsConstructor
public class ProfissionalServico {
    
    private final ProfissionalRepositorio repositorio;
    private final ApplicationEventPublisher publicadorEventos;

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
        return repositorio.estaQualificado(profissionalId, servicoId);
    }

    @Transactional
    public Profissional registrarNovo(Profissional profissional) {
        Validacoes.validarObjetoObrigatorio(profissional, "O profissional");
        
        if (repositorio.existePorCpf(profissional.getCpf())) {
            throw new IllegalArgumentException(
                "Já existe um profissional cadastrado com o CPF: " + profissional.getCpf().toString()
            );
        }
        
        Profissional salvo = repositorio.salvar(profissional);

        if (publicadorEventos != null) {
            publicadorEventos.publishEvent(new ProfissionalEvent(this, salvo, TipoAcao.CRIADO));
        }
        
        return salvo;
    }

    @Transactional
    public Profissional registrarNovo(Profissional profissional, Senioridade senioridade) {
        Validacoes.validarObjetoObrigatorio(profissional, "O profissional");
        Validacoes.validarObjetoObrigatorio(senioridade, "Senioridade");

        profissional.setSenioridade(senioridade);
        return registrarNovo(profissional);
    }

    @Transactional
    public Profissional atualizar(Profissional profissional) {
        Validacoes.validarObjetoObrigatorio(profissional, "O profissional");
        Validacoes.validarObjetoObrigatorio(profissional.getId(), "O ID do profissional");
        
        buscarPorId(profissional.getId());
        
        Profissional salvo = repositorio.salvar(profissional);

        if (publicadorEventos != null) {
            publicadorEventos.publishEvent(new ProfissionalEvent(this, salvo, TipoAcao.ATUALIZADO));
        }

        return salvo;
    }
    
    @Transactional
    public Profissional atualizar(Integer id, Profissional dadosAtualizados) {
        ProfissionalId idVo = new ProfissionalId(id);
        Profissional existente = buscarPorId(idVo);
        
        existente.setNome(dadosAtualizados.getNome());
        existente.setTelefone(dadosAtualizados.getTelefone());
        existente.setEmail(dadosAtualizados.getEmail());
        existente.setAgenda(dadosAtualizados.getAgenda());
        
        existente.setAtivo(dadosAtualizados.isAtivo()); 

        if (dadosAtualizados.getServicoOferecidoIds() != null) {
            existente.setServicoOferecidoIds(dadosAtualizados.getServicoOferecidoIds());
        }

        Profissional salvo = repositorio.salvar(existente);

        if (publicadorEventos != null) {
            publicadorEventos.publishEvent(new ProfissionalEvent(this, salvo, TipoAcao.ATUALIZADO));
        }
        return salvo;
    }

    @Transactional
    public void remover(ProfissionalId id) {
        Profissional p = buscarPorId(id);
        repositorio.remover(id.getValor());
        
        if (publicadorEventos != null) {
            publicadorEventos.publishEvent(new ProfissionalEvent(this, p, TipoAcao.DESLIGADO));
        }
    }
    
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
        
    }

    @Transactional
    public void configurarJornada(ProfissionalId profissionalId, Agenda novaJornada, String tipoUsuarioLogado) {
        if (tipoUsuarioLogado == null || !tipoUsuarioLogado.equals("ADMIN")) {
            throw new IllegalArgumentException("Acesso negado: apenas administradores podem configurar a jornada.");
        }
        Profissional profissional = buscarPorId(profissionalId);
        profissional.setAgenda(novaJornada);  
        
        Profissional salvo = repositorio.salvar(profissional);
        
        if (publicadorEventos != null) {
            publicadorEventos.publishEvent(new ProfissionalEvent(this, salvo, TipoAcao.ATUALIZADO));
        }
    }
}