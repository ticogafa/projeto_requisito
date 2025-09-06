package com.barbearia.marketing.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import com.barbearia.common.enums.StatusVoucher;

public class Voucher {
    UUID id; // PK - also the code
    Cliente cliente; // FK Cliente(id), NOT NULL
    BigDecimal valorDesconto; // NOT NULL
    StatusVoucher status; // DEFAULT GERADO
    LocalDateTime expiraEm; // opcional (se não tiver, não expira)
  }