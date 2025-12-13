package com.cesarschool.barbearia.dominio.principal.produto;

import java.util.List;

import com.cesarschool.barbearia.dominio.compartilhado.base.Repositorio;

/**
 * Interface Subject do Padrão PROXY.
 * 
 * <p>Esta interface define o contrato comum entre o Real Subject (ProdutoRepositorioJpa)
 * e o Virtual Proxy com lazy loading (ProdutoRepositorioVirtualProxy).</p>
 * 
 * <p><b>Padrão Proxy:</b> Fornece um substituto para outro objeto, controlando o acesso a ele.</p>
 * 
 * @author Tiago
 * @version 4.0
 */
public interface ProdutoRepositorio extends Repositorio<Produto, Integer>{
	/**
	 * Busca produtos com estoque abaixo do mínimo.
	 * @return Lista de produtos com estoque baixo
	 */
	List<Produto> findProdutosComEstoqueBaixo();

	/**
	 * Busca produtos com estoque abaixo do mínimo (método alternativo).
	 * @return Lista de produtos com estoque baixo
	 */
    List<Produto> listarProdutosComEstoqueBaixo();
}
