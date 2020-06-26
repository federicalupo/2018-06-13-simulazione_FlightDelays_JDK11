package it.polito.tdp.flightdelays.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import it.polito.tdp.flightdelays.db.FlightDelaysDAO;
import it.polito.tdp.flightdelays.model.Evento.Tipo;

public class Simulatore {
	
	//mondo
	private List<Airport> aeroporti;
	private Map<Integer, Airport> posizioneIniziale; //passeggero - posizione
	private Map<Integer, Integer> passeggeroVoli;
	private FlightDelaysDAO dao;
	
	//input
	private Integer nPasseggeri;
	private Integer nVoli;
	//coda
	private PriorityQueue<Evento> coda;
	
	//output
	private Map<Integer, Double> ritardi; //passeggero-ritardo
	
	
	public void init(List<Airport> aeroporti, Integer nPasseggeri, Integer nVoli) {
		this.aeroporti = aeroporti;
		this.nPasseggeri = nPasseggeri;
		this.nVoli = nVoli;
		this.dao = new FlightDelaysDAO();
		
		this.coda = new PriorityQueue<>();
		this.posizioneIniziale = new HashMap<>();
		this.ritardi = new HashMap<>();
		this.passeggeroVoli = new HashMap<>();
		
		for(int i=0; i<nPasseggeri; i++) {
			this.passeggeroVoli.put(i, 0);
			this.ritardi.put(i, 0.0);
		}
		
		for(int i =0 ; i<nPasseggeri; i++) {
			this.posizioneIniziale.put(i,  aeroporti.get((int) (Math.random()*aeroporti.size())));
		}
		
		for(Integer i : posizioneIniziale.keySet()) {
			Volo v = dao.getVolo(posizioneIniziale.get(i).getId());
			if(v!=null) {
				Evento e = new Evento(Tipo.PARTENZA, v.getPartenza(), i, v );
				coda.add(e);
			}
		}
			
		
	}
	
	public void run() {
		while(!coda.isEmpty()) {
			processEvent(coda.poll());
		}
		
	}
	
	private void processEvent(Evento e) {
		switch(e.getTipo()) {
		case PARTENZA:
			
			
			
			this.passeggeroVoli.put(e.getPasseggero(), passeggeroVoli.get(e.getPasseggero())+1); //conto il volo
			
			//conto il ritardo
			this.ritardi.put(e.getPasseggero(), ritardi.get(e.getPasseggero())+(e.getVolo().getRitardo()));
			
			
			Evento evento = new Evento(Tipo.ARRIVO, e.getVolo().getArrivo(), e.getPasseggero(), e.getVolo());
			
			coda.add(evento);
			
			break;
		case ARRIVO:
			
			//if(!controllaVoli()) { //se non tutti hanno V voli, vai avanti 
			
			if(this.passeggeroVoli.get(e.getPasseggero()).compareTo(this.nVoli)!=0) { //non ancora raggiunto V voli
			
				Volo v = dao.getVolo(e.getVolo().getAeroportoArrivo(), e.getTempo());
				if(v!=null) { //c'è il volo
					Evento parto = new Evento(Tipo.PARTENZA, v.getPartenza(), e.getPasseggero(), v);
					coda.add(parto);
				}
			}
		
			break;
		}
		
	}
	
	/*1 interpretazione  => ogni volta controllo se tutti sono arrivati a V voli
	private boolean controllaVoli() {
		boolean trovato = true;
		
		for(Integer i : this.passeggeroVoli.keySet()) {
			if(this.passeggeroVoli.get(i) < this.nVoli) {
				trovato = false;
			}
		}
		return trovato;
	}
	*/

	public Map<Integer, Double> ritardo(){
		return this.ritardi;
	}
}
