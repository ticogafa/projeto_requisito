package com.cesarschool.barbearia.dominio.principal.voucher;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.cesarschool.barbearia.dominio.principal.cliente.ClienteId;

public final class Voucher {

    private Integer id; // também é o valor usado pra resgatar
    private ClienteId cliente;
    private BigDecimal valorDesconto;
    private StatusVoucher status = StatusVoucher.GERADO; // opcional
    private LocalDateTime expiraEm; // opcional (se não tiver, não expira)

    public Voucher(Integer id, ClienteId cliente, BigDecimal valorDesconto, StatusVoucher status, LocalDateTime expiraEm) {
        setId(id);
        setCliente(cliente);
        setValorDesconto(valorDesconto);
        setStatus(status);
        setExpiraEm(expiraEm);
    }

    public void setId(Integer id) {
        com.cesarschool.barbearia.dominio.compartilhado.utils.Validacoes.validarObjetoObrigatorio(id, "Id do Voucher");
        com.cesarschool.barbearia.dominio.compartilhado.utils.Validacoes.validarInteiroPositivo(id, "Id do Voucher");
        this.id = id;
    }

    public void setCliente(ClienteId cliente) {
        this.cliente = cliente;
    }

    public void setValorDesconto(BigDecimal valorDesconto) {
        com.cesarschool.barbearia.dominio.compartilhado.utils.Validacoes.validarObjetoObrigatorio(valorDesconto, "Valor Desconto");
        com.cesarschool.barbearia.dominio.compartilhado.utils.Validacoes.validarValorPositivo(valorDesconto, "Valor Desconto");
        this.valorDesconto = valorDesconto;
    }

    public void setStatus(StatusVoucher status) {
        if (status != null) {
            this.status = status;
        }
    }

    public void setExpiraEm(LocalDateTime expiraEm) {
        if (expiraEm != null) {
            com.cesarschool.barbearia.dominio.compartilhado.utils.Validacoes.validarDataNaoPassada(expiraEm, "Data de Expiração");
        }
        this.expiraEm = expiraEm;
    }

    public Integer getId() { return id; }
    public ClienteId getCliente() { return cliente; }
    public BigDecimal getValorDesconto() { return valorDesconto; }
    public StatusVoucher getStatus() { return status; }
    public LocalDateTime getExpiraEm() { return expiraEm; }
}