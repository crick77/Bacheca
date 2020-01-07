package it.dipvvf.abr.app.bacheca.model;

import java.sql.Date;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Elenco {
	private int id;
	private String tipo;
	private int numero;
	private Date dataPubblicazione;
	private String titolo;
	private String ufficio;
	private String proprietario;
	
	public Elenco() {
		super();
	}
	
	public Elenco(int id, String tipo, int numero, Date dataPubblicazione, String titolo, String ufficio,
			String proprietario) {
		super();
		this.id = id;
		this.tipo = tipo;
		this.numero = numero;
		this.dataPubblicazione = dataPubblicazione;
		this.titolo = titolo;
		this.ufficio = ufficio;
		this.proprietario = proprietario;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public int getNumero() {
		return numero;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}

	public Date getDataPubblicazione() {
		return dataPubblicazione;
	}

	public void setDataPubblicazione(Date dataPubblicazione) {
		this.dataPubblicazione = dataPubblicazione;
	}

	public String getTitolo() {
		return titolo;
	}

	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}

	public String getUfficio() {
		return ufficio;
	}

	public void setUfficio(String ufficio) {
		this.ufficio = ufficio;
	}

	public String getProprietario() {
		return proprietario;
	}

	public void setProprietario(String proprietario) {
		this.proprietario = proprietario;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Elenco other = (Elenco) obj;
		if (id != other.id)
			return false;
		return true;
	}
}
