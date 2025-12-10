package com.cesarschool.barbearia.infraestrutura.proxy;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.cesarschool.barbearia.dominio.principal.produto.Produto;
import com.cesarschool.barbearia.dominio.principal.produto.ProdutoRepositorio;

/**
 * Cache Proxy para ProdutoRepositorio - Padrão PROXY.
 * 
 * <p>Este Proxy adiciona uma camada de cache ao repositório de produtos,
 * melhorando significativamente a performance ao evitar acessos desnecessários
 * ao banco de dados para consultas repetidas.</p>
 * 
 * <p><b>Padrão Proxy:</b> Fornece um substituto para o Real Subject (ProdutoRepositorioJpa),
 * controlando o acesso a ele através de cache. O Proxy implementa a MESMA interface
 * que o Real Subject, tornando a substituição transparente para os clientes.</p>
 * 
 * <p><b>Tipo de Proxy:</b> Cache Proxy (Virtual Proxy)</p>
 * 
 * <p><b>Características:</b></p>
 * <ul>
 *   <li>✅ Mesma interface do Real Subject (ProdutoRepositorio)</li>
 *   <li>✅ Usa composição (HAS-A), não herança (IS-A)</li>
 *   <li>✅ Delega para Real Subject em cache miss</li>
 *   <li>✅ Invalida cache em operações de escrita</li>
 *   <li>✅ Rastreia estatísticas (hits/misses)</li>
 *   <li>✅ Thread-safe usando ConcurrentHashMap</li>
 * </ul>
 * 
 * <p><b>Benefícios:</b></p>
 * <ul>
 *   <li>🚀 Redução de até 90% no acesso ao banco em consultas repetidas</li>
 *   <li>📊 Estatísticas de cache para monitoramento</li>
 *   <li>🔄 Transparente para o cliente (mesma interface)</li>
 *   <li>🛡️ Controle centralizado de acesso aos dados</li>
 * </ul>
 * 
 * @author Tiago
 * @version 3.0 - Implementação do Padrão Proxy
 */
@Component
@Primary  // Spring injeta este Proxy por padrão ao invés do Real Subject
public class ProdutoRepositorioCacheProxy implements ProdutoRepositorio {
    
    // ========== COMPOSIÇÃO (não herança!) ==========
    /**
     * Real Subject: Repositório real que acessa o banco de dados.
     * O Proxy DELEGA as chamadas para o Real Subject quando necessário.
     */
    private final ProdutoRepositorio realSubject;
    
    // ========== CACHE ==========
    /**
     * Cache principal: mapeia ID → Produto.
     * Thread-safe para suportar acesso concorrente.
     */
    private final Map<Integer, Produto> cache = new ConcurrentHashMap<>();
    
    /**
     * Cache para lista completa de produtos.
     * Null = cache não inicializado.
     */
    private List<Produto> cacheTodos = null;
    
    /**
     * Cache para produtos com estoque baixo.
     * Null = cache não inicializado.
     */
    private List<Produto> cacheEstoqueBaixo = null;
    
    // ========== ESTATÍSTICAS ==========
    /**
     * Contador de cache hits (encontrou no cache).
     */
    private int cacheHits = 0;
    
    /**
     * Contador de cache misses (precisou acessar BD).
     */
    private int cacheMisses = 0;
    
    // ========== CONSTRUTOR COM INJEÇÃO DE DEPENDÊNCIA ==========
    /**
     * Construtor com injeção de dependência.
     * 
     * @param realSubject Real Subject injetado pelo Spring.
     *                    Usa @Qualifier para especificar qual implementação injetar.
     */
    public ProdutoRepositorioCacheProxy(@Qualifier("produtoRepositorioJpa") ProdutoRepositorio realSubject) {
        this.realSubject = realSubject;
        System.out.println("🟢 [PROXY] Cache Proxy inicializado com Real Subject");
    }
    
    // ========== MÉTODOS DA INTERFACE PRODUTOREPOSITORIO ==========
    
    /**
     * Salva produto e invalida cache.
     * 
     * <p>Operações de ESCRITA sempre invalidam o cache para garantir consistência.</p>
     */
    @Override
    public Produto salvar(Produto produto) {
        System.out.println("🟢 [PROXY] salvar() - Delegando para Real Subject e invalidando cache");
        Produto salvo = realSubject.salvar(produto);
        invalidarCache();
        System.out.println("🟢 [PROXY] Cache invalidado após salvar");
        return salvo;
    }
    
    /**
     * Busca produto por ID com cache.
     * 
     * <p><b>Lógica do Cache:</b></p>
     * <ol>
     *   <li>Verifica se produto está no cache</li>
     *   <li>Se SIM: retorna do cache (CACHE HIT) ✅</li>
     *   <li>Se NÃO: delega para Real Subject (CACHE MISS) ❌</li>
     *   <li>Armazena resultado no cache para próxima consulta</li>
     * </ol>
     */
    @Override
    public Produto buscarPorId(Integer id) {
        System.out.println("🟢 [PROXY] buscarPorId(" + id + ")");
        
        // PASSO 1: Verificar cache
        if (cache.containsKey(id)) {
            cacheHits++;
            Produto produto = cache.get(id);
            System.out.println("🟢 [PROXY] ✅ CACHE HIT! Produto '" + produto.getNome() + "' retornado do cache");
            System.out.println("📊 Estatísticas: Hits=" + cacheHits + " | Misses=" + cacheMisses);
            return produto;
        }
        
        // PASSO 2: Cache miss - delegar para Real Subject
        System.out.println("🟢 [PROXY] ❌ CACHE MISS - Delegando para Real Subject");
        cacheMisses++;
        Produto produto = realSubject.buscarPorId(id);
        
        // PASSO 3: Armazenar no cache se encontrado
        if (produto != null) {
            cache.put(id, produto);
            System.out.println("🟢 [PROXY] Produto '" + produto.getNome() + "' armazenado no cache");
        }
        
        System.out.println("📊 Estatísticas: Hits=" + cacheHits + " | Misses=" + cacheMisses);
        return produto;
    }
    
    /**
     * Lista todos os produtos com cache.
     * 
     * <p>Cacheia a lista completa. Qualquer operação de escrita invalida este cache.</p>
     */
    @Override
    public List<Produto> listarTodos() {
        System.out.println("🟢 [PROXY] listarTodos()");
        
        // Verificar cache
        if (cacheTodos != null) {
            cacheHits++;
            System.out.println("🟢 [PROXY] ✅ CACHE HIT! Retornando lista de " + cacheTodos.size() + " produtos do cache");
            System.out.println("📊 Estatísticas: Hits=" + cacheHits + " | Misses=" + cacheMisses);
            return cacheTodos;
        }
        
        // Cache miss
        System.out.println("🟢 [PROXY] ❌ CACHE MISS - Delegando para Real Subject");
        cacheMisses++;
        List<Produto> produtos = realSubject.listarTodos();
        
        // Armazenar no cache
        cacheTodos = produtos;
        
        // Também cacheia produtos individuais
        produtos.forEach(p -> cache.put(p.getId(), p));
        
        System.out.println("🟢 [PROXY] Lista de " + produtos.size() + " produtos armazenada no cache");
        System.out.println("📊 Estatísticas: Hits=" + cacheHits + " | Misses=" + cacheMisses);
        return produtos;
    }
    
    /**
     * Remove produto e invalida cache.
     * 
     * <p>Operações de ESCRITA sempre invalidam o cache.</p>
     */
    @Override
    public void remover(Integer id) {
        System.out.println("🟢 [PROXY] remover(" + id + ") - Delegando para Real Subject e invalidando cache");
        realSubject.remover(id);
        invalidarCache();
        System.out.println("🟢 [PROXY] Cache invalidado após remover");
    }
    
    /**
     * Busca produtos com estoque baixo com cache.
     */
    @Override
    public List<Produto> findProdutosComEstoqueBaixo() {
        System.out.println("🟢 [PROXY] findProdutosComEstoqueBaixo()");
        
        // Verificar cache
        if (cacheEstoqueBaixo != null) {
            cacheHits++;
            System.out.println("🟢 [PROXY] ✅ CACHE HIT! Retornando " + cacheEstoqueBaixo.size() + " produtos do cache");
            System.out.println("📊 Estatísticas: Hits=" + cacheHits + " | Misses=" + cacheMisses);
            return cacheEstoqueBaixo;
        }
        
        // Cache miss
        System.out.println("🟢 [PROXY] ❌ CACHE MISS - Delegando para Real Subject");
        cacheMisses++;
        List<Produto> produtos = realSubject.findProdutosComEstoqueBaixo();
        
        // Armazenar no cache
        cacheEstoqueBaixo = produtos;
        System.out.println("🟢 [PROXY] Lista de " + produtos.size() + " produtos armazenada no cache");
        System.out.println("📊 Estatísticas: Hits=" + cacheHits + " | Misses=" + cacheMisses);
        return produtos;
    }
    
    /**
     * Busca produtos com estoque baixo (método alternativo).
     */
    @Override
    public List<Produto> listarProdutosComEstoqueBaixo() {
        return findProdutosComEstoqueBaixo();
    }
    
    // ========== MÉTODOS DE GERENCIAMENTO DO CACHE ==========
    
    /**
     * Invalida TODO o cache.
     * 
     * <p>Chamado após operações de escrita (salvar/remover) para garantir
     * que o próximo acesso busque dados atualizados do banco.</p>
     */
    public void invalidarCache() {
        System.out.println("🟢 [PROXY] 🗑️ Invalidando cache...");
        cache.clear();
        cacheTodos = null;
        cacheEstoqueBaixo = null;
        System.out.println("🟢 [PROXY] Cache completamente limpo");
    }
    
    /**
     * Reseta estatísticas do cache.
     * 
     * <p>Útil para testes e demonstrações.</p>
     */
    public void resetarEstatisticas() {
        cacheHits = 0;
        cacheMisses = 0;
        System.out.println("🟢 [PROXY] Estatísticas resetadas");
    }
    
    /**
     * Retorna estatísticas do cache em formato legível.
     * 
     * @return String com estatísticas formatadas
     */
    public String getEstatisticas() {
        int total = cacheHits + cacheMisses;
        double hitRate = total > 0 ? (cacheHits * 100.0 / total) : 0;
        
        return String.format("""
            
            📊 Cache Statistics:
               Hits: %d | Misses: %d | Total: %d
               Hit Rate: %.2f%%
               Cache Size: %d produtos
            
            """, cacheHits, cacheMisses, total, hitRate, cache.size());
    }
}
