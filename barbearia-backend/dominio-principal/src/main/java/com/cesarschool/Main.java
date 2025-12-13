package com.cesarschool;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication(scanBasePackages = "com.cesarschool.barbearia")
@EnableJpaRepositories(basePackages = {"com.cesarschool.barbearia.infraestrutura.persistencia.jpa", "com.cesarschool.barbearia.dominio.principal"})
@EntityScan(basePackages = {"com.cesarschool.barbearia.infraestrutura.persistencia.jpa", "com.cesarschool.barbearia.dominio.principal"})
public class Main {
    
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
        System.out.println("========================================");
        System.out.println("  Barbearia Backend - Sistema Iniciado");
        System.out.println("  H2 Console: http://localhost:8080/h2-console");
        System.out.println("========================================");
    }
}