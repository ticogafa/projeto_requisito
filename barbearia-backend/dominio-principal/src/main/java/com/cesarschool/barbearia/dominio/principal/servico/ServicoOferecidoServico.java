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

    public ServicoOferecido buscarPorId(Integer id) {
        Validacoes.validarObjetoObrigatorio(id, "ID do serviço");
        ServicoOferecido servico = repositorio.buscarPorId(id);
        
        if (servico == null) {
            throw new IllegalArgumentException("Serviço não encontrado com ID: " + id);
        }
        return servico;
    }

    public ServicoOferecido registrar(ServicoOferecido servico) {
        Validacoes.validarObjetoObrigatorio(servico, "O serviço");
        
        if (repositorio.buscarPorNome(servico.getNome()) != null) {
            throw new IllegalArgumentException("Já existe um serviço com o nome: " + servico.getNome());
        }

        if (servico.getDuracaoMinutos() > 480) {
            throw new IllegalArgumentException(
                "Duração não pode exceder 480 minutos (8 horas). Valor fornecido: " + 
                servico.getDuracaoMinutos() + " minutos"
            );
        }
        
        return repositorio.salvar(servico);
    }

    public void associarProfissional(String nomeServico, String nomeProfissional) {
        Validacoes.validarStringObrigatoria(nomeServico, "Nome do serviço");
        Validacoes.validarStringObrigatoria(nomeProfissional, "Nome do profissional");

        if (!repositorio.estaQualificado(nomeServico, nomeProfissional)) {
            throw new IllegalArgumentException(
                String.format("Profissional " + nomeProfissional + "não está qualificado para o serviço "+ nomeServico + ".")
            );
        }
        repositorio.salvarAssociacao(nomeServico, nomeProfissional);
    }

    public ServicoOferecido definirAddOn(ServicoOferecidoId addOnId, ServicoOferecidoId principalId) {
        Validacoes.validarObjetoObrigatorio(addOnId, "ID do Add-On");
        Validacoes.validarObjetoObrigatorio(principalId, "ID do Serviço Principal");

        ServicoOferecido addOn = buscarPorId(addOnId.getValor());
        buscarPorId(principalId.getValor());

        addOn.definirComoAddonDe(principalId);

        return repositorio.salvar(addOn);
    }
    
    public ServicoOferecido definirIntervaloLimpeza(Integer id, int intervaloMinutos) {
        Validacoes.validarObjetoObrigatorio(id, "ID do serviço");
        
        ServicoOferecido servico = buscarPorId(id);
        servico.definirIntervaloLimpeza(intervaloMinutos);

        return repositorio.salvar(servico);
    }

    public boolean podeSerAgendadoSozinho(ServicoOferecido servico) {
        Validacoes.validarObjetoObrigatorio(servico, "O serviço");
        return servico.getServicoPrincipalId() == null;
    }

    public List<ServicoOferecido> buscarPorProfissional(ProfissionalId profissionalId) {
        Validacoes.validarObjetoObrigatorio(profissionalId, "ID do profissional");
        return repositorio.buscarPorProfissional(profissionalId);
    }

    public List<ServicoOferecido> listarTodos() {
        return repositorio.listarTodos();
    }

    public ServicoOferecido desativarServico(Integer id, String motivo) {
        Validacoes.validarObjetoObrigatorio(id, "ID do serviço");
        Validacoes.validarStringObrigatoria(motivo, "Motivo da inatividade");

        ServicoOferecido servico = buscarPorId(id);
        servico.desativar(motivo);
        
        return repositorio.salvar(servico);
    }

    public List<ServicoOferecido> listarServicosAtivos() {
        return repositorio.listarTodos().stream()
                .filter(ServicoOferecido::isAtivo)
                .toList();
    }
    
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
        
        buscarPorId(id);
        
        return repositorio.salvar(servico);
    }

    public ServicoOferecido atualizarPreco(Integer id, BigDecimal novoPreco) {
        Validacoes.validarObjetoObrigatorio(id, "ID do serviço");
        Validacoes.validarValorPositivo(novoPreco, "Novo preço");
        
        ServicoOferecido servico = buscarPorId(id);
        servico.atualizarPreco(novoPreco);
        
        return repositorio.salvar(servico);
    }

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
        buscarPorId(id); 
        repositorio.remover(id);
    }
}