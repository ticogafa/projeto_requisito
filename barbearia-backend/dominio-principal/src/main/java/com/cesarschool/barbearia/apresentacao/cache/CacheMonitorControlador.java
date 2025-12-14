package com.cesarschool.barbearia.apresentacao.cache;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cesarschool.barbearia.infraestrutura.proxy.ProdutoRepositorioVirtualProxy;

@RestController
@RequestMapping("/api/cache")
@CrossOrigin(origins = "*")
public class CacheMonitorControlador {
    
    private final ProdutoRepositorioVirtualProxy virtualProxy;
    
    public CacheMonitorControlador(ProdutoRepositorioVirtualProxy virtualProxy) {
        this.virtualProxy = virtualProxy;
    }
    
    /**
     * Retorna métricas do cache em formato JSON.
     * 
     * <p>Útil para integração com dashboards e monitoramento automatizado.</p>
     * 
     * @return Mapa com métricas (hits, misses, hit rate, etc.)
     */
    @GetMapping("/metricas")
    public ResponseEntity<Map<String, Object>> getMetricas() {
        return ResponseEntity.ok(virtualProxy.getMetricas());
    }
    
    /**
     * Retorna estatísticas detalhadas do cache em formato texto.
     * 
     * <p>Retorna uma visualização formatada com emojis e estatísticas completas.</p>
     * 
     * @return Estatísticas formatadas
     */
    @GetMapping("/estatisticas")
    public ResponseEntity<String> getEstatisticas() {
        return ResponseEntity.ok(virtualProxy.getEstatisticas());
    }
    
    /**
     * Reseta os contadores de estatísticas do cache.
     * 
     * <p>Útil para testes e benchmarks. Não limpa o cache, apenas reseta contadores.</p>
     * 
     * @return Mensagem de confirmação
     */
    @PostMapping("/resetar")
    public ResponseEntity<String> resetarEstatisticas() {
        virtualProxy.resetarEstatisticas();
        return ResponseEntity.ok("✅ Estatísticas de lazy loading resetadas com sucesso!");
    }
    
    /**
     * Limpa completamente o cache.
     * 
     * <p>Remove todos os dados em cache. Próximas requisições buscarão do banco.</p>
     * 
     * @return Mensagem de confirmação
     */
    @PostMapping("/limpar")
    public ResponseEntity<String> limparCache() {
        virtualProxy.invalidarDadosCarregados();
        return ResponseEntity.ok("✅ Dados lazy-loaded limpos com sucesso!");
    }
}
