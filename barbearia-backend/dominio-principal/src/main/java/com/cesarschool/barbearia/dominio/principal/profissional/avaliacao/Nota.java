package com.cesarschool.barbearia.dominio.principal.profissional.avaliacao;

import java.util.Objects;

public final class Nota {
    private final int value;

    public Nota(int value) {
        if (value < 1 || value > 5) {
            throw new IllegalArgumentException("Nota deve ser entre 1 e 5");
        }
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Nota nota = (Nota) o;
        return value == nota.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}