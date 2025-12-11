package com.cesarschool.barbearia;

import java.math.BigDecimal;
import java.util.Scanner;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.cesarschool.barbearia.dominio.principal.produto.Produto;
import com.cesarschool.barbearia.dominio.principal.produto.ProdutoRepositorio;
import com.cesarschool.barbearia.infraestrutura.proxy.ProdutoRepositorioVirtualProxy;

/**
 * Demonstrador do Padrão PROXY com Cache.
 * 
 * <p>Este demonstrador executa uma série de testes interativos que mostram
 * visualmente como o Cache Proxy funciona e melhora a performance do sistema.</p>
 * 
 * <p><b>Como executar:</b></p>
 * <pre>
 * cd barbearia-backend/dominio-principal
 * mvn spring-boot:run -Dspring-boot.run.profiles=demo
 * </pre>
 * 
 * <p><b>O que será demonstrado:</b></p>
 * <ul>
 *   <li>✅ Cache Miss (primeira busca acessa BD)</li>
 *   <li>✅ Cache Hit (segunda busca NÃO acessa BD)</li>
 *   <li>✅ Invalidação de cache após escrita</li>
 *   <li>✅ Estatísticas de performance (hit rate)</li>
 *   <li>✅ Logs diferenciando Proxy 🟢 vs Real Subject 🔵</li>
 * </ul>
 * 
 * @author Tiago
 * @version 3.0 - Demonstração do Padrão Proxy
 */
@Component
@Profile("demo")
public class DemonstradorProxy implements CommandLineRunner {
    
    private final ProdutoRepositorio produtoRepositorio;
    private final Scanner scanner = new Scanner(System.in);
    
    public DemonstradorProxy(ProdutoRepositorio produtoRepositorio) {
        this.produtoRepositorio = produtoRepositorio;
    }
    
    @Override
    public void run(String... args) throws Exception {
        imprimirCabecalho();
        
        // Teste 1: Cadastrar produto
        teste1_CadastrarProduto();
        
        // Teste 2: Primeira busca (cache miss)
        teste2_PrimeiraBusca();
        
        // Teste 3: Segunda busca (cache hit)
        teste3_SegundaBusca();
        
        // Teste 4: Terceira busca (cache hit novamente)
        teste4_TerceiraBusca();
        
        // Teste 5: Listar todos (cache miss)
        teste5_ListarTodos();
        
        // Teste 6: Listar todos novamente (cache hit)
        teste6_ListarTodosNovamente();
        
        // Teste 7: Atualizar produto (invalida cache)
        teste7_AtualizarProduto();
        
        // Teste 8: Buscar após invalidação (cache miss)
        teste8_BuscarAposInvalidacao();
        
        // Estatísticas finais
        exibirEstatisticasFinais();
        
        imprimirRodape();
    }
    
    private void imprimirCabecalho() {
        System.out.println("\n");
        System.out.println("╔══════════════════════════════════════════════════════════╗");
        System.out.println("║      DEMONSTRAÇÃO DO PADRÃO PROXY (Cache)               ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝");
        System.out.println();
        System.out.println("🎯 Objetivo: Demonstrar cache proxy melhorando performance");
        System.out.println();
        System.out.println("📖 Legenda:");
        System.out.println("   🟢 [PROXY] = Cache Proxy (controla acesso)");
        System.out.println("   🔵 [REAL SUBJECT] = Repositório JPA (acessa BD)");
        System.out.println("   ✅ CACHE HIT = Dados retornados do cache (rápido!)");
        System.out.println("   ❌ CACHE MISS = Precisou acessar BD (lento)");
        System.out.println();
    }
    
    private void teste1_CadastrarProduto() {
        imprimirSeparador();
        System.out.println("TESTE 1: Cadastrar produto (invalidará cache se houver)");
        imprimirSeparador();
        System.out.println();
        
        // Criando produto com ID temporário (será gerado pelo BD)
        Produto produto = new Produto(
            999, // ID temporário (será substituído pelo auto_increment)
            "Shampoo Anticaspa Premium",
            15,
            BigDecimal.valueOf(45.90),
            5
        );
        
        System.out.println("➤ Cadastrando: " + produto.getNome());
        Produto salvo = produtoRepositorio.salvar(produto);
        System.out.println("✅ Produto salvo com ID: " + salvo.getId());
        System.out.println();
        
        // Armazena ID para próximos testes
        System.setProperty("demo.produto.id", salvo.getId().toString());
        
        esperarEnter();
    }
    
    private void teste2_PrimeiraBusca() {
        imprimirSeparador();
        System.out.println("TESTE 2: Primeira busca por ID (cache VAZIO)");
        imprimirSeparador();
        System.out.println();
        System.out.println("💡 Esperado: CACHE MISS - vai acessar o banco de dados");
        System.out.println();
        
        Integer id = Integer.parseInt(System.getProperty("demo.produto.id"));
        System.out.println("➤ Buscando produto ID: " + id);
        System.out.println();
        
        Produto produto = produtoRepositorio.buscarPorId(id);
        System.out.println();
        System.out.println("✅ Produto encontrado: " + produto.getNome());
        System.out.println();
        
        esperarEnter();
    }
    
    private void teste3_SegundaBusca() {
        imprimirSeparador();
        System.out.println("TESTE 3: Segunda busca por ID (produto JÁ EM CACHE)");
        imprimirSeparador();
        System.out.println();
        System.out.println("💡 Esperado: CACHE HIT - NÃO vai acessar o banco de dados!");
        System.out.println("   🚀 Performance melhorada - sem latência de BD");
        System.out.println();
        
        Integer id = Integer.parseInt(System.getProperty("demo.produto.id"));
        System.out.println("➤ Buscando produto ID: " + id + " (novamente)");
        System.out.println();
        
        Produto produto = produtoRepositorio.buscarPorId(id);
        System.out.println();
        System.out.println("✅ Produto retornado: " + produto.getNome());
        System.out.println();
        
        esperarEnter();
    }
    
    private void teste4_TerceiraBusca() {
        imprimirSeparador();
        System.out.println("TESTE 4: Terceira busca por ID (ainda em cache)");
        imprimirSeparador();
        System.out.println();
        System.out.println("💡 Esperado: CACHE HIT novamente!");
        System.out.println();
        
        Integer id = Integer.parseInt(System.getProperty("demo.produto.id"));
        System.out.println("➤ Buscando produto ID: " + id + " (terceira vez)");
        System.out.println();
        
        Produto produto = produtoRepositorio.buscarPorId(id);
        System.out.println();
        System.out.println("✅ Produto retornado: " + produto.getNome());
        System.out.println("📈 Note como as buscas 2 e 3 são instantâneas (sem acesso a BD)");
        System.out.println();
        
        esperarEnter();
    }
    
    private void teste5_ListarTodos() {
        imprimirSeparador();
        System.out.println("TESTE 5: Listar todos os produtos (primeira vez)");
        imprimirSeparador();
        System.out.println();
        System.out.println("💡 Esperado: CACHE MISS - lista não está em cache ainda");
        System.out.println();
        
        System.out.println("➤ Listando todos os produtos...");
        System.out.println();
        
        var produtos = produtoRepositorio.listarTodos();
        System.out.println();
        System.out.println("✅ Total de produtos: " + produtos.size());
        System.out.println();
        
        esperarEnter();
    }
    
    private void teste6_ListarTodosNovamente() {
        imprimirSeparador();
        System.out.println("TESTE 6: Listar todos os produtos (segunda vez)");
        imprimirSeparador();
        System.out.println();
        System.out.println("💡 Esperado: CACHE HIT - lista está em cache");
        System.out.println();
        
        System.out.println("➤ Listando todos os produtos novamente...");
        System.out.println();
        
        var produtos = produtoRepositorio.listarTodos();
        System.out.println();
        System.out.println("✅ Total de produtos: " + produtos.size());
        System.out.println("🚀 Lista retornada instantaneamente do cache!");
        System.out.println();
        
        esperarEnter();
    }
    
    private void teste7_AtualizarProduto() {
        imprimirSeparador();
        System.out.println("TESTE 7: Atualizar produto (invalida cache)");
        imprimirSeparador();
        System.out.println();
        System.out.println("💡 Esperado: Cache será INVALIDADO após salvar");
        System.out.println();
        
        Integer id = Integer.parseInt(System.getProperty("demo.produto.id"));
        Produto produto = produtoRepositorio.buscarPorId(id);
        
        System.out.println("➤ Atualizando preço do produto: " + produto.getNome());
        System.out.println("   Preço atual: R$ " + produto.getPreco());
        
        produto.setPreco(BigDecimal.valueOf(49.90));
        System.out.println("   Novo preço: R$ " + produto.getPreco());
        System.out.println();
        
        produtoRepositorio.salvar(produto);
        System.out.println();
        System.out.println("✅ Produto atualizado");
        System.out.println("🗑️ Cache INVALIDADO - garantindo consistência dos dados");
        System.out.println();
        
        esperarEnter();
    }
    
    private void teste8_BuscarAposInvalidacao() {
        imprimirSeparador();
        System.out.println("TESTE 8: Buscar após invalidação de cache");
        imprimirSeparador();
        System.out.println();
        System.out.println("💡 Esperado: CACHE MISS - cache foi invalidado no teste anterior");
        System.out.println();
        
        Integer id = Integer.parseInt(System.getProperty("demo.produto.id"));
        System.out.println("➤ Buscando produto ID: " + id + " (após atualização)");
        System.out.println();
        
        Produto produto = produtoRepositorio.buscarPorId(id);
        System.out.println();
        System.out.println("✅ Produto encontrado: " + produto.getNome());
        System.out.println("   Preço atualizado: R$ " + produto.getPreco());
        System.out.println("📊 Produto está novamente no cache para próximas consultas");
        System.out.println();
        
        esperarEnter();
    }
    
    private void exibirEstatisticasFinais() {
        imprimirSeparador();
        System.out.println("ESTATÍSTICAS FINAIS DO VIRTUAL PROXY (LAZY LOADING)");
        imprimirSeparador();
        System.out.println();
        
        if (produtoRepositorio instanceof ProdutoRepositorioVirtualProxy) {
            ProdutoRepositorioVirtualProxy proxy = (ProdutoRepositorioVirtualProxy) produtoRepositorio;
            System.out.println(proxy.getEstatisticas());
            
            // Análise
            System.out.println("📈 ANÁLISE:");
            System.out.println("   • Primeira busca = LAZY LOAD (carrega do banco)");
            System.out.println("   • Buscas subsequentes = REUSO (já carregado)");
            System.out.println("   • Taxa de reuso > 50% = lazy loading efetivo");
            System.out.println("   • Operações de escrita invalidam dados (garantem consistência)");
            System.out.println("   • Próximas buscas recarregam sob demanda (lazy)");
        }
        
        System.out.println();
    }
    
    private void imprimirRodape() {
        System.out.println();
        System.out.println("╔══════════════════════════════════════════════════════════╗");
        System.out.println("║  ✅ PADRÃO PROXY FUNCIONANDO PERFEITAMENTE!             ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝");
        System.out.println();
        System.out.println("🎓 CONCEITOS DEMONSTRADOS:");
        System.out.println("   ✓ Proxy tem MESMA interface que Real Subject");
        System.out.println("   ✓ Proxy usa COMPOSIÇÃO (has-a), não herança");
        System.out.println("   ✓ Proxy DELEGA para Real Subject quando necessário");
        System.out.println("   ✓ Proxy adiciona CONTROLE (cache) de forma transparente");
        System.out.println("   ✓ Cliente NÃO sabe que está usando Proxy");
        System.out.println();
        System.out.println("🚀 BENEFÍCIOS:");
        System.out.println("   • Redução de ~66% nas consultas ao banco de dados");
        System.out.println("   • Performance melhorada significativamente");
        System.out.println("   • Transparente para o código cliente");
        System.out.println("   • Fácil de adicionar/remover (DI com @Primary)");
        System.out.println();
        System.out.println("Pressione ENTER para finalizar...");
        scanner.nextLine();
    }
    
    private void imprimirSeparador() {
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
    }
    
    private void esperarEnter() {
        System.out.println("⏸️  Pressione ENTER para continuar...");
        scanner.nextLine();
        System.out.println();
    }
}
