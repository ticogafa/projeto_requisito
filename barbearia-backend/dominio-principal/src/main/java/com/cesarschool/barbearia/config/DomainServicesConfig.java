package com.cesarschool.barbearia.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cesarschool.barbearia.aplicacao.agendamento.AgendamentoRepositorioAplicacao;
import com.cesarschool.barbearia.aplicacao.agendamento.AgendamentoServicoAplicacao;
import com.cesarschool.barbearia.aplicacao.estoque.ProdutoRepositorioAplicacao;
import com.cesarschool.barbearia.aplicacao.estoque.ProdutoServicoAplicacao;
import com.cesarschool.barbearia.dominio.compartilhado.eventos.PublicadorEventos;
import com.cesarschool.barbearia.dominio.principal.agendamento.AgendamentoRepositorio;
import com.cesarschool.barbearia.dominio.principal.agendamento.AgendamentoServico;
import com.cesarschool.barbearia.dominio.principal.produto.ProdutoRepositorio;
import com.cesarschool.barbearia.dominio.principal.produto.ProdutoServico;
import com.cesarschool.barbearia.dominio.principal.produto.estoque.GestaoEstoqueServico;
import com.cesarschool.barbearia.dominio.principal.produto.estoque.MovimentacaoEstoqueRepositorio;
import com.cesarschool.barbearia.dominio.principal.profissional.ProfissionalRepositorio;
import com.cesarschool.barbearia.dominio.principal.profissional.ProfissionalServico;
import com.cesarschool.barbearia.dominio.principal.servico.ServicoOferecidoRepositorio;
import com.cesarschool.barbearia.dominio.principal.servico.ServicoOferecidoServico;
import com.cesarschool.barbearia.dominio.principal.cliente.caixa.GestaoCaixaServico;
import com.cesarschool.barbearia.dominio.principal.cliente.caixa.IGestaoCaixa;
import com.cesarschool.barbearia.dominio.principal.cliente.caixa.LancamentoRepositorio;
import com.cesarschool.barbearia.dominio.principal.cliente.caixa.ValidadorSaldoDecorator;
import com.cesarschool.barbearia.dominio.principal.cliente.ClienteRepositorio;
import com.cesarschool.barbearia.dominio.principal.cliente.ClienteServico;

/**
 * Configuração de Beans para Domain Services.
 * Seguindo o padrão do sgb-2025-01, os Domain Services são configurados
 * como @Bean na camada de infraestrutura/apresentação, NÃO com @Service
 * no domínio. Isso mantém o domínio livre de dependências de framework.
 * Esta classe é responsável por:
 * - Instanciar Domain Services com suas dependências
 * - Conectar interfaces de repositório com implementações JPA
 * - Manter a pureza do domínio (sem anotações Spring)
 * @see dev.sauloaraujo.sgb.BackendAplicacao (sgb-2025-01)
 */
@Configuration
public class DomainServicesConfig {

    /**
     * Configura o serviço de domínio de Agendamento.
     * @param agendamentoRepositorio Interface do repositório (implementada na infraestrutura)
     * @param profissionalServico Serviço de profissionais (necessário para validações)
     * @param servicoRepositorio Repositório de serviços (para validar serviços ativos)
     * @return Instância configurada de AgendamentoServico
     */
    @Bean
    public AgendamentoServico agendamentoServico(
            AgendamentoRepositorio agendamentoRepositorio,
            ProfissionalServico profissionalServico,
            ServicoOferecidoRepositorio servicoRepositorio) {
        return new AgendamentoServico(agendamentoRepositorio, profissionalServico, servicoRepositorio);
    }

    /**
     * Configura o serviço de domínio de Produto.
     * * @param produtoRepositorio Interface do repositório (implementada na infraestrutura)
     * @return Instância configurada de ProdutoServico
     */
    @Bean
    public ProdutoServico produtoServico(ProdutoRepositorio produtoRepositorio) {
        return new ProdutoServico(produtoRepositorio);
    }

    /**
     * Configura o serviço de domínio de Cliente.
     * @param clienteRepositorio Interface do repositório
     * @return Instância configurada de ClienteServico
     */
    @Bean
    public ClienteServico clienteServico(ClienteRepositorio clienteRepositorio) {
        return new ClienteServico(clienteRepositorio);
    }

    /**
     * Configura o serviço de domínio de Gestão de Estoque.
     * Este é um serviço mais complexo que orquestra operações de estoque
     * e mantém histórico de movimentações.
     * * @param produtoRepositorio Repositório de produtos
     * @param movimentacaoRepositorio Repositório de movimentações (para rastreabilidade)
     * @return Instância configurada de GestaoEstoqueServico
     */
    @Bean
    public GestaoEstoqueServico gestaoEstoqueServico(
            ProdutoRepositorio produtoRepositorio,
            MovimentacaoEstoqueRepositorio movimentacaoRepositorio) {
        return new GestaoEstoqueServico(produtoRepositorio, movimentacaoRepositorio);
    }

    /**
     * Configura o serviço da camada de aplicação para Agendamento.
     * Segue padrão SGB-2025-01: orquestra domain services e repositórios de aplicação.
     * * @param repositorioAplicacao Repositório para consultas com DTOs/projeções
     * @param agendamentoServico Serviço de domínio para lógica de negócio
     * @param servicoServico Serviço de domínio de serviços oferecidos
     * @param clienteRepositorio Repositório de clientes para busca
     * @param clienteServico Serviço de domínio de clientes para criação
     * @return Instância configurada de AgendamentoServicoAplicacao
     */
    @Bean
    public AgendamentoServicoAplicacao agendamentoServicoAplicacao(
            AgendamentoRepositorioAplicacao repositorioAplicacao,
            AgendamentoServico agendamentoServico,
            ServicoOferecidoServico servicoServico,
            ClienteRepositorio clienteRepositorio,
            ClienteServico clienteServico) {
        return new AgendamentoServicoAplicacao(repositorioAplicacao, agendamentoServico, servicoServico, clienteRepositorio, clienteServico);
    }

    /**
     * Configura o serviço da camada de aplicação para Produto/Estoque.
     * Segue padrão SGB-2025-01: orquestra domain services e repositórios de aplicação.
     * * @param repositorioAplicacao Repositório para consultas com DTOs/projeções
     * @param gestaoEstoque Serviço de domínio para lógica de negócio de estoque
     * @return Instância configurada de ProdutoServicoAplicacao
     */
    @Bean
    public ProdutoServicoAplicacao produtoServicoAplicacao(
            ProdutoRepositorioAplicacao repositorioAplicacao,
            GestaoEstoqueServico gestaoEstoque) {
        return new ProdutoServicoAplicacao(repositorioAplicacao, gestaoEstoque);
    }

    // TODO: Implementar VendaJpa antes de habilitar este bean
    // /**
    //  * Configura o serviço de domínio de Venda.
    //  * 
    //  * @param vendaRepositorio Repositório de vendas
    //  * @param produtoServico Serviço de produtos (para validar estoque)
    //  * @return Instância configurada de VendaServico
    //  */
    // @Bean
    // public VendaServico vendaServico(
    //         VendaRepositorio vendaRepositorio,
    //         ProdutoServico produtoServico) {
    //     return new VendaServico(vendaRepositorio, produtoServico);
    // }


    /**
     * Configura o serviço de domínio de Gestão de Caixa com Decorators.
     * 
     * Este bean demonstra o padrão Decorator. A implementação base
     * (GestaoCaixaServico) é envolvida por múltiplos decoradores, cada um
     * adicionando uma nova responsabilidade (validação de saldo, logging).
     * 
     * O Spring injetará a implementação JPA de LancamentoRepositorio.
     * 
     * @param lancamentoRepositorio Interface do repositório de lançamentos.
     * @return Uma instância de IGestaoCaixa decorada e pronta para uso.
     */
    @Bean
    public IGestaoCaixa gestaoCaixaServico(LancamentoRepositorio lancamentoRepositorio) {
        // 1. Começa com a implementação base
        IGestaoCaixa servicoBase = new GestaoCaixaServico(lancamentoRepositorio);

        // 2. Envolve com o decorador de validação de saldo
        IGestaoCaixa comValidador = new ValidadorSaldoDecorator(servicoBase);

        // 3. Envolve com o decorador de log e retorna
        // IGestaoCaixa comLog = new LoggerDecorator(comValidador);

        return comValidador;
    }
}
