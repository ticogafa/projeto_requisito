package com.cesarschool.barbearia_backend.marketing.dto;

// import jakarta.validation.constraints.Email;
// import jakarta.validation.constraints.NotBlank;
// import jakarta.validation.constraints.NotNull;

import lombok.Data;

public class ClienteDTOs {

    private ClienteDTOs() {
        // Utility class
    }

    @Data
    public static class CriarClienteRequest {
        private String nome;
        private String email;
        private String cpf;
        private String telefone;
    }

    @Data
    public static class AtualizarClienteRequest {
        private Integer id;
        private String nome;        
        private String email;
        private String telefone;
    }

    @Data
    public static class ClienteResponse {
        private Integer id;
        private String nome;
        private String email;
        private String cpf;
        private String telefone;
        private int pontos;
    }
}
