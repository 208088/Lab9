package it.polito.tdp.porto.model;

import java.util.*;

public class Autore {
	
	private int id_creator;
	private String cognome;
	private String nome;
	List<Articolo> articoli;
	List<Autore> coautori;
	
	public Autore(int id_creator, String cognome, String nome) {
		super();
		this.id_creator = id_creator;
		this.cognome = cognome;
		this.nome = nome;
		articoli= new ArrayList<Articolo>();
		coautori= new ArrayList<Autore>();
	}
	
	public int getId_creator() {
		return id_creator;
	}
	public String getCognome() {
		return cognome;
	}
	public String getNome() {
		return nome;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id_creator;
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
		Autore other = (Autore) obj;
		if (id_creator != other.id_creator)
			return false;
		return true;
	}

	public void trovaCoautori() {
		for (Articolo a : articoli) {
			List<Autore> list=a.getAutori();
			for (Autore l : list) {
				if(l.getId_creator()!=this.id_creator && !coautori.contains(l))
				{
					coautori.add(l);
				}
			}
		}
		
	}

	public List<Articolo> getArticoli() {
		return articoli;
	}

	public void setArticoli(List<Articolo> articoli) {
		this.articoli = articoli;
	}

	public List<Autore> getCoautori() {
		return coautori;
	}

	public void setCoautori(List<Autore> coautori) {
		this.coautori = coautori;
	}
	

}
