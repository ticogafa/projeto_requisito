package com.barbearia.agendamento.model;

import java.time.LocalDateTime;
import java.util.UUID;

import com.barbearia.common.enums.StatusAgendamento;
import com.barbearia.marketing.model.Cliente;
import com.barbearia.profissionais.model.Profissional;
import com.barbearia.profissionais.model.ServicoOferecido;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@Table(name = "agendamento")
public class Agendamento {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NonNull
    @Column(nullable = false)
    private LocalDateTime dataHora;

    @NonNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusAgendamento status;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;
    
    @NonNull
    @ManyToOne
    @JoinColumn(name = "profissional_id", nullable = false)
    private Profissional profissional;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "servico_id", nullable = false)
    private ServicoOferecido servico;

    @Column(nullable = true)
    private String observacoes;
    //ainda nao sei ao certo o que fazer aqui
    public void setTokenConfirmacao(String string) {
        
        throw new UnsupportedOperationException("ainda nao implementei 'setTokenConfirmacao'");
    }
    
}
    
