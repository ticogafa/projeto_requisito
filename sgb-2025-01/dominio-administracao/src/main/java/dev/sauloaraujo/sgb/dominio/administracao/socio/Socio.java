package dev.sauloaraujo.sgb.dominio.administracao.socio;

import static org.apache.commons.lang3.Validate.notNull;

public class Socio {
	private final SocioId id;

	private String nome;
	private Email emailContato;

	public Socio(String nome, Email emailContato) {
		id = null;

		setNome(nome);
		setEmailContato(emailContato);
	}

	public Socio(SocioId id, String nome, Email emailContato) {
		notNull(id, "O id não pode ser nulo");
		this.id = id;
	}

	public SocioId getId() {
		return id;
	}

	private void setNome(String nome) {
		notNull(nome, "O nome não pode ser nulo");
		notNull(nome, "O nome não pode estar em branco");

		this.nome = nome;
	}

	public String getNome() {
		return nome;
	}

	private void setEmailContato(Email emailContato) {
		notNull(nome, "O email de contato não pode ser nulo");

		this.emailContato = emailContato;
	}

	public Email getEmailContato() {
		return emailContato;
	}

	@Override
	public String toString() {
		return nome;
	}
}