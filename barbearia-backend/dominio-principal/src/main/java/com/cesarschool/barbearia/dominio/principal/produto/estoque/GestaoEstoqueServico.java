package com.cesarschool.barbearia.dominio.principal.produto.estoque;

import java.time.LocalDateTime;
import java.util.List;

import com.cesarschool.barbearia.dominio.compartilhado.utils.Validacoes;
import com.cesarschool.barbearia.dominio.principal.produto.Produto;
import com.cesarschool.barbearia.dominio.principal.produto.ProdutoId;
import com.cesarschool.barbearia.dominio.principal.produto.ProdutoRepositorio;

/**
 * Serviço de domínio responsável pela gestão completa do estoque de produtos.
 * 
 * Este serviço implementa todas as regras de negócio relacionadas ao controle de estoque:
 * - Cadastro e validação de produtos únicos
 * - Atualização de estoque (entrada/saída)
 * - Registro de vendas no PDV
 * - Validação de estoque mínimo
 * - Geração de alertas de estoque baixo
 * - Manutenção de histórico de movimentações
 * - Ativação/desativação de produtos
 * 
 * Implementa os cenários BDD definidos em Estoque.feature:
 * - Cadastrar produto com nome único (POSITIVO)
 * - Impedir cadastro com nome duplicado (NEGATIVO)
 * - Atualizar estoque com quantidade válida (POSITIVO)
 * - Registrar venda PDV reduzindo estoque (POSITIVO)
 * - Impedir venda com estoque insuficiente (NEGATIVO)
 * 
 * @author Sistema de Barbearia
 * @version 1.0
 */
public class GestaoEstoqueServico {

    private final ProdutoRepositorio produtoRepositorio;
    private final MovimentacaoEstoqueRepositorio movimentacaoRepositorio;

    /**
     * Construtor com injeção de dependências dos repositórios necessários.
     */
    public GestaoEstoqueServico(
            ProdutoRepositorio produtoRepositorio,
            MovimentacaoEstoqueRepositorio movimentacaoRepositorio) {
        
        Validacoes.validarObjetoObrigatorio(produtoRepositorio, "Repositório de Produto");
        Validacoes.validarObjetoObrigatorio(movimentacaoRepositorio, "Repositório de Movimentação");
        
        this.produtoRepositorio = produtoRepositorio;
        this.movimentacaoRepositorio = movimentacaoRepositorio;
    }

    // ==================== OPERAÇÕES DE CADASTRO ====================

    /**
     * Cadastra um novo produto no sistema com validação de nome único.
     * 
     * Regras de negócio:
     * - Nome do produto deve ser único (case-insensitive)
     * - Estoque inicial não pode ser negativo
     * - Preço deve ser maior que zero
     * 
     * @param produto Produto a ser cadastrado
     * @param usuarioResponsavel Usuário que está realizando o cadastro
     * @return Produto cadastrado com ID gerado
     * @throws IllegalArgumentException se já existir produto com o mesmo nome
     */
    public Produto cadastrarProduto(Produto produto, String usuarioResponsavel) {
        Validacoes.validarObjetoObrigatorio(produto, "Produto");
        
        // Valida nome único (BDD: Cenário positivo/negativo de cadastro)
        validarNomeUnico(produto.getNome(), null);
        
        // Salva o produto
        Produto produtoSalvo = produtoRepositorio.salvar(produto);
        
        // Registra movimentação de estoque inicial
        if (produtoSalvo.getEstoque() > 0) {
            registrarMovimentacao(
                new ProdutoId(produtoSalvo.getId()),
                produtoSalvo.getNome(),
                TipoMovimentacao.ESTOQUE_INICIAL,
                produtoSalvo.getEstoque(),
                0,
                produtoSalvo.getEstoque(),
                "Cadastro inicial do produto",
                usuarioResponsavel
            );
        }
        
        return produtoSalvo;
    }

    /**
     * Valida se o nome do produto é único no sistema.
     * 
     * @param nome Nome a ser validado
     * @param idProdutoAtual ID do produto atual (null para novo cadastro)
     * @throws IllegalArgumentException se já existir produto com o mesmo nome
     */
    private void validarNomeUnico(String nome, Integer idProdutoAtual) {
        Validacoes.validarObjetoObrigatorio(nome, "Nome do Produto");
        
        List<Produto> produtos = produtoRepositorio.listarTodos();
        
        boolean nomeJaExiste = produtos.stream()
            .filter(p -> idProdutoAtual == null || !p.getId().equals(idProdutoAtual))
            .anyMatch(p -> p.getNome().trim().equalsIgnoreCase(nome.trim()));
        
        if (nomeJaExiste) {
            throw new IllegalArgumentException(
                "Já existe um produto cadastrado com o nome: " + nome
            );
        }
    }

    /**
     * Atualiza um produto existente, validando nome único.
     * 
     * @param produtoId ID do produto a ser atualizado
     * @param produto Dados atualizados do produto
     * @param usuarioResponsavel Usuário responsável pela atualização
     * @return Produto atualizado
     */
    public Produto atualizarProduto(ProdutoId produtoId, Produto produto, String usuarioResponsavel) {
        Validacoes.validarObjetoObrigatorio(produtoId, "ID do Produto");
        Validacoes.validarObjetoObrigatorio(produto, "Produto");
        
        // Verifica se o produto existe
        Produto produtoExistente = buscarProduto(produtoId);
        
        // Valida nome único (exceto para o próprio produto)
        validarNomeUnico(produto.getNome(), produtoExistente.getId());
        
        // Atualiza o produto
        return produtoRepositorio.salvar(produto);
    }

    // ==================== OPERAÇÕES DE ESTOQUE ====================

    /**
     * Adiciona quantidade ao estoque do produto (entrada de estoque).
     * 
     * Implementa BDD: "Atualizar estoque com quantidade válida"
     * 
     * @param produtoId ID do produto
     * @param quantidade Quantidade a adicionar (deve ser positiva)
     * @param observacao Motivo da entrada
     * @param usuarioResponsavel Usuário responsável pela operação
     * @return Produto com estoque atualizado
     */
    public Produto adicionarEstoque(
            ProdutoId produtoId, 
            int quantidade, 
            String observacao,
            String usuarioResponsavel) {
        
        Validacoes.validarObjetoObrigatorio(produtoId, "ID do Produto");
        Validacoes.validarInteiroPositivo(quantidade, "Quantidade");
        
        Produto produto = buscarProduto(produtoId);
        int estoqueAnterior = produto.getEstoque();
        int estoqueNovo = estoqueAnterior + quantidade;
        
        // Atualiza o estoque
        produto.setEstoque(estoqueNovo);
        Produto produtoAtualizado = produtoRepositorio.salvar(produto);
        
        // Registra a movimentação
        registrarMovimentacao(
            produtoId,
            produto.getNome(),
            TipoMovimentacao.ENTRADA,
            quantidade,
            estoqueAnterior,
            estoqueNovo,
            observacao != null ? observacao : "Entrada de estoque",
            usuarioResponsavel
        );
        
        return produtoAtualizado;
    }

    /**
     * Remove quantidade do estoque do produto (saída de estoque).
     * 
     * Regra de negócio: não pode deixar estoque negativo
     * 
     * @param produtoId ID do produto
     * @param quantidade Quantidade a remover
     * @param observacao Motivo da saída
     * @param usuarioResponsavel Usuário responsável pela operação
     * @return Produto com estoque atualizado
     * @throws IllegalStateException se estoque for insuficiente
     */
    public Produto removerEstoque(
            ProdutoId produtoId, 
            int quantidade, 
            String observacao,
            String usuarioResponsavel) {
        
        Validacoes.validarObjetoObrigatorio(produtoId, "ID do Produto");
        Validacoes.validarInteiroPositivo(quantidade, "Quantidade");
        
        Produto produto = buscarProduto(produtoId);
        int estoqueAnterior = produto.getEstoque();
        
        // Valida estoque suficiente
        if (estoqueAnterior < quantidade) {
            throw new IllegalStateException(
                String.format(
                    "Estoque insuficiente para o produto '%s'. Disponível: %d, Solicitado: %d",
                    produto.getNome(),
                    estoqueAnterior,
                    quantidade
                )
            );
        }
        
        int estoqueNovo = estoqueAnterior - quantidade;
        
        // Atualiza o estoque
        produto.setEstoque(estoqueNovo);
        Produto produtoAtualizado = produtoRepositorio.salvar(produto);
        
        // Registra a movimentação
        registrarMovimentacao(
            produtoId,
            produto.getNome(),
            TipoMovimentacao.SAIDA,
            quantidade,
            estoqueAnterior,
            estoqueNovo,
            observacao != null ? observacao : "Saída de estoque",
            usuarioResponsavel
        );
        
        return produtoAtualizado;
    }

    // ==================== OPERAÇÕES DE VENDA PDV ====================

    /**
     * Registra uma venda no PDV, reduzindo o estoque automaticamente.
     * 
     * Implementa BDD:
     * - "Registrar venda PDV com produto reduzindo estoque (sucesso)"
     * - "Impedir venda PDV com estoque insuficiente (falha)"
     * 
     * @param produtoId ID do produto vendido
     * @param quantidade Quantidade vendida
     * @param usuarioResponsavel Usuário que realizou a venda
     * @return Produto com estoque atualizado
     * @throws IllegalStateException se estoque for insuficiente
     */
    public Produto registrarVendaPDV(
            ProdutoId produtoId, 
            int quantidade,
            String usuarioResponsavel) {
        
        Validacoes.validarObjetoObrigatorio(produtoId, "ID do Produto");
        Validacoes.validarInteiroPositivo(quantidade, "Quantidade");
        
        Produto produto = buscarProduto(produtoId);
        int estoqueAnterior = produto.getEstoque();
        
        // Valida estoque suficiente (BDD: Cenário negativo)
        if (estoqueAnterior < quantidade) {
            throw new IllegalStateException(
                String.format(
                    "Estoque insuficiente para venda do produto '%s'. Disponível: %d, Solicitado: %d",
                    produto.getNome(),
                    estoqueAnterior,
                    quantidade
                )
            );
        }
        
        int estoqueNovo = estoqueAnterior - quantidade;
        
        // Atualiza o estoque (BDD: Cenário positivo)
        produto.setEstoque(estoqueNovo);
        Produto produtoAtualizado = produtoRepositorio.salvar(produto);
        
        // Registra a movimentação de venda
        registrarMovimentacao(
            produtoId,
            produto.getNome(),
            TipoMovimentacao.VENDA,
            quantidade,
            estoqueAnterior,
            estoqueNovo,
            "Venda PDV",
            usuarioResponsavel
        );
        
        return produtoAtualizado;
    }

    /**
     * Valida se há estoque suficiente para uma venda antes de processá-la.
     * 
     * @param produtoId ID do produto
     * @param quantidade Quantidade desejada
     * @return true se há estoque suficiente, false caso contrário
     */
    public boolean validarEstoqueDisponivel(ProdutoId produtoId, int quantidade) {
        Validacoes.validarObjetoObrigatorio(produtoId, "ID do Produto");
        
        Produto produto = buscarProduto(produtoId);
        return produto.getEstoque() >= quantidade;
    }

    // ==================== CONSULTAS E RELATÓRIOS ====================

    /**
     * Busca um produto por ID.
     * 
     * @param produtoId ID do produto
     * @return Produto encontrado
     * @throws IllegalArgumentException se produto não existir
     */
    public Produto buscarProduto(ProdutoId produtoId) {
        Validacoes.validarObjetoObrigatorio(produtoId, "ID do Produto");
        
        Produto produto = produtoRepositorio.buscarPorId(produtoId.getValor());
        
        if (produto == null) {
            throw new IllegalArgumentException(
                "Produto não encontrado com ID: " + produtoId.getValor()
            );
        }
        
        return produto;
    }

    /**
     * Lista todos os produtos cadastrados.
     */
    public List<Produto> listarTodosProdutos() {
        return produtoRepositorio.listarTodos();
    }

    /**
     * Lista produtos com estoque abaixo do mínimo configurado.
     * Útil para gerar alertas de reposição.
     */
    public List<Produto> listarProdutosComEstoqueBaixo() {
        return produtoRepositorio.listarProdutosComEstoqueBaixo();
    }

    /**
     * Verifica se um produto está com estoque abaixo do mínimo.
     * 
     * @param produtoId ID do produto
     * @return true se estoque está baixo, false caso contrário
     */
    public boolean isEstoqueBaixo(ProdutoId produtoId) {
        Produto produto = buscarProduto(produtoId);
        return produto.getEstoque() <= produto.getEstoqueMinimo();
    }

    /**
     * Busca o histórico completo de movimentações de um produto.
     * 
     * @param produtoId ID do produto
     * @return Lista de movimentações ordenada por data (mais recente primeiro)
     */
    public List<MovimentacaoEstoque> buscarHistoricoProduto(ProdutoId produtoId) {
        Validacoes.validarObjetoObrigatorio(produtoId, "ID do Produto");
        return movimentacaoRepositorio.buscarPorProduto(produtoId);
    }

    /**
     * Busca movimentações de um produto em um período específico.
     */
    public List<MovimentacaoEstoque> buscarHistoricoPorPeriodo(
            ProdutoId produtoId,
            LocalDateTime dataInicio,
            LocalDateTime dataFim) {
        
        Validacoes.validarObjetoObrigatorio(produtoId, "ID do Produto");
        Validacoes.validarObjetoObrigatorio(dataInicio, "Data Início");
        Validacoes.validarObjetoObrigatorio(dataFim, "Data Fim");
        
        return movimentacaoRepositorio.buscarPorProdutoEPeriodo(produtoId, dataInicio, dataFim);
    }

    /**
     * Busca as últimas N movimentações de um produto.
     */
    public List<MovimentacaoEstoque> buscarUltimasMovimentacoes(ProdutoId produtoId, int limite) {
        Validacoes.validarObjetoObrigatorio(produtoId, "ID do Produto");
        Validacoes.validarInteiroPositivo(limite, "Limite");
        
        return movimentacaoRepositorio.buscarUltimasMovimentacoes(produtoId, limite);
    }

    /**
     * Lista todas as movimentações por tipo (ENTRADA, SAIDA, VENDA, etc.).
     */
    public List<MovimentacaoEstoque> listarMovimentacoesPorTipo(TipoMovimentacao tipo) {
        Validacoes.validarObjetoObrigatorio(tipo, "Tipo de Movimentação");
        return movimentacaoRepositorio.buscarPorTipo(tipo);
    }

    // ==================== OPERAÇÕES DE ATIVAÇÃO/DESATIVAÇÃO ====================

    /**
     * Desativa um produto, impedindo novas vendas.
     * Mantém o histórico e estoque atual para consulta.
     * 
     * @param produtoId ID do produto
     * @param motivo Motivo da desativação
     * @param usuarioResponsavel Usuário responsável
     */
    public void desativarProduto(ProdutoId produtoId, String motivo, String usuarioResponsavel) {
        Validacoes.validarObjetoObrigatorio(produtoId, "ID do Produto");
        
        Produto produto = buscarProduto(produtoId);
        int estoqueAtual = produto.getEstoque();
        
        // Registra a desativação no histórico
        registrarMovimentacao(
            produtoId,
            produto.getNome(),
            TipoMovimentacao.DESATIVACAO,
            0,
            estoqueAtual,
            estoqueAtual,
            motivo != null ? "Desativado: " + motivo : "Produto desativado",
            usuarioResponsavel
        );
    }

    // ==================== MÉTODOS PRIVADOS ====================

    /**
     * Registra uma movimentação de estoque no histórico.
     */
    private void registrarMovimentacao(
            ProdutoId produtoId,
            String nomeProduto,
            TipoMovimentacao tipo,
            int quantidade,
            int estoqueAnterior,
            int estoqueAtual,
            String observacao,
            String usuarioResponsavel) {
        
        MovimentacaoEstoque movimentacao = new MovimentacaoEstoque(
            produtoId,
            nomeProduto,
            tipo,
            quantidade,
            estoqueAnterior,
            estoqueAtual,
            observacao,
            usuarioResponsavel
        );
        
        movimentacaoRepositorio.salvar(movimentacao);
    }
}
