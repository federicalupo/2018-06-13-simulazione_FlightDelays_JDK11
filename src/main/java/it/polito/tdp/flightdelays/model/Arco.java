package it.polito.tdp.flightdelays.model;

public class Arco implements Comparable<Arco> {

	
	private Airport a1;
	private Airport a2;
	private Double peso;
	
	public Arco(Airport a1, Airport a2, Double peso) {
		this.a1=a1;
		this.a2=a2;
		this.peso=peso;
	}

	public Airport getA1() {
		return a1;
	}

	public Airport getA2() {
		return a2;
	}

	public Double getPeso() {
		return peso;
	}

	@Override
	public String toString() {
		return  a1 + " - " + a2 + " " + peso;
	}

	@Override
	public int compareTo(Arco o) {
		return -this.peso.compareTo(o.getPeso());
	}
	
	
}
