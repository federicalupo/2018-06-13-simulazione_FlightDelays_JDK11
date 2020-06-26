package it.polito.tdp.flightdelays.model;

import java.time.LocalDateTime;

public class Evento implements Comparable<Evento>{
	
	public enum Tipo{
		PARTENZA,
		ARRIVO
	}
	
	private Tipo tipo;
	private LocalDateTime tempo;
	private Integer passeggero;
	private Volo volo;
	
	public Evento(Tipo tipo, LocalDateTime tempo, Integer passeggero, Volo volo) {
		super();
		this.tipo = tipo;
		this.tempo = tempo;
		this.passeggero = passeggero;
		this.volo = volo;
	}

	public Tipo getTipo() {
		return tipo;
	}

	public LocalDateTime getTempo() {
		return tempo;
	}

	public Integer getPasseggero() {
		return passeggero;
	}

	public Volo getVolo() {
		return volo;
	}

	@Override
	public int compareTo(Evento o) {
		
		return this.tempo.compareTo(o.getTempo());
	}
	
	
	

}
