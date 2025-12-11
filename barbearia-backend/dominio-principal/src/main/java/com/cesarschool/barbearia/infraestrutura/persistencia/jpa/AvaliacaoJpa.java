package com.cesarschool.barbearia.infraestrutura.persistencia.jpa;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "avaliacao")
public class AvaliacaoJpa {

    @Id
    @Column(length = 36)
    private String id;

    @Column(name = "profissional_id", nullable = false)
    private Integer profissionalId;

    @Column(nullable = false)
    private int nota; // Salvamos o valor inteiro da Nota

    @Column(name = "data_avaliacao", nullable = false)
    private LocalDateTime data;
}