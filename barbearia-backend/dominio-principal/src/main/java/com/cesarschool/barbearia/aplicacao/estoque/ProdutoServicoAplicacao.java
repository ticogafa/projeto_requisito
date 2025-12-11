package com.cesarschool.barbearia.aplicacao.estoque;

import java.util.List;

import static org.apache.commons.lang3.Validate.notNull;

import com.cesarschool.barbearia.dominio.principal.produto.Produto;
import com.cesarschool.barbearia.dominio.principal.produto.ProdutoId;
import com.cesarschool.barbearia.dominio.principal.produto.estoque.GestaoEstoqueServico;

import lombok.RequiredArgsConstructor;

/**
 * Serviço da camada de aplicação para gestão de estoque/produtos.
 * Orquestra serviços de domínio e repositórios de aplicação.
 * Seguindo padrão SGB-2025-01.
 */
@RequiredArgsConstructor
public class ProdutoServicoAplicacao {
    
    private final ProdutoRepositorioAplicacao repositorioAplicacao;
    private final GestaoEstoqueServico gestaoEstoque;

    /**
     * Lista todos os produtos em formato resumido.
     */
    public List<ProdutoResumo> pesquisarResumos() {
        return repositorioAplicacao.pesquisarResumos();
    }

    /**
     * Lista produtos com estoque abaixo do mínimo configurado.
     */
    public List<ProdutoResumo> pesquisarComEstoqueBaixo() {
        return repositorioAplicacao.pesquisarComEstoqueBaixo();
    }

    /**
     * Lista produtos em formato expandido (com informações adicionais).
     */
    public List<ProdutoResumoExpandido> pesquisarResumosExpandidos() {
        return repositorioAplicacao.pesquisarResumosExpandidos();
    }

    /**
     * Busca um produto por ID em formato resumido.
     */
    public ProdutoResumo buscarResumoPorId(Integer id) {
        notNull(id, "ID não pode ser nulo");
        return repositorioAplicacao.buscarResumoPorId(id);
    }

    /**
     * Cadastra um novo produto no sistema.
     */
    public ProdutoResumo cadastrar(CadastrarProdutoRequest request) {
        notNull(request, "Request não pode ser nulo");
        
        // Cria entidade de domínio (ID será gerado pelo banco)
        Produto produto = new Produto(
            null,
            request.getNome(),
            request.getEstoqueInicial() != null ? request.getEstoqueInicial().intValue() : 0,
            request.getPreco(),
            request.getEstoqueMinimo() != null ? request.getEstoqueMinimo().intValue() : 0
        );
        
        // Salva via serviço de domínio (valida nome único)
        Produto salvo = gestaoEstoque.cadastrarProduto(produto, request.getUsuarioResponsavel());
        
        // Retorna DTO via repositório de aplicação
        return repositorioAplicacao.buscarResumoPorId(salvo.getId());
    }

    /**
     * Atualiza um produto existente.
     */
    public ProdutoResumo atualizar(Integer id, AtualizarProdutoRequest request) {
        notNull(id, "ID não pode ser nulo");
        notNull(request, "Request não pode ser nulo");
        
        // Cria entidade atualizada
        Produto produto = new Produto(
            id,
            request.getNome(),
            request.getEstoque(),
            request.getPreco(),
            request.getEstoqueMinimo()
        );
        
        // Atualiza via serviço de domínio
        Produto atualizado = gestaoEstoque.atualizarProduto(
            new ProdutoId(id), 
            produto, 
            request.getUsuarioResponsavel()
        );
        
        // Retorna DTO
        return repositorioAplicacao.buscarResumoPorId(atualizado.getId());
    }

    /**
     * Adiciona estoque a um produto (entrada).
     */
    public ProdutoResumo adicionarEstoque(Integer id, AdicionarEstoqueRequest request) {
        notNull(id, "ID não pode ser nulo");
        notNull(request, "Request não pode ser nulo");
        
        // Adiciona via serviço de domínio
        Produto atualizado = gestaoEstoque.adicionarEstoque(
            new ProdutoId(id),
            request.getQuantidade(),
            request.getObservacao(),
            request.getUsuarioResponsavel()
        );
        
        // Retorna DTO
        return repositorioAplicacao.buscarResumoPorId(atualizado.getId());
    }

    /**
     * Remove estoque de um produto (saída).
     */
    public ProdutoResumo removerEstoque(Integer id, RemoverEstoqueRequest request) {
        notNull(id, "ID não pode ser nulo");
        notNull(request, "Request não pode ser nulo");
        
        // Remove via serviço de domínio
        Produto atualizado = gestaoEstoque.removerEstoque(
            new ProdutoId(id),
            request.getQuantidade(),
            request.getObservacao(),
            request.getUsuarioResponsavel()
        );
        
        // Retorna DTO
        return repositorioAplicacao.buscarResumoPorId(atualizado.getId());
    }

    /**
     * Registra uma venda de produto no PDV (reduz estoque).
     */
    public ProdutoResumo registrarVenda(Integer id, RegistrarVendaRequest request) {
        notNull(id, "ID não pode ser nulo");
        notNull(request, "Request não pode ser nulo");
        
        // Registra venda via serviço de domínio
        Produto atualizado = gestaoEstoque.registrarVendaPDV(
            new ProdutoId(id),
            request.getQuantidade(),
            request.getUsuarioResponsavel()
        );
        
        // Retorna DTO
        return repositorioAplicacao.buscarResumoPorId(atualizado.getId());
    }

    /**
     * Lista movimentações de estoque de um produto.
     */
    public List<MovimentacaoEstoqueResumo> listarMovimentacoesPorProduto(Integer produtoId) {
        notNull(produtoId, "ID do produto não pode ser nulo");
        return repositorioAplicacao.buscarMovimentacoesPorProduto(new ProdutoId(produtoId));
    }

    /**
     * Verifica se produto tem estoque baixo.
     */
    public boolean isEstoqueBaixo(Integer id) {
        notNull(id, "ID não pode ser nulo");
        return gestaoEstoque.isEstoqueBaixo(new ProdutoId(id));
    }

    /**
     * Deleta um produto do sistema.
     */
    public void deletar(Integer id) {
        notNull(id, "ID não pode ser nulo");
        gestaoEstoque.deletarProduto(new ProdutoId(id));
    }
}
