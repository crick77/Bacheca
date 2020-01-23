package it.dipvvf.abr.app.bacheca.model;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * POJO per i record della tabella Allegato
 * 
 * @author riccardo.iovenitti
 *
 */
@XmlRootElement
public class Allegato {
	private int id;
	private String nomefile;
	private int dimensione;
	@XmlTransient
	@JsonIgnore
	private byte[] contenuto;
	
	public Allegato() {
		super();
	}

	public Allegato(int id, String nomefile, int dimensione, byte[] contenuto) {
		super();
		this.id = id;
		this.nomefile = nomefile;
		this.dimensione = dimensione;
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
