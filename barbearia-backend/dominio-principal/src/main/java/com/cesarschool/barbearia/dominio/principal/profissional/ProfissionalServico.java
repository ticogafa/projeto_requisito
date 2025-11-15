package com.cesarschool.barbearia.dominio.principal.profissional;

import java.time.LocalDateTime;
import java.util.List;

import com.cesarschool.barbearia.dominio.compartilhado.utils.Validacoes;
import com.cesarschool.barbearia.dominio.compartilhado.valueobjects.Cpf;
import com.cesarschool.barbearia.dominio.principal.servico.ServicoOferecidoId;

import lombok.RequiredArgsConstructor;

/**
 * Domain Service para Profissional.
 * Contém lógica de negócio que não pertence naturalmente a uma entidade.
 * 
 * IMPORTANTE: Sem anotações de infraestrutura (@Service, @Transactional, @Autowired).
 * A configuração de beans deve ser feita na camada de infraestrutura ou aplicação.
 */
@RequiredArgsConstructor
public class ProfissionalServico {
    
    private final ProfissionalRepositorio repositorio;

    public List<Profissional> buscarQualificadosParaServico(ServicoOferecidoId servicoId) {
        return repositorio.buscarQualificadosParaServico(servicoId);
    }

    public List<Profissional> buscarDisponiveisNaDataHora(LocalDateTime dataHora, Integer duracaoMinutos) {
        return repositorio.buscarDisponiveisNaDataHora(dataHora, duracaoMinutos);
    }

    public Profissional registrarNovo(Profissional profissional) {
        Validacoes.validarObjetoObrigatorio(profissional, "O profissional");
        
        if (repositorio.existePorCpf(profissional.getCpf())) {
            throw new IllegalArgumentException(
                "Já existe um profissional cadastrado com o CPF: " + profissional.getCpf().toString()
            );
        }
        
        return repositorio.salvar(profissional);
    }

    public Profissional registrarNovo(Profissional profissional, Senioridade senioridade) {
        Validacoes.validarObjetoObrigatorio(profissional, "O profissional");
        Validacoes.validarObjetoObrigatorio(senioridade, "Senioridade");

        if (repositorio.existePorCpf(profissional.getCpf())) {
            throw new IllegalArgumentException(
                "Já existe um profissional cadastrado com o CPF: " + profissional.getCpf().toString()
            );
        }
        
        profissional.setSenioridade(senioridade);
        
        return repositorio.salvar(profissional);
    }

    public Profissional buscarPorId(ProfissionalId id) {
        Validacoes.validarObjetoObrigatorio(id, "O ID");
        Profissional p = repositorio.buscarPorId(id.getValor());
        if (p == null) {
            throw new IllegalArgumentException("Profissional não encontrado com ID: " + id.getValor());
        }
        return p;
    }

    public Profissional buscarPorCpf(Cpf cpf) {
        Validacoes.validarObjetoObrigatorio(cpf, "O CPF");
        return repositorio.buscarPorCpf(cpf);
    }

    public List<Profissional> listarTodos() {
        return repositorio.listarTodos();
    }

    public Profissional atualizar(Profissional profissional) {
        Validacoes.validarObjetoObrigatorio(profissional, "O profissional");
        Validacoes.validarObjetoObrigatorio(profissional.getId(), "O ID do profissional");
        
        buscarPorId(profissional.getId());
        
        return repositorio.salvar(profissional);
    }

    public void remover(ProfissionalId id) {
        buscarPorId(id);
        repositorio.remover(id.getValor());
    }

    public Profissional buscarPrimeiroProfissionalDisponivel(LocalDateTime dataHora, int duracaoServicoMinutos) {
        return repositorio.buscarPrimeiroProfissionalDisponivel(dataHora, duracaoServicoMinutos);
    }

    public Profissional desativar(ProfissionalId id, String motivo) {
        Validacoes.validarObjetoObrigatorio(id, "O ID do profissional");
        Validacoes.validarStringObrigatoria(motivo, "O motivo da inatividade");
        
        Profissional profissional = buscarPorId(id);
        profissional.desativar(motivo);
        
        return repositorio.salvar(profissional);
    }

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

    /**
     * Configura a jornada de trabalho de um profissional.
     * Implementação completa.
     * * @param profissionalId O ID do profissional a ser alterado.
     * @param novaJornada O novo objeto de Agenda.
     * @param tipoUsuarioLogado O tipo de usuário tentando a ação.
     */
    public void configurarJornada(ProfissionalId profissionalId, Agenda novaJornada, String tipoUsuarioLogado) {
        if (tipoUsuarioLogado == null || !tipoUsuarioLogado.equals("ADMIN")) {
            throw new IllegalArgumentException("Acesso negado: apenas administradores podem configurar a jornada.");
        }
        Profissional profissional = buscarPorId(profissionalId);
        profissional.setAgenda(novaJornada);  
        repositorio.salvar(profissional);
    }

    /**
     * Verifica se um profissional está qualificado para executar um serviço.
     * @param profissionalId ID do profissional
     * @param servicoId ID do serviço
     * @return true se o profissional está qualificado
     */
    public boolean estaQualificado(ProfissionalId profissionalId, ServicoOferecidoId servicoId) {
        Validacoes.validarObjetoObrigatorio(profissionalId, "ID do profissional");
        Validacoes.validarObjetoObrigatorio(servicoId, "ID do serviço");
        return repositorio.estaQualificado(profissionalId.getValor(), servicoId.getValor());
    }
}