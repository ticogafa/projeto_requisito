package com.cesarschool.barbearia.infraestrutura.persistencia.jpa;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "execucao_atendimento")
public class ExecucaoAtendimentoJpa {

    @Id
    @Column(length = 36)
    private String id;

    @Column(name = "profissional_id", nullable = false, length = 36)
    private String profissionalId;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal valor;

    @Column(nullable = false)
    private LocalDateTime inicio;

    @Column
    private LocalDateTime fim;
}