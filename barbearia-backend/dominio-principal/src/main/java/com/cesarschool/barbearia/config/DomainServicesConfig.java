package com.cesarschool.barbearia.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cesarschool.barbearia.aplicacao.agendamento.AgendamentoRepositorioAplicacao;
import com.cesarschool.barbearia.aplicacao.agendamento.AgendamentoServicoAplicacao;
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
     * @return Instância configurada de AgendamentoServicoAplicacao
     */
    @Bean
    public AgendamentoServicoAplicacao agendamentoServicoAplicacao(
            AgendamentoRepositorioAplicacao repositorioAplicacao,
            AgendamentoServico agendamentoServico,
            ServicoOferecidoServico servicoServico) {
        return new AgendamentoServicoAplicacao(repositorioAplicacao, agendamentoServico, servicoServico);
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
}
