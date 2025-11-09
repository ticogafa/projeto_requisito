package com.cesarschool.barbearia.infraestrutura.persistencia.jpa;

import static jakarta.persistence.GenerationType.*;

import java.time.LocalTime;

import com.cesarschool.barbearia.dominio.principal.profissional.Senioridade;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Entity
@Table(name = "PROFISSIONAL")
public final class ProfissionalJpa {
    
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "ID")
    @Id
    private Integer id;
    
    @Column(name = "NOME", nullable = false, length = 100)
    private String nome;

    @Column(name = "EMAIL", nullable = false, length = 100)
    private String email;

    @Column(name = "CPF", nullable = false, length = 11)
    private String cpf;

    @Column(name = "TELEFONE", nullable = false, length = 15)
    private String telefone;
    
    @Column(name = "INICIO_JORNADA", nullable = false)
    @Builder.Default
    private LocalTime inicioJornada = LocalTime.of(9, 0);

    @Column(name = "FIM_JORNADA", nullable = false)
    @Builder.Default
    private LocalTime fimJornada = LocalTime.of(17, 0);
  
    @Column(name = "SENIORIDADE", nullable = false)
    private Senioridade senioridade; 

    @Column(name = "ATIVO", nullable = false)
    private boolean ativo; 

    @Column(name = "MOTIVO_INATIVIDADE", length = 255)
    private String motivoInatividade; 
}
