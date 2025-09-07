package com.barbearia.marketing.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import com.barbearia.common.enums.StatusVoucher;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;

@Entity
@Data
@AllArgsConstructor
@Table(name = "voucher")
public class Voucher {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    UUID id; // também é o valor usado pra resgatar

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    Cliente cliente;

    @Column(nullable = false)
    BigDecimal valorDesconto;
    
    @Column(nullable = false)
    StatusVoucher status = StatusVoucher.GERADO; //opcional
    
    @Column(nullable = true)
    LocalDateTime expiraEm; // opcional (se não tiver, não expira)

    public Voucher(Cliente cliente, BigDecimal valorDesconto){
      this.cliente=cliente;
      this.valorDesconto=valorDesconto;
    }
}