package dev.sauloaraujo.sgb.dominio.administracao.socio;

import static org.apache.commons.lang3.Validate.isTrue;

import java.util.Objects;

public class SocioId {
	private final int id;

	public SocioId(int id) {
		isTrue(id > 0, "O id deve ser positivo");

		this.id = id;
	}

	public int getId() {
		return id;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj instanceof SocioId) {
			var socioId = (SocioId) obj;
			return id == socioId.id;
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public String toString() {
		return Integer.toString(id);
	}
}