package com.cesarschool.barbearia.dominio.principal.populador;

import java.math.BigDecimal;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.cesarschool.barbearia.dominio.principal.profissional.Profissional;
import com.cesarschool.barbearia.dominio.principal.profissional.ProfissionalRepositorio;
import com.cesarschool.barbearia.dominio.principal.servico.ServicoOferecido;
import com.cesarschool.barbearia.dominio.principal.servico.ServicoOferecidoRepositorio;

@Component
@Order(2)
public class PopuladorServicoOferecido implements CommandLineRunner {

    private final ServicoOferecidoRepositorio servicoOferecidoRepositorio;
    private final ProfissionalRepositorio profissionalRepositorio;

    public PopuladorServicoOferecido(ServicoOferecidoRepositorio servicoOferecidoRepositorio, ProfissionalRepositorio profissionalRepositorio) {
        this.servicoOferecidoRepositorio = servicoOferecidoRepositorio;
        this.profissionalRepositorio = profissionalRepositorio;
    }

    @Override
    public void run(String... args) throws Exception {
        if (servicoOferecidoRepositorio.listarTodos().isEmpty()) {
            Profissional profissional = profissionalRepositorio.listarTodos().get(0);

            ServicoOferecido servico1 = new ServicoOferecido(
                profissional.getId(),
                "Corte de Cabelo",
                new BigDecimal("50.00"),
                "Corte de cabelo masculino com máquina e tesoura.",
                30
            );

            ServicoOferecido servico2 = new ServicoOferecido(
                profissional.getId(),
                "Barba",
                new BigDecimal("35.00"),
                "Aparar e desenhar a barba com navalha.",
                20
            );

            servicoOferecidoRepositorio.salvar(servico1);
            servicoOferecidoRepositorio.salvar(servico2);

            System.out.println(">>> Serviços padrão criados.");
        }
    }
}
