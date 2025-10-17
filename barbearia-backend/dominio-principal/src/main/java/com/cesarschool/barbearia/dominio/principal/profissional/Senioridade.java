package com.cesarschool.barbearia.dominio.principal.profissional;

public enum Senioridade {
    JUNIOR("Júnior", 1),
    PLENO("Pleno", 2),
    SENIOR("Sênior", 3);

    private final String descricao;
    private final int nivel;

    Senioridade(String descricao, int nivel) {
        this.descricao = descricao;
        this.nivel = nivel;
    }

    public String getDescricao() { return descricao; }
    public int getNivel() { return nivel; }
}