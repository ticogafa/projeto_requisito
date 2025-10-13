package com.cesarschool.barbearia.dominio.principal.servico;

import java.math.BigDecimal;
import java.util.List;

import com.cesarschool.barbearia.dominio.compartilhado.utils.Validacoes;
import com.cesarschool.barbearia.dominio.principal.profissional.ProfissionalId;

/**
 * Serviço de domínio para ServicoOferecido.
 * Gerencia os serviços oferecidos pelos profissionais.
 */
public class ServicoOferecidoServico {
    
    private final ServicoOferecidoRepositorio repositorio;

    public ServicoOferecidoServico(ServicoOferecidoRepositorio repositorio) {
        Validacoes.validarObjetoObrigatorio(repositorio, "O repositório");
        this.repositorio = repositorio;
    }

    /**
     * Registra um novo serviço oferecido.
     * Regras de negócio:
     * - ProfissionalId é obrigatório
     * - Nome deve ter entre 3 e 100 caracteres
     * - Preço deve ser positivo
     * - Descrição é obrigatória e não pode exceder 255 caracteres
     * - Duração deve ser entre 1 e 480 minutos (8 horas)
     */
    public ServicoOferecido registrar(ServicoOferecido servico) {
        // Validações já são realizadas nos setters do ServicoOferecido.
        Validacoes.validarObjetoObrigatorio(servico, "O serviço");
        
        if (servico.getDuracaoMinutos() > 480) {
            throw new IllegalArgumentException(
                "Duração não pode exceder 480 minutos (8 horas). Valor fornecido: " + 
                servico.getDuracaoMinutos() + " minutos"
            );
        }
        
        return repositorio.salvar(servico);
    }

    public ServicoOferecido buscarPorId(Integer id) {
        Validacoes.validarObjetoObrigatorio(id, "ID do serviço");
        return repositorio.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException(
                    "Serviço não encontrado com ID: " + id
        ));
    }

    /**
     * Busca todos os serviços oferecidos por um profissional específico.
     */
    public List<ServicoOferecido> buscarPorProfissional(ProfissionalId profissionalId) {
        Validacoes.validarObjetoObrigatorio(profissionalId, "ID do profissional");
        return repositorio.buscarPorProfissional(profissionalId);
    }

    public List<ServicoOferecido> listarTodos() {
        return repositorio.listarTodos();
    }

    /**
     * Atualiza um serviço existente.
     */
    public ServicoOferecido atualizar(Integer id, ServicoOferecido servico) {
        Validacoes.validarObjetoObrigatorio(id, "ID do serviço");
        Validacoes.validarObjetoObrigatorio(servico, "O serviço");
        Validacoes.validarObjetoObrigatorio(servico.getProfissionalId(), "ID do profissional");
        Validacoes.validarStringObrigatoria(servico.getNome(), "Nome do serviço");
        Validacoes.validarTamanhoMinimoString(servico.getNome(), 3, "Nome do serviço");
        Validacoes.validarTamanhoMaximoString(servico.getNome(), 100, "Nome do serviço");
        Validacoes.validarValorPositivo(servico.getPreco(), "Preço do serviço");
        Validacoes.validarStringObrigatoria(servico.getDescricao(), "Descrição do serviço");
        Validacoes.validarTamanhoMaximoString(servico.getDescricao(), 255, "Descrição do serviço");
        Validacoes.validarInteiroPositivo(servico.getDuracaoMinutos(), "Duração do serviço");
        
        if (servico.getDuracaoMinutos() > 480) {
            throw new IllegalArgumentException(
                "Duração não pode exceder 480 minutos (8 horas). Valor fornecido: " + 
                servico.getDuracaoMinutos() + " minutos"
            );
        }
        
        // Verifica se o serviço existe
        buscarPorId(id);
        
        return repositorio.salvar(servico);
    }

    /**
     * Atualiza apenas o preço de um serviço.
     * Método de conveniência para ajustes de preço.
     */
    public ServicoOferecido atualizarPreco(Integer id, BigDecimal novoPreco) {
        Validacoes.validarObjetoObrigatorio(id, "ID do serviço");
        Validacoes.validarValorPositivo(novoPreco, "Novo preço");
        
        ServicoOferecido servico = buscarPorId(id);
        servico.atualizarPreco(novoPreco);
        
        return repositorio.salvar(servico);
    }

    /**
     * Atualiza apenas a duração de um serviço.
     * Método de conveniência para ajustes de duração.
     */
    public ServicoOferecido atualizarDuracao(Integer id, Integer novaDuracao) {
        Validacoes.validarObjetoObrigatorio(id, "ID do serviço");
        Validacoes.validarInteiroPositivo(novaDuracao, "Nova duração");
        
        if (novaDuracao > 480) {
            throw new IllegalArgumentException(
                "Duração não pode exceder 480 minutos (8 horas). Valor fornecido: " + 
                novaDuracao + " minutos"
            );
        }
        
        ServicoOferecido servico = buscarPorId(id);
        servico.atualizarDuracao(novaDuracao);
        
        return repositorio.salvar(servico);
    }

    public void remover(Integer id) {
        Validacoes.validarObjetoObrigatorio(id, "ID do serviço");
        buscarPorId(id); // Verifica se existe
        repositorio.remover(id);
    }
}
