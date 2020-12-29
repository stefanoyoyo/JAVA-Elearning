package Server.Database;

import java.io.IOException;
import java.util.Scanner;

import Common.Logger;
import Server.Cache;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

/**
 *  @author Amenta Stefano, Moroni Paolo 
 * Classe che gestisce l'apertura della connessione al db,e contiene l'oggetto di riferimento per la connessione
 */
public class DatabaseManager {
	

	public static Connection conn;
	public Connection getConnection ( ) {
		return conn;
	}
	
	/**
	 * Metodo che rende accessibili le credenziali d'accesso in forma statica al resto del codice
	 * @return credenziali d'accesso per il login
	 */


	private void PulisciConsole() throws IOException {
		final String operatingSystem = System.getProperty("os.name");

		if (operatingSystem.contains("Windows")) {
			Runtime.getRuntime().exec("cls");
		} else {
			Runtime.getRuntime().exec("clear");
		}
	}

	/**
	 * Metodo che consente di stabilire la connessione con il databse relazionale
	 * @throws SQLException
	 */
	private void setup() throws SQLException {
		@SuppressWarnings("resource")
		Scanner scan = new Scanner(System.in);
		while ((conn == null) || (conn.isClosed())) {
			
			System.out.println("Tentativo di connessione al database. inserire il nome del host");
			String host = scan.nextLine();
			System.out.println("Inserire Username per l'accesso al DB");
			String user = scan.nextLine();
			System.out.println("Inserire Password per l'accesso al DB");
			String psw = scan.nextLine();
			
			String connectionStringU = "jdbc:postgresql://" + host + ":5432/dbSeatIn";
			try {
				conn = DriverManager.getConnection(connectionStringU, user, psw);
			} catch (Exception e) {
				Logger.WriteError(e, "sas", "sssss");
				try {
					PulisciConsole();
				} catch (IOException e1) {

				}
				System.out.println("Connessione al db fallita. Inserire credenzali valide");
				Logger.WriteError(e, "DBManager", "SignIn");
			}
		}
		System.out.println("Connection up");

		System.out.println("Caricamento liste iniziali");

		Cache.updateDipartimenti(conn);
		Cache.updateCorsiLaurea(conn);
		Cache.updateCorsiMateria(conn);

		System.out.println("Liste iniziali caricate");

	}

	
	

	
	/**
	 * Costruttore della classe che inizializza la connessione con il database
	 */
	public DatabaseManager() {
		try {
			setup();
		} catch (Exception e) {
			System.out.println("Error DB setup");
			System.out.println(e.getMessage());
		}
	}

	/**
	 * Metodo che genera un codice di attivazione
	 * @return
	 */
	public static String generateAttCode() {
		String alfabeto = "123456789";
		Random rand = new Random();
		StringBuilder res = new StringBuilder(); 
		for (int i = 0; i < 6; i++) {
			int randIndex = rand.nextInt(alfabeto.length());
			res.append(alfabeto.charAt(randIndex));
		}
		return res.toString();
	}
}