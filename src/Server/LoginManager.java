package Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import Common.DBType.Userdatas;
import Common.DBType.Course;
import Common.DBType.UserMonitoring;
import Common.Pacchetti.RequestContent;
import Common.Pacchetti.ReceiveContent;
import Server.Database.DatabaseManager;
import Server.Database.Utenti.AdminController;
import Server.Database.Utenti.DocenteController;
import Server.Database.Utenti.StudentController;

/**
 * Classe per la gestione del login
 * @author Amenta Stefano, Moroni Paolo
 *
 */
public class LoginManager extends Thread {
	
//	Socket s;
//	DatabaseManager db;
//	UserMonitoring utenteLoggato;
//	ObjectOutputStream oos;
//	ObjectInputStream ois;
//	
//	/**
//	 * Inizializzazione classe per la gestione del login
//	 * @param sock
//	 * @param dbm
//	 */
//	public LoginManager(Socket sock, DatabaseManager dbm) {
//		this.s = sock;
//		this.db = dbm;
//	}
//
//	public void run() {
//		try {
//
//			boolean uscita = false;
//			oos = new ObjectOutputStream(s.getOutputStream());
//			ois = new ObjectInputStream(s.getInputStream());
//			while (!uscita) {
//				RequestContent rp = (RequestContent) ois.readObject();
//				switch (rp.type) {
//				case LOGIN_USER:
//					manageLogin(rp);
//					break;
//				default:
//					uscita = true;
//					break;
//				}
//			}
//		} catch (Exception e) {
//			System.out.println(e.toString());
//			System.out.println("Chiusura socket inaspettata");
//			LoggedUserController.loggedUserList.remove(this.utenteLoggato);
//		}
//	}
//
//	/**
//	 * Classe per la gestione del login lato client
//	 * @param rp
//	 */
//	private void manageLogin(RequestContent rp) {
//		Userdatas logged = null;
//		switch (rp.userType) {
//		case ADMIN:
//			logged = AdminController.login(db.conn, rp.parameters[0].toString(), rp.parameters[1].toString());
//			break;
//		case TEACHER:
//			logged = DocenteController.login(db.conn, rp.parameters[0].toString(), rp.parameters[1].toString());
//			break;
//		case STUDENT:
//			logged = StudentController.login(db.conn, rp.parameters[0].toString(), rp.parameters[1].toString());
//			break;
//		default:
//			break;
//		}
//		ReceiveContent pr = new ReceiveContent();
//		if (logged != null) {
//			logged.t = rp.userType;
//			this.utenteLoggato = new UserMonitoring(logged);
//			LoggedUserController.loggedUserList.add(this.utenteLoggato);
//			pr.parameters = new Object[] { logged };
//		} else {
//			pr.parameters = new Object[] {};
//
//		}
//
//		try {
//			
//		} catch (Exception e) {
//			System.out.println("Errore invio risposta al client");
//		}
//	}

}
