package com.cesarschool.barbearia.apresentacao;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Controlador REST para verificação de saúde da aplicação.
 * 
 * @author Sistema de Barbearia
 * @version 1.0
 */
@RestController
@RequestMapping("/api")
public class HealthController {
    
    /**
     * Endpoint de verificação de saúde.
     * 
     * @return Informações sobre o status da aplicação
     */
    @GetMapping("/health")
    public Map<String, Object> health() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("timestamp", LocalDateTime.now());
        response.put("application", "Barbearia Backend");
        response.put("version", "1.0.0-SNAPSHOT");
        return response;
    }
    
    /**
     * Endpoint raiz da API.
     * 
     * @return Mensagem de boas-vindas
     */
    @GetMapping
    public Map<String, String> welcome() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Bem-vindo ao Barbearia Backend!");
        response.put("documentation", "/api/health");
        response.put("h2Console", "/h2-console");
        return response;
    }
}
