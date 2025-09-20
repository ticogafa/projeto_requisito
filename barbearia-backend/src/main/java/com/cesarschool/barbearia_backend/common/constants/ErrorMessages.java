package com.cesarschool.barbearia_backend.common.constants;

public enum ErrorMessages {
    ENTIDADE_NAO_ENCONTRADA("%s não encontrado"),
    CAMPO_NAO_PODE_SER_ALTERADO("%s não pode ser alterado"),
    HORARIO_NAO_ENCONTRADO("Horário de trabalho não encontrado"),
    DIA_SEMANA_JA_CADASTRADO("%s já cadastrado(a) para o profissional %s"),
    HORARIO_NAO_PERTENCE_A_PROFISSIONAL("Horário não pertence ao profissional informado");

    private final String template;

    ErrorMessages(String template) {
        this.template = template;
    }

    public String format(Object... args) {
        return String.format(template, args);
    }

    @Override
    public String toString() {
        return template;
    }
}
