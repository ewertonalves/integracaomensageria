package com.integracao.integracaomensageria.model;

public class Distritos {

	private Long id;
	private String nome;
	private Municipio municipio;

	public Distritos() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Municipio getMunicipio() {
		return municipio;
	}

	public void setMunicipio(Municipio municipio) {
		this.municipio = municipio;
	}

	public static class Municipio {

		private Long id;
		private String nome;

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public String getNome() {
			return nome;
		}

		public void setNome(String nome) {
			this.nome = nome;
		}

	}

	@Override
	public String toString() {
		return "Distritos [id=" + id + ", nome=" + nome + ", municipio=" + municipio + ", getId()=" + getId()
				+ ", getNome()=" + getNome() + ", getMunicipio()=" + getMunicipio() + ", getClass()=" + getClass()
				+ ", hashCode()=" + hashCode() + ", toString()=" + super.toString() + "]";
	}
}
