package com.cesarschool.barbearia.infraestrutura.proxy;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.cesarschool.barbearia.dominio.principal.produto.Produto;
import com.cesarschool.barbearia.dominio.principal.produto.ProdutoRepositorio;

/**
 * Virtual Proxy para ProdutoRepositorio - Padrão VIRTUAL PROXY com LAZY LOADING.
 * 
 * <p>Este Proxy implementa o padrão Virtual Proxy, adiando o carregamento de dados
 * do banco de dados até que sejam realmente necessários (lazy loading). Isso economiza
 * recursos ao evitar operações caras de I/O até o momento exato em que os dados são acessados.</p>
 * 
 * <p><b>Padrão Virtual Proxy:</b> Atua como um substituto (placeholder) para o Real Subject,
 * adiando operações custosas até que sejam absolutamente necessárias.</p>
 * 
 * <p><b>Características do Virtual Proxy:</b></p>
 * <ul>
 *   <li>✅ Mesma interface do Real Subject (ProdutoRepositorio)</li>
 *   <li>✅ Usa composição (HAS-A), não herança (IS-A)</li>
 *   <li>✅ <b>Lazy Initialization:</b> Carrega dados SOB DEMANDA</li>
 *   <li>✅ <b>Economia de Recursos:</b> Evita carregar dados desnecessários</li>
 *   <li>✅ <b>Transparente:</b> Cliente não sabe que está usando um proxy</li>
 *   <li>✅ Thread-safe usando ConcurrentHashMap</li>
 *   <li>✅ Rastreia operações de lazy loading</li>
 * </ul>
 * 
 * <p><b>Como funciona o Lazy Loading:</b></p>
 * <ol>
 *   <li>Primeira chamada: Proxy verifica se dados estão carregados</li>
 *   <li>Se NÃO carregados: Delega para Real Subject (LAZY LOAD)</li>
 *   <li>Se JÁ carregados: Retorna dados já carregados (REUSO)</li>
 *   <li>Operações de escrita: Invalida dados carregados</li>
 * </ol>
 * 
 * <p><b>Benefícios:</b></p>
 * <ul>
 *   <li>⚡ Inicialização rápida (não carrega tudo de uma vez)</li>
 *   <li>💾 Economia de memória (só carrega o que é usado)</li>
 *   <li>🔄 Transparente para o cliente</li>
 *   <li>📊 Monitoramento de lazy loads vs reuso</li>
 * </ul>
 * 
 * @author Tiago
 * @version 7.0 - Virtual Proxy com Lazy Loading (Híbrido: Proxy instanciado por Spring, lógica pura)
 */
@Component
@Primary
public class ProdutoRepositorioVirtualProxy implements ProdutoRepositorio {
    
    // ========== COMPOSIÇÃO (não herança!) ==========
    /**
     * Real Subject: Repositório real que acessa o banco de dados.
     * O Proxy DELEGA as chamadas para o Real Subject APENAS quando necessário (lazy).
     */
    private final ProdutoRepositorio realSubject;
    
    // ========== LAZY LOADING - DADOS CARREGADOS SOB DEMANDA ==========
    /**
     * Produtos carregados sob demanda (lazy).
     * Map<ID, Produto> - só carrega do BD quando realmente acessado.
     * Thread-safe usando ConcurrentHashMap.
     */
    private final Map<Integer, Produto> produtosCarregados = new ConcurrentHashMap<>();
    
    /**
     * Lista completa de produtos (lazy loading).
     * null = NÃO CARREGADO AINDA (lazy)
     * não-null = JÁ CARREGADO
     */
    private List<Produto> listaTodosCarregada = null;
    
    /**
     * Lista de produtos com estoque baixo (lazy loading).
     * null = NÃO CARREGADO AINDA (lazy)
     * não-null = JÁ CARREGADO
     */
    private List<Produto> listaEstoqueBaixoCarregada = null;
    
    // ========== ESTATÍSTICAS DE LAZY LOADING ==========
    /**
     * Contador de vezes que dados JÁ estavam carregados (reuso).
     */
    private int reusoContador = 0;
    
    /**
     * Contador de vezes que foi necessário LAZY LOAD (buscar do BD).
     */
    private int lazyLoadContador = 0;
    
    // ========== CONSTRUTOR COM SPRING DI (apenas para injeção) ==========
    /**
     * Construtor com injeção de dependência do Real Subject.
     * Spring gerencia APENAS a injeção, a lógica do Proxy é pura (sem dependência de framework).
     * 
     * @param realSubject Real Subject injetado pelo Spring
     */
    @Autowired
    public ProdutoRepositorioVirtualProxy(@Qualifier("produtoRepositorioJpa") ProdutoRepositorio realSubject) {
        this.realSubject = realSubject;
        System.out.println("🟣 [VIRTUAL PROXY] Virtual Proxy inicializado - LAZY LOADING ativado");
        System.out.println("🟣 [VIRTUAL PROXY] Dados serão carregados SOB DEMANDA (quando necessário)");
        System.out.println("🟣 [VIRTUAL PROXY] Abordagem: Injeção via Spring, lógica pura (independente de framework)");
    }
    
    // ========== MÉTODOS DA INTERFACE PRODUTOREPOSITORIO ==========
    
    /**
     * Salva produto e invalida APENAS os dados relacionados.
     * 
     * <p>Invalidação SELETIVA: só remove o produto modificado e listas,
     * mantendo outros produtos lazy-loaded para melhor reuso.</p>
     */
    @Override
    public Produto salvar(Produto produto) {
        System.out.println("🟣 [VIRTUAL PROXY] salvar() - Delegando para Real Subject");
        Produto salvo = realSubject.salvar(produto);
        
        // Invalidação SELETIVA: apenas o produto modificado e listas
        if (salvo != null && salvo.getId() != null) {
            produtosCarregados.remove(salvo.getId());
            System.out.println("🟣 [VIRTUAL PROXY] Produto ID " + salvo.getId() + " invalidado (foi modificado)");
        }
        
        // Invalida listas (podem estar desatualizadas)
        listaTodosCarregada = null;
        listaEstoqueBaixoCarregada = null;
        System.out.println("🟣 [VIRTUAL PROXY] Listas invalidadas (outros produtos preservados)");
        
        // Recarrega o produto recém-salvo (otimização)
        if (salvo != null && salvo.getId() != null) {
            produtosCarregados.put(salvo.getId(), salvo);
            System.out.println("🟣 [VIRTUAL PROXY] Produto recém-salvo carregado automaticamente");
        }
        
        return salvo;
    }
    
    /**
     * Busca produto por ID com LAZY LOADING.
     * 
     * <p><b>Lógica do Virtual Proxy (Lazy Loading):</b></p>
     * <ol>
     *   <li>Verifica se produto JÁ foi carregado anteriormente</li>
     *   <li>Se JÁ carregado: retorna imediatamente (REUSO) ✅</li>
     *   <li>Se NÃO carregado: delega para Real Subject (LAZY LOAD) 📥</li>
     *   <li>Armazena resultado para futuras consultas</li>
     * </ol>
     */
    @Override
    public Produto buscarPorId(Integer id) {
        System.out.println("🟣 [VIRTUAL PROXY] buscarPorId(" + id + ")");
        
        // PASSO 1: Verificar se JÁ foi carregado (lazy loading já aconteceu?)
        if (produtosCarregados.containsKey(id)) {
            reusoContador++;
            Produto produto = produtosCarregados.get(id);
            System.out.println("🟣 [VIRTUAL PROXY] ✅ REUSO! Produto '" + produto.getNome() + "' já estava carregado");
            System.out.println("📊 Estatísticas: Reuso=" + reusoContador + " | Lazy Loads=" + lazyLoadContador);
            return produto;
        }
        
        // PASSO 2: NÃO carregado - fazer LAZY LOAD do banco de dados
        lazyLoadContador++;
        System.out.println("🟣 [VIRTUAL PROXY] 📥 LAZY LOAD - Carregando do banco de dados...");
        Produto produto = realSubject.buscarPorId(id);
        
        // PASSO 3: Armazenar para futuras consultas (evitar lazy load duplo)
        if (produto != null) {
            produtosCarregados.put(id, produto);
            System.out.println("🟣 [VIRTUAL PROXY] Produto '" + produto.getNome() + "' carregado e armazenado");
        }
        
        System.out.println("📊 Estatísticas: Reuso=" + reusoContador + " | Lazy Loads=" + lazyLoadContador);
        return produto;
    }
    
    /**
     * Lista todos os produtos com LAZY LOADING.
     * 
     * <p>Só carrega do banco na PRIMEIRA vez que for chamado.</p>
     */
    @Override
    public List<Produto> listarTodos() {
        System.out.println("🟣 [VIRTUAL PROXY] listarTodos()");
        
        // Verificar se JÁ carregou anteriormente
        if (listaTodosCarregada != null) {
            reusoContador++;
            System.out.println("🟣 [VIRTUAL PROXY] ✅ REUSO! Lista com " + listaTodosCarregada.size() + " produtos já estava carregada");
            System.out.println("📊 Estatísticas: Reuso=" + reusoContador + " | Lazy Loads=" + lazyLoadContador);
            return listaTodosCarregada;
        }
        
        // NÃO carregou ainda - fazer LAZY LOAD
        lazyLoadContador++;
        System.out.println("🟣 [VIRTUAL PROXY] 📥 LAZY LOAD - Carregando lista completa do banco de dados...");
        listaTodosCarregada = realSubject.listarTodos();
        
        // Também armazena produtos individuais
        listaTodosCarregada.forEach(p -> produtosCarregados.put(p.getId(), p));
        
        System.out.println("🟣 [VIRTUAL PROXY] Lista de " + listaTodosCarregada.size() + " produtos carregada");
        System.out.println("📊 Estatísticas: Reuso=" + reusoContador + " | Lazy Loads=" + lazyLoadContador);
        return listaTodosCarregada;
    }
    
    /**
     * Remove produto e invalida APENAS os dados relacionados.
     */
    @Override
    public void remover(Integer id) {
        System.out.println("🟣 [VIRTUAL PROXY] remover(" + id + ") - Delegando para Real Subject");
        realSubject.remover(id);
        
        // Invalidação SELETIVA: apenas o produto removido e listas
        produtosCarregados.remove(id);
        listaTodosCarregada = null;
        listaEstoqueBaixoCarregada = null;
        
        System.out.println("🟣 [VIRTUAL PROXY] Produto ID " + id + " e listas invalidados (outros produtos preservados)");
    }
    
    /**
     * Busca produtos com estoque baixo com LAZY LOADING.
     */
    @Override
    public List<Produto> findProdutosComEstoqueBaixo() {
        System.out.println("🟣 [VIRTUAL PROXY] findProdutosComEstoqueBaixo()");
        
        // Verificar se JÁ carregou
        if (listaEstoqueBaixoCarregada != null) {
            reusoContador++;
            System.out.println("🟣 [VIRTUAL PROXY] ✅ REUSO! Lista de estoque baixo já estava carregada");
            System.out.println("📊 Estatísticas: Reuso=" + reusoContador + " | Lazy Loads=" + lazyLoadContador);
            return listaEstoqueBaixoCarregada;
        }
        
        // NÃO carregou - fazer LAZY LOAD
        lazyLoadContador++;
        System.out.println("🟣 [VIRTUAL PROXY] 📥 LAZY LOAD - Carregando produtos com estoque baixo...");
        listaEstoqueBaixoCarregada = realSubject.findProdutosComEstoqueBaixo();
        
        System.out.println("🟣 [VIRTUAL PROXY] " + listaEstoqueBaixoCarregada.size() + " produtos com estoque baixo carregados");
        System.out.println("📊 Estatísticas: Reuso=" + reusoContador + " | Lazy Loads=" + lazyLoadContador);
        return listaEstoqueBaixoCarregada;
    }
    
    /**
     * Lista produtos com estoque baixo (atualizado).
     */
    @Override
    public List<Produto> listarProdutosComEstoqueBaixo() {
        return findProdutosComEstoqueBaixo();
    }
    
    // ========== MÉTODOS DE GERENCIAMENTO ==========
    
    /**
     * Invalida TODOS os dados lazy-loaded.
     * 
     * <p>Chamado após operações de escrita (salvar/remover) para garantir
     * que o próximo acesso busque dados atualizados do banco.</p>
     */
    public void invalidarDadosCarregados() {
        System.out.println("🟣 [VIRTUAL PROXY] 🗑️ Invalidando dados lazy-loaded...");
        produtosCarregados.clear();
        listaTodosCarregada = null;
        listaEstoqueBaixoCarregada = null;
        System.out.println("🟣 [VIRTUAL PROXY] Todos os dados lazy-loaded foram limpos");
    }
    
    /**
     * Reseta estatísticas de lazy loading.
     */
    public void resetarEstatisticas() {
        reusoContador = 0;
        lazyLoadContador = 0;
        System.out.println("🟣 [VIRTUAL PROXY] Estatísticas resetadas");
    }
    
    /**
     * Retorna estatísticas do lazy loading em formato legível.
     */
    public String getEstatisticas() {
        int total = reusoContador + lazyLoadContador;
        double reusoRate = total > 0 ? (reusoContador * 100.0 / total) : 0;
        
        return String.format("""
            
            📊 ========== ESTATÍSTICAS DO VIRTUAL PROXY ==========
            
            🎯 Performance Lazy Loading:
               ✅ Reuso (Já Carregado):  %d
               📥 Lazy Loads (Do BD):    %d
               📈 Taxa de Reuso:         %.2f%%
            
            💾 Dados Carregados:
               🔢 Produtos Individuais:  %d
               📋 Lista Completa:        %s
               ⚠️  Estoque Baixo:        %s
            
            🟣 Padrão: VIRTUAL PROXY com LAZY LOADING
            """,
            reusoContador,
            lazyLoadContador,
            reusoRate,
            produtosCarregados.size(),
            listaTodosCarregada != null ? listaTodosCarregada.size() + " produtos" : "Não carregada",
            listaEstoqueBaixoCarregada != null ? listaEstoqueBaixoCarregada.size() + " produtos" : "Não carregada"
        );
    }
    
    /**
     * Retorna métricas em formato Map para API REST.
     */
    public Map<String, Object> getMetricas() {
        int total = reusoContador + lazyLoadContador;
        double reusoRate = total > 0 ? (reusoContador * 100.0 / total) : 0;
        
        return Map.of(
            "reuso", reusoContador,
            "lazyLoads", lazyLoadContador,
            "total", total,
            "taxaReuso", reusoRate,
            "produtosCarregados", produtosCarregados.size(),
            "listaTodosCarregada", listaTodosCarregada != null,
            "listaEstoqueBaixoCarregada", listaEstoqueBaixoCarregada != null
        );
    }
}
