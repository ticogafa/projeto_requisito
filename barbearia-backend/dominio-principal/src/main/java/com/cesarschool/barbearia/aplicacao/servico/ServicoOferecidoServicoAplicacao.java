package com.cesarschool.barbearia.aplicacao.servico;

import java.util.List;

import com.cesarschool.barbearia.dominio.compartilhado.utils.Validacoes;

public class ServicoOferecidoServicoAplicacao {

	private ServicoOferecidolRepositorioAplicacao repositorio;

    public ServicoOferecidoServicoAplicacao(ServicoOferecidolRepositorioAplicacao repositorio) {
        Validacoes.validarObjetoObrigatorio(repositorio, "repositorio");
        this.repositorio = repositorio;
    }

    public List<ServicoOferecidoResumo> pesquisarResumos() {
        return repositorio.listarTodosResumos();
    }
}
