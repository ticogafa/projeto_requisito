package com.cesarschool.barbearia.apresentacao.produto;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.cesarschool.barbearia.aplicacao.estoque.AdicionarEstoqueRequest;
import com.cesarschool.barbearia.aplicacao.estoque.AtualizarProdutoRequest;
import com.cesarschool.barbearia.aplicacao.estoque.CadastrarProdutoRequest;
import com.cesarschool.barbearia.aplicacao.estoque.MovimentacaoEstoqueResumo;
import com.cesarschool.barbearia.aplicacao.estoque.ProdutoResumo;
import com.cesarschool.barbearia.aplicacao.estoque.ProdutoServicoAplicacao;
import com.cesarschool.barbearia.aplicacao.estoque.RegistrarVendaRequest;
import com.cesarschool.barbearia.aplicacao.estoque.RemoverEstoqueRequest;
import com.cesarschool.barbearia.dominio.compartilhado.exceptions.ExceptionHandler;
import com.cesarschool.barbearia.dominio.compartilhado.logger.LoggerSingleton;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoControlador {

    private static final LoggerSingleton logger = LoggerSingleton.getInstance();

    @Autowired
    private ProdutoServicoAplicacao produtoServico;

    @Autowired
    private ExceptionHandler exceptionHandler;

    @PostMapping
    public ResponseEntity<ProdutoResumo> cadastrar(@RequestBody CadastrarProdutoRequest request) {
        return exceptionHandler.withHandler(() -> {
            logger.info("Cadastrando novo produto: " + request.getNome());
            ProdutoResumo produto = produtoServico.cadastrar(request);

            URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(produto.getId())
                    .toUri();

            return ResponseEntity.created(uri).body(produto);
        });
    }

    @GetMapping
    public ResponseEntity<List<ProdutoResumo>> listarTodos() {
        return exceptionHandler.withHandler(() -> {
            logger.info("Listando todos os produtos");
            return ResponseEntity.ok(produtoServico.pesquisarResumos());
        });
    }

    @GetMapping("/estoque-baixo")
    public ResponseEntity<List<ProdutoResumo>> listarEstoqueBaixo() {
        return exceptionHandler.withHandler(() -> {
            logger.info("Listando produtos com estoque baixo");
            return ResponseEntity.ok(produtoServico.pesquisarComEstoqueBaixo());
        });
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProdutoResumo> buscarPorId(@PathVariable Integer id) {
        return exceptionHandler.withHandler(() -> {
            logger.info("Buscando produto ID: " + id);
            return ResponseEntity.ok(produtoServico.buscarResumoPorId(id));
        });
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProdutoResumo> atualizar(
            @PathVariable Integer id,
            @RequestBody AtualizarProdutoRequest request) {
        return exceptionHandler.withHandler(() -> {
            logger.info("Atualizando produto ID: " + id);
            ProdutoResumo produto = produtoServico.atualizar(id, request);
            return ResponseEntity.ok(produto);
        });
    }

    @PostMapping("/{id}/adicionar-estoque")
    public ResponseEntity<ProdutoResumo> adicionarEstoque(
            @PathVariable Integer id,
            @RequestBody AdicionarEstoqueRequest request) {
        return exceptionHandler.withHandler(() -> {
            logger.info("Adicionando estoque ao produto ID: " + id + ", Quantidade: " + request.getQuantidade());
            ProdutoResumo produto = produtoServico.adicionarEstoque(id, request);
            return ResponseEntity.ok(produto);
        });
    }

    @PostMapping("/{id}/remover-estoque")
    public ResponseEntity<ProdutoResumo> removerEstoque(
            @PathVariable Integer id,
            @RequestBody RemoverEstoqueRequest request) {
        return exceptionHandler.withHandler(() -> {
            logger.info("Removendo estoque do produto ID: " + id + ", Quantidade: " + request.getQuantidade());
            ProdutoResumo produto = produtoServico.removerEstoque(id, request);
            return ResponseEntity.ok(produto);
        });
    }

    @PostMapping("/{id}/registrar-venda")
    public ResponseEntity<ProdutoResumo> registrarVenda(
            @PathVariable Integer id,
            @RequestBody RegistrarVendaRequest request) {
        return exceptionHandler.withHandler(() -> {
            logger.info("Registrando venda do produto ID: " + id + ", Quantidade: " + request.getQuantidade());
            ProdutoResumo produto = produtoServico.registrarVenda(id, request);
            return ResponseEntity.ok(produto);
        });
    }

    @GetMapping("/{id}/movimentacoes")
    public ResponseEntity<List<MovimentacaoEstoqueResumo>> listarMovimentacoes(@PathVariable Integer id) {
        return exceptionHandler.withHandler(() -> {
            logger.info("Listando movimentações do produto ID: " + id);
            return ResponseEntity.ok(produtoServico.listarMovimentacoesPorProduto(id));
        });
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        return exceptionHandler.withHandler(() -> {
            logger.info("Deletando produto ID: " + id);
            produtoServico.deletar(id);
            return ResponseEntity.noContent().build();
        });
    }
}
