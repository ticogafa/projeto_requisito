package com.cesarschool.barbearia_backend.profissionais.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

public final class ServicoDTOs {

    private ServicoDTOs() {
        // Utility class
    }

    @Data
    public static class CriarServicoRequest {
        @NotBlank(message = "Nome é obrigatório")
        @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
        private String nome;

        @NotNull(message = "Preço é obrigatório")
        @DecimalMin(value = "0.01", message = "Preço deve ser maior que zero")
        private BigDecimal preco;

        @NotNull(message = "Duração é obrigatória")
        @Min(value = 1, message = "Duração deve ser maior que zero")
        private Integer duracaoMinutos;

        @NotBlank(message = "Descrição é obrigatória")
        @Size(min = 5, max = 255, message = "Descrição deve ter entre 5 e 255 caracteres")
        private String descricao;
    }

    @Data
    public static class AtualizarServicoRequest {
        @NotNull(message = "ID é obrigatório")
        private Integer id;

        @NotBlank(message = "Nome é obrigatório")
        @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
        private String nome;

        @NotNull(message = "Preço é obrigatório")
        @DecimalMin(value = "0.01", message = "Preço deve ser maior que zero")
        private BigDecimal preco;

        @NotNull(message = "Duração é obrigatória")
        @Min(value = 1, message = "Duração deve ser maior que zero")
        private Integer duracaoMinutos;

        @NotBlank(message = "Descrição é obrigatória")
        @Size(min = 5, max = 255, message = "Descrição deve ter entre 5 e 255 caracteres")
        private String descricao;
    }

    @Data
    public static class ServicoResponse {
        private Integer id;
        private String nome;
        private BigDecimal preco;
        private Integer duracaoMinutos;
        private String descricao;
    }
}
