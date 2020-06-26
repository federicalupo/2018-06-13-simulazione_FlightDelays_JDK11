package it.polito.tdp.flightdelays.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.flightdelays.db.FlightDelaysDAO;

public class Model {
	
	private FlightDelaysDAO dao;
	private Graph<Airport, DefaultWeightedEdge> grafo;
	private Map<String, Airport> idMap;
	private List<Arco> archi;
	private List<Airport> vertici;
	
	public Model() {
		dao = new FlightDelaysDAO();
	}

	public List<Airline> tendina(){
		return dao.loadAllAirlines();
	}
	
	public void creaGrafo(Airline airline) {
		grafo = new SimpleDirectedWeightedGraph<Airport, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		idMap = new HashMap<>();
		
		vertici = dao.loadAllAirports(idMap);
		Graphs.addAllVertices(this.grafo, vertici);
		
		archi = dao.archi(airline, idMap);
		
		for(Arco a : dao.archi(airline, idMap)) {
			if(!this.grafo.containsEdge(a.getA1(), a.getA2()))
				Graphs.addEdge(this.grafo, a.getA1(), a.getA2(), a.getPeso());
		}
	}
	
	public List<Arco> rottePeggiori(){
	
		List<Arco> peggiori = new ArrayList<>();
		
		Collections.sort(this.archi);
		
		for(int i=0; i<10 && i<archi.size(); i++) {
			peggiori.add(archi.get(i));
		}
		
		return peggiori;
		
	}

	public Integer nVertici() {
		return grafo.vertexSet().size();
	}
	public Integer nArchi() {
		return grafo.edgeSet().size();
	}
	
	public Map<Integer, Double> simulazione(Integer nPasseggeri, Integer nVoli){
		Simulatore simulatore = new Simulatore();
		simulatore.init(vertici, nPasseggeri, nVoli);
		simulatore.run();
		
		return simulatore.ritardo();
		
	}
}
