package com.cesarschool.barbearia.dominio.principal.produto;

import java.util.List;

import com.cesarschool.barbearia.dominio.compartilhado.base.Repositorio;

public interface ProdutoRepositorio extends Repositorio<Produto, Integer>{
	List<Produto> findProdutosComEstoqueBaixo();

    List<Produto> listarProdutosComEstoqueBaixo();
}
