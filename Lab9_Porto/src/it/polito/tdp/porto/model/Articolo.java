package it.polito.tdp.porto.model;

import java.util.*;

public class Articolo {

	private int eprintid;
	private int year;
	private String title;
	List<Autore> autori= new ArrayList<Autore>();
	
	public List<Autore> getAutori() {
		return autori;
	}

	public void setAutori(List<Autore> autori) {
		this.autori = autori;
	}

	public Articolo(int eprintid, int year, String title) {
		this.eprintid = eprintid;
		this.year = year;
		this.title = title;
	}

	public int getEprintid() {
		return eprintid;
	}

	public int getYear() {
		return year;
	}

	public String getTitle() {
		return title;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + eprintid;
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
		Articolo other = (Articolo) obj;
		if (eprintid != other.eprintid)
			return false;
		return true;
	}

	
	
}
