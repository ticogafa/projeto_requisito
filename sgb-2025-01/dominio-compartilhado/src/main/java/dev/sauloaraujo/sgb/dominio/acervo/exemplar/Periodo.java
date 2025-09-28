package dev.sauloaraujo.sgb.dominio.acervo.exemplar;

import static org.apache.commons.lang3.Validate.isTrue;
import static org.apache.commons.lang3.Validate.notNull;

import java.time.LocalDate;

public class Periodo {
	private final LocalDate inicio;
	private final LocalDate fim;

	public Periodo(LocalDate inicio, LocalDate fim) {
		notNull(inicio, "O início não pode ser nulo");
		notNull(fim, "O fim não pode ser nulo");
		isTrue(inicio.compareTo(fim) <= 0, "O início não pode ser maior que o fim");

		this.inicio = inicio;
		this.fim = fim;
	}

	public LocalDate getInicio() {
		return inicio;
	}

	public LocalDate getFim() {
		return fim;
	}
}