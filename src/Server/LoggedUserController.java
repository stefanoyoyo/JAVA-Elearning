package Server;

import java.sql.Connection;
import java.util.ArrayList;

import Common.DBType.UserMonitoring;
import Server.Database.Corsi.CorsiLaureaController;

/**
 * Classe che contiene una lista di utenti loggato
 * @author Amenta Stefano, Moroni Paolo
 *
 */
public class LoggedUserController {
	public static ArrayList<UserMonitoring> loggedUserList = new ArrayList<UserMonitoring>();

	/**
	 * Funzione per aggiornare la posizione dell'utente nel client
	 * 
	 * @param newCorso
	 * @param userID
	 * @param conn
	 */
	public static void updateLocUser(long newCorso, long userID, Connection conn) {
		long sec = 0;
		long oldCorsoID = -1;
		for (UserMonitoring user : loggedUserList) {;
			if (user.userID == userID) {
				oldCorsoID = user.IdCorsoAttuale;
				sec = user.updateLocation(newCorso);
				user.IdCorsoAttuale = newCorso;
				break;
			}
		}
		if (newCorso != -1) {
			CorsiLaureaController.updateAccessCount(conn, newCorso, userID);
		}
		
		CorsiLaureaController.updateSecondsUser(conn, oldCorsoID, sec);
	}

	/**
	 * Funzione per ottenere il numero degli utenti attualmente connessi in un corso
	 * 
	 * @param corsoID
	 * @return numUtenti
	 */
	public static int getUserNumberCorso(long corsoID) {
		int cont = 0;
		for (UserMonitoring user : loggedUserList) {
			if (user.IdCorsoAttuale == corsoID) {
				cont++;
			}
		}
		return cont;
	}
}
