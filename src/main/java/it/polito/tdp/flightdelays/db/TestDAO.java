package it.polito.tdp.flightdelays.db;

import java.sql.Connection;

public class TestDAO {

	public static void main(String[] args) {
		
		try {
			Connection connection = DBConnect.getConnection();
			connection.close();
			System.out.println("Test PASSED");

		} catch (Exception e) {
			System.err.println("Test FAILED");
		}
		

		FlightDelaysDAO dao = new FlightDelaysDAO();

		System.out.println(dao.loadAllAirlines());
	//	System.out.println(dao.loadAllAirports());
		System.out.println(dao.loadAllFlights());
	}

}
