package it.polito.tdp.flightdelays.model;

import java.time.LocalDateTime;

public class Volo {
	private LocalDateTime partenza;
	private String aeroportoArrivo; 
	private LocalDateTime arrivo;
	private Double ritardo;
	
	public Volo(LocalDateTime partenza, String aeroportoArrivo, LocalDateTime arrivo, Double ritardo) {
		super();
		this.partenza= partenza;
		this.aeroportoArrivo=aeroportoArrivo;
		this.arrivo = arrivo;
		this.ritardo = ritardo;
	}

	public LocalDateTime getArrivo() {
		return arrivo;
	}

	public Double getRitardo() {
		return ritardo;
	}

	public LocalDateTime getPartenza() {
		return partenza;
	}

	public String getAeroportoArrivo() {
		return aeroportoArrivo;
	}

	@Override
	public String toString() {
		return  partenza + " " + aeroportoArrivo + " " + arrivo
				+ " " + ritardo;
	}



	

}
