package com.cesarschool.barbearia.apresentacao.proxy;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cesarschool.barbearia.infraestrutura.proxy.ProdutoRepositorioVirtualProxy;

/**
 * Controlador REST para expor métricas do Virtual Proxy em tempo real.
 * 
 * <p>Este controlador permite monitorar o comportamento do Virtual Proxy através de endpoints REST,
 * facilitando a observação do lazy loading, reuso de dados e performance do sistema.</p>
 * 
 * <p><b>Endpoints disponíveis:</b></p>
 * <ul>
 *   <li>GET /api/proxy/statistics - Retorna estatísticas em formato JSON</li>
 *   <li>GET /api/proxy/statistics/text - Retorna estatísticas em formato texto legível</li>
 *   <li>DELETE /api/proxy/cache - Limpa todos os dados lazy-loaded do cache</li>
 *   <li>DELETE /api/proxy/statistics - Reseta os contadores de estatísticas</li>
 * </ul>
 * 
 * @author Tiago Gurgel
 * @version 1.0
 */
@RestController
@RequestMapping("/api/proxy")
public class ProxyMetricasControlador {
    
    private final ProdutoRepositorioVirtualProxy virtualProxy;
    
    public ProxyMetricasControlador(ProdutoRepositorioVirtualProxy virtualProxy) {
        this.virtualProxy = virtualProxy;
    }
    
    /**
     * Retorna estatísticas do Virtual Proxy em formato JSON.
     * 
     * <p><b>Exemplo de resposta:</b></p>
     * <pre>
     * {
     *   "reuso": 12,
     *   "lazyLoads": 5,
     *   "total": 17,
     *   "taxaReuso": 70.59,
     *   "produtosCarregados": 3,
     *   "listaTodosCarregada": true,
     *   "listaEstoqueBaixoCarregada": false
     * }
     * </pre>
     * 
     * @return Mapa com métricas do proxy
     */
    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getStatistics() {
        Map<String, Object> metricas = virtualProxy.getMetricas();
        return ResponseEntity.ok(metricas);
    }
    
    /**
     * Retorna estatísticas do Virtual Proxy em formato texto legível.
     * 
     * <p>Útil para visualização direta no navegador ou logs.</p>
     * 
     * @return String com estatísticas formatadas
     */
    @GetMapping("/statistics/text")
    public ResponseEntity<String> getStatisticsText() {
        String estatisticas = virtualProxy.getEstatisticas();
        return ResponseEntity.ok(estatisticas);
    }
    
    /**
     * Limpa todos os dados lazy-loaded do cache do Virtual Proxy.
     * 
     * <p>Após esta operação, todas as próximas consultas farão lazy load novamente do banco de dados.</p>
     * 
     * @return Mensagem de confirmação
     */
    @DeleteMapping("/cache")
    public ResponseEntity<Map<String, String>> clearCache() {
        virtualProxy.invalidarDadosCarregados();
        return ResponseEntity.ok(Map.of(
            "message", "Cache do Virtual Proxy limpo com sucesso",
            "status", "success"
        ));
    }
    
    /**
     * Reseta os contadores de estatísticas do Virtual Proxy.
     * 
     * <p>Zera os contadores de lazy loads e reuso, mas mantém os dados carregados no cache.</p>
     * 
     * @return Mensagem de confirmação
     */
    @DeleteMapping("/statistics")
    public ResponseEntity<Map<String, String>> resetStatistics() {
        virtualProxy.resetarEstatisticas();
        return ResponseEntity.ok(Map.of(
            "message", "Estatísticas do Virtual Proxy resetadas com sucesso",
            "status", "success"
        ));
    }
    
    /**
     * Retorna informações sobre o padrão Virtual Proxy implementado.
     * 
     * @return Informações sobre o padrão
     */
    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> getProxyInfo() {
        return ResponseEntity.ok(Map.of(
            "pattern", "Virtual Proxy",
            "type", "Structural Pattern (GoF)",
            "purpose", "Lazy Loading - Adiamento de carregamento de dados até que sejam necessários",
            "benefits", Map.of(
                "performance", "Inicialização rápida e economia de recursos",
                "memory", "Carrega apenas dados sob demanda",
                "io", "Reduz operações de I/O no banco de dados",
                "transparency", "Cliente não precisa saber da existência do proxy"
            ),
            "endpoints", Map.of(
                "statistics", "GET /api/proxy/statistics - Métricas em JSON",
                "statisticsText", "GET /api/proxy/statistics/text - Métricas em texto",
                "clearCache", "DELETE /api/proxy/cache - Limpa cache lazy-loaded",
                "resetStatistics", "DELETE /api/proxy/statistics - Reseta contadores",
                "info", "GET /api/proxy/info - Informações sobre o padrão"
            )
        ));
    }
}
