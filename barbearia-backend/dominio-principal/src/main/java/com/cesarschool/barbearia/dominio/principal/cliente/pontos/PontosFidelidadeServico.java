package com.cesarschool.barbearia.dominio.principal.cliente.pontos;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.stereotype.Service;

import com.cesarschool.barbearia.dominio.principal.cliente.Cliente;
import com.cesarschool.barbearia.dominio.principal.cliente.ClienteId;
import com.cesarschool.barbearia.dominio.principal.cliente.ClienteRepositorio;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PontosFidelidadeServico {

    private final ClienteRepositorio clienteRepositorio;

    /**
     * Credita pontos para o cliente com base no valor pago: 1 ponto por real, truncando casas decimais.
     */
    public Cliente creditar(ClienteId clienteId, BigDecimal valorPago) {
        if (clienteId == null) {
            throw new IllegalArgumentException("ClienteId não pode ser nulo para pontuação de fidelidade");
        }
        if (valorPago == null) {
            throw new IllegalArgumentException("Valor pago não pode ser nulo para pontuação de fidelidade");
        }

        Cliente cliente = clienteRepositorio.buscarPorId(clienteId.getValor());
        if (cliente == null) {
            throw new IllegalArgumentException("Cliente não encontrado para pontuação de fidelidade: " + clienteId.getValor());
        }

        int pontosGanhos = valorPago.setScale(0, RoundingMode.FLOOR).intValue();
        if (pontosGanhos < 0) {
            throw new IllegalArgumentException("Valor pago não pode gerar pontos negativos");
        }

        cliente.adicionarPontos(pontosGanhos);
        return clienteRepositorio.salvar(cliente);
    }
}
