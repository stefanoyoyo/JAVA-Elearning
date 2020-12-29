package Server;

import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Scanner;
import Server.Database.DatabaseManager;
import Server.Database.Utenti.AdminController;

/**
 * Classe principale del server, accetta connessioni in entrata e per ogniuna di
 * queste istanzia un thread per il client
 * 
 * @author Amenta Stefano, Moroni Paolo
 *
 *
 */
public class ServerMain {

	/**
	 * Metodo per far loggare l'admin all'avvio del server
	 * 
	 * @param dbm
	 * @param scan
	 * @return
	 */
	@SuppressWarnings("resource")
	private static boolean startupLogin(DatabaseManager dbm, Scanner scan) {
		boolean scelta = false;
		do {
		if (AdminController.isThereAdmin(DatabaseManager.conn)) {
			System.out.println("Desideri registrare un nuovo Admin (0) o effettuare un Login? (1)");
			String risp = null;
			boolean adminCreato = false;
			risp = new Scanner(System.in).nextLine();
			scan.reset();
			switch (risp) {
			case "0":
				adminCreato = AdminController.creaAdmin(DatabaseManager.conn);
					if (adminCreato) {
						scelta = true;
					} else {
						scelta = false;
					}
				break;
			case "1":
				if (AdminController.LoginAdmin(DatabaseManager.conn)) {
					System.out.println("Login Effettuato");
					scelta = true;
					return scelta;
				} else {
					System.out.println("Login fallito");
				}
				break;
			default:
				System.out.println("Errore, selezionare una risposta valida");
				break;
			}

		} else {
			System.out.println("Non esiste nessun admin... avviamento creazione di un nuovo profilo amministratore");
			AdminController.creaAdmin(DatabaseManager.conn);
		}
		} while (!scelta);
		
		return scelta;
		
	}

	/**
	 * Main del server
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		final int PORT = 50123;
		ServerSocket ss = null;
		Socket s = null;
		ServerThread newClient = null; // server thread
		// Ciao
		try {
			Scanner scan = new Scanner(System.in);
			
			DatabaseManager dbm = new DatabaseManager();
			 Server.Utilities.EmailSender.SetPassword(scan);			
			startupLogin(dbm, scan);
			
			scan.close();
			try {
			ss = new ServerSocket(PORT);
			} catch (SocketException sock) {
				sock.printStackTrace();
			}
			System.out.println("Server avviato con successo");
			while (true) {
				
				System.out.println("Ricerca nuova connessione...");
				 s = ss.accept();
				System.out.println("Connessione ricevuta, avvio nuovo client");
			    newClient = new ServerThread(s, dbm);
				newClient.start();
			}
		} catch (Exception e) {
		}
	}
	
}