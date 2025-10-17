package com.cesarschool.barbearia.dominio.principal.profissional;

import java.time.LocalTime;

public final class Agenda {
    private final int jornadaPadraoMinutos = 480;
    private LocalTime inicioJornada;
    private LocalTime fimJornada;

    public Agenda() {
        this.inicioJornada = LocalTime.of(9, 0);
        this.fimJornada = LocalTime.of(17, 0);
    }
    
    public int calcularJornadaHoras() {
        return jornadaPadraoMinutos / 60; 
    }

    public boolean estaVazia() {
        return false; 
    }
}