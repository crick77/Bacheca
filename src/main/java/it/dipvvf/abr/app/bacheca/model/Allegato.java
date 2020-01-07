package it.dipvvf.abr.app.bacheca.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Allegato {
	private int id;
	private String nomefile;
	private int dimensione;
	private String descrizione;
	private int ordine;
	private byte[] contenuto;
	
	public Allegato() {
		super();
	}

	public Allegato(int id, String nomefile, int dimensione, String descrizione, int ordine, byte[] contenuto) {
		super();
		this.id = id;
		this.nomefile = nomefile;
		this.dimensione = dimensione;
		this.descrizione = descrizione;
		this.ordine = ordine;
		this.contenuto = contenuto;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNomefile() {
		return nomefile;
	}

	public void setNomefile(String nomefile) {
		this.nomefile = nomefile;
	}

	public int getDimensione() {
		return dimensione;
	}

	public void setDimensione(int dimensione) {
		this.dimensione = dimensione;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public int getOrdine() {
		return ordine;
	}

	public void setOrdine(int ordine) {
		this.ordine = ordine;
	}

	public byte[] getContenuto() {
		return contenuto;
	}

	public void setContenuto(byte[] contenuto) {
		this.contenuto = contenuto;
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
		Allegato other = (Allegato) obj;
		if (id != other.id)
			return false;
		return true;
	}
}
