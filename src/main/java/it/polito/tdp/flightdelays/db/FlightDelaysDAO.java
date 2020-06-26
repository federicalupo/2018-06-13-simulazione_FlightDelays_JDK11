package it.polito.tdp.flightdelays.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;

import it.polito.tdp.flightdelays.model.Airline;
import it.polito.tdp.flightdelays.model.Airport;
import it.polito.tdp.flightdelays.model.Arco;
import it.polito.tdp.flightdelays.model.Flight;
import it.polito.tdp.flightdelays.model.Volo;

public class FlightDelaysDAO {

	public List<Airline> loadAllAirlines() {
		String sql = "SELECT id, airline from airlines";
		List<Airline> result = new ArrayList<Airline>();

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				result.add(new Airline(rs.getString("ID"), rs.getString("airline")));
			}

			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}

	public List<Airport> loadAllAirports(Map<String, Airport> idMap) {
		String sql = "SELECT id, airport, city, state, country, latitude, longitude FROM airports";
		List<Airport> result = new ArrayList<Airport>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				Airport airport = new Airport(rs.getString("id"), rs.getString("airport"), rs.getString("city"),
						rs.getString("state"), rs.getString("country"), rs.getDouble("latitude"), rs.getDouble("longitude"));
				result.add(airport);
				idMap.put(airport.getId(), airport);
			}
			
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}

	public List<Flight> loadAllFlights() {
		String sql = "SELECT id, airline, flight_number, origin_airport_id, destination_airport_id, scheduled_dep_date, "
				+ "arrival_date, departure_delay, arrival_delay, air_time, distance FROM flights";
		List<Flight> result = new LinkedList<Flight>();

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				Flight flight = new Flight(rs.getInt("id"), rs.getString("airline"), rs.getInt("flight_number"),
						rs.getString("origin_airport_id"), rs.getString("destination_airport_id"),
						rs.getTimestamp("scheduled_dep_date").toLocalDateTime(),
						rs.getTimestamp("arrival_date").toLocalDateTime(), rs.getInt("departure_delay"),
						rs.getInt("arrival_delay"), rs.getInt("air_time"), rs.getInt("distance"));
				result.add(flight);
			}

			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}
	
	public List<Arco> archi(Airline a, Map<String, Airport> idMap){
		String sql="select origin_airport_id, destination_airport_id, avg(arrival_delay) as media " + 
				"from flights " + 
				"where airline=? " + 
				"group by origin_airport_id, destination_airport_id ";
		
		List<Arco> archi = new ArrayList<>();
	

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, a.getId());
			
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				Airport a1 = idMap.get(rs.getString("origin_airport_id"));
				Airport a2 = idMap.get(rs.getString("destination_airport_id"));
				
				if(a1!=null && a2!=null) {
					
					Double distanza = LatLngTool.distance(a1.getPosizione(), a2.getPosizione(), LengthUnit.KILOMETER);
					Double peso = rs.getDouble("media")/distanza;
					
					archi.add(new Arco(a1, a2, peso));
				}
	
			}
			
			conn.close();
			return archi;
			
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
		
	}

	public Volo getVolo(String airport) {
		String sql = "select scheduled_dep_date, destination_airport_id, arrival_date, arrival_delay " + 
				"from flights " + 
				"where year(`SCHEDULED_DEP_DATE`) = 2015 " + 
				"and origin_airport_id=? " + 
				"order by `SCHEDULED_DEP_DATE` " + 
				"limit 1";
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, airport);
			
			ResultSet rs = st.executeQuery();
			
			Volo v = null;

			if(rs.next()) {
				 v = new Volo(rs.getTimestamp("scheduled_dep_date").toLocalDateTime(),
						rs.getString("destination_airport_id"), rs.getTimestamp("arrival_date").toLocalDateTime(), rs.getDouble("arrival_delay"));
			}
			
			conn.close();
			return v;
			
			
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
		
		
	}

	public Volo getVolo(String aeroportoArrivo, LocalDateTime tempo) {
		String sql ="select scheduled_dep_date, destination_airport_id, arrival_date, arrival_delay " + 
				"from flights " + 
				"where year(`SCHEDULED_DEP_DATE`) = 2015 " + 
				"and origin_airport_id=? " + 
				"and scheduled_dep_date>? " + 
				"order by `SCHEDULED_DEP_DATE` " + 
				"limit 1";
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, aeroportoArrivo);
			st.setTimestamp(2, Timestamp.valueOf(tempo));
			
			ResultSet rs = st.executeQuery();
			
			Volo v = null;

			if(rs.next()) {
				 v = new Volo(rs.getTimestamp("scheduled_dep_date").toLocalDateTime(),
						rs.getString("destination_airport_id"), rs.getTimestamp("arrival_date").toLocalDateTime(), rs.getDouble("arrival_delay"));
			}
			
			conn.close();
			return v;
			
			
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
		
	}
}

