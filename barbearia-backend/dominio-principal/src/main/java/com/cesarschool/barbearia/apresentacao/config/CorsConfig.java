package com.cesarschool.barbearia.apresentacao.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        
        // Permite credenciais
        config.setAllowCredentials(true);
        
        // Permite requisições do frontend (Vite roda na porta 5173 por padrão)
        config.setAllowedOrigins(List.of("http://localhost:5173", "http://127.0.0.1:5173"));
        
        // Permite todos os headers
        config.setAllowedHeaders(Arrays.asList("*"));
        
        // Permite todos os métodos HTTP
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        
        // Expõe headers de resposta
        config.setExposedHeaders(Arrays.asList("Authorization", "Content-Type"));
        
        // Aplica configuração para todas as rotas
        source.registerCorsConfiguration("/**", config);
        
        return new CorsFilter(source);
    }
}
