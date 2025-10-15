package com.cesarschool.barbearia.dominio.principal.venda;

import java.util.List;

import com.cesarschool.barbearia.dominio.compartilhado.utils.Validacoes;
import com.cesarschool.barbearia.dominio.principal.itemvenda.ItemVenda;
import com.cesarschool.barbearia.dominio.principal.produto.Produto;
import com.cesarschool.barbearia.dominio.principal.produto.ProdutoServico;

public class VendaServico {

    private final VendaRepositorio repositorio;
    private final ProdutoServico produtoRepository;

    public VendaServico(VendaRepositorio repositorio, ProdutoServico produtoRepository) {
        Validacoes.validarObjetoObrigatorio(repositorio, "O repositório de vendas");
        Validacoes.validarObjetoObrigatorio(produtoRepository, "O repositório de produtos");
        this.repositorio = repositorio;
        this.produtoRepository = produtoRepository;
    }

    /**
     * Registra uma nova venda realizando validações de estoque.
     * Regras de negócio:
     * - Deve conter ao menos um item
     * - Estoque dos produtos deve ser suficiente
     * - Estoque é automaticamente atualizado após a venda
     */
    public Venda registrarVendaPDV(Venda venda) {
        Validacoes.validarObjetoObrigatorio(venda, "A venda");
        
        if (venda.getItens() == null || venda.getItens().isEmpty()) {
            throw new IllegalArgumentException("Venda deve conter ao menos um item");
        }
        
        // Valida e atualiza estoque antes de salvar
        validarEAtualizarEstoque(venda.getItens());
        
        return repositorio.salvar(venda);
    }

    public Venda buscarPorId(VendaId id) {
        Validacoes.validarObjetoObrigatorio(id, "ID da venda");
        return repositorio.buscarPorId(id.getValor());
    }

    public List<Venda> listarTodas() {
        return repositorio.listarTodos();
    }

    /**
     * Valida se há estoque suficiente e atualiza o estoque dos produtos.
     * Regra de negócio: operação atômica - se um produto não tiver estoque, nenhum é atualizado.
     */
    private void validarEAtualizarEstoque(List<ItemVenda> itens) {
        // Primeira passagem: valida se todos os produtos têm estoque suficiente
        for (ItemVenda item : itens) {
            if (item.getProdutoId() != null && item.getQuantidade() > 0) {
                Produto p = produtoRepository.buscarPorId(item.getProdutoId());
                
                if (p.getEstoque() < item.getQuantidade()) {
                    throw new IllegalStateException(
                        "Estoque insuficiente para o produto: " + p.getNome() + 
                        ". Disponível: " + p.getEstoque() + ", Solicitado: " + item.getQuantidade()
                    );
                }
            }
        }
        
        // Segunda passagem: atualiza o estoque (só executa se passou pela validação completa)
        for (ItemVenda item : itens) {
            if (item.getProdutoId() != null && item.getQuantidade() > 0) {
                Produto p = produtoRepository.buscarPorId(item.getProdutoId());
                p.setEstoque(p.getEstoque() - item.getQuantidade());
                produtoRepository.salvar(p);
            }
        }
    }

    public void remover(VendaId id) {
        Validacoes.validarObjetoObrigatorio(id, "ID da venda");
        buscarPorId(id); // Verifica se existe
        repositorio.remover(id.getValor());
    }
}