package com.cesarschool.barbearia_backend.profissionais.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

public final class ProfissionalDTOs {

    private ProfissionalDTOs() {
        // Utility class
    }

    @Data
    public static class CriarProfissionalRequest {
        @NotBlank(message = "Nome é obrigatório")
        @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
        private String nome;

        @NotBlank(message = "Email é obrigatório")
        private String email;

        @NotBlank(message = "CPF é obrigatório")
        private String cpf;

        @NotBlank(message = "Telefone é obrigatório")
        private String telefone;
    }

    @Data
    public static class AtualizarProfissionalRequest {
        
        @NotNull(message = "ID é obrigatório")
        private Integer id;

        @NotBlank(message = "Nome é obrigatório")
        @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
        private String nome;

        @NotBlank(message = "Email é obrigatório")
        private String email;

        @NotBlank(message = "CPF é obrigatório")
        private String cpf;

        @NotBlank(message = "Telefone é obrigatório")
        private String telefone;
    }

    @Data
    public static class ProfissionalResponse {
        private Integer id;
        private String nome;
        private String email;
        private String cpf;
        private String telefone;
    }

}
