package Server.Database.Utenti;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import Common.DBType.Course;
import Common.DBType.Teacher;
import Server.Cache;
import Server.Database.DatabaseManager;

/**
 *  @author Amenta Stefano, Moroni Paolo 
 * Classe per la gestione dei docenti e le operazioni a loro relative su DB
 */
public class DocenteController extends AnagraficaController {

	
	/**
	 * Metodo che consente di assegnare un docente ad un corso 
	 * @param conn connessione al DB
	 * @param idDocente da associare al corso
	 * @param idCorso a cui associare il docente
	 */
	public static void linkDocenteCorso(Connection conn, long idDocente, long idCorso) {
		try {
			long Id = AnagraficaController.getMaxId(conn, "\"TeachVCourse\"", "id");
			String query = "INSERT INTO \"TeachVCourse\" (\"id\",\"teacher\", \"course\") VALUES (" + ++Id + "," + idDocente + "," + idCorso
					+ ")";
			Statement st = conn.createStatement();
			st.executeUpdate(query);
		} catch (Exception e) {
			System.out.println(e);
			System.out.println("Errore link docente course");
		}
	}

	
	/**
	 * Metodo per la creazione di un docente
	 * @param conn connessione al DB
	 * @param newDocente anagrafica deldocente da creare
	 */
	public static void creaDocente(Connection conn, Teacher newDocente) {
		newDocente.activationCode = DatabaseManager.generateAttCode();
		try {
			boolean isZero = newDocente.userID == 0? true:false;
			long matr = 0;
				if (isZero) {
				matr = getMaxId(conn,"\"Userdata\"", "userid");
				newDocente.userID = ++matr;
				}	
			long codAtt = AnagraficaController.genCodAtt();
			PreparedStatement st = conn.prepareStatement(
					"INSERT INTO \"Userdata\" (\"userid\", \"email\", \"name\", \"surname\", \"password\","
					+ " \"activationcode\", \"trusted\",  \"islocked\", \"loginattempts\") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
			st.setLong(1, newDocente.userID);
			st.setString(2, newDocente.Email);
			st.setString(3, newDocente.name);
			st.setString(4, newDocente.surname);
			st.setString(5, newDocente.Password);
			st.setBoolean(7, newDocente.trusted);
			st.setBoolean(8, newDocente.isLocked);
			st.setLong(9, newDocente.loginattempts);
			st.setLong(6, codAtt);
			st.executeUpdate();
			st.close();
			java.sql.Statement st1 = conn.createStatement();
			ResultSet rs = st1.executeQuery(
					"SELECT \"userid\" FROM \"Userdata\" WHERE \"email\" LIKE \'" + newDocente.Email + "\'");
			String matS = "-1";
			while (rs.next()) {
				matS = rs.getString(1);
			}
			st1.close();
			long mat = Long.parseLong(matS);
			PreparedStatement stF = conn
					.prepareStatement("INSERT INTO \"Teachers\" (\"userid\", \"department\") " + "VALUES (?,?)");
			stF.setLong(1, mat);
			stF.setLong(2, newDocente.department);
			stF.executeUpdate();
			stF.close();
			Server.Utilities.EmailSender.send_uninsubria_email(newDocente.Email, "Registrazione SimpleElearning",
					"Codice d'attivazione:" + codAtt);
		} catch (Exception e) {
			System.out.println("");
			System.out.println(e.getMessage());
		}
	}

	
	/**
	 * Metodo che restituisce la lista dei corsi tenuti da un docente
	 * @param conn connessione al DB
	 * @param matricola del docente 
	 * @return corsi tenuti dal docente
	 */
	public static ArrayList<Course> getCorsiDocente(Connection conn, long matricola) { 
		String query = "SELECT \"TeachVCourse\".\"course\" FROM \"TeachVCourse\" WHERE \"TeachVCourse\".\"teacher\" = "
				+ matricola;
		ArrayList<Course> lista = new ArrayList<Course>();
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(query);
			while (rs.next()) {
				try {
				lista.add(Cache.getCorsoMateria(rs.getLong(1)));
				} catch (Exception ex) {
					ex.getMessage();
				}
			}
			return lista;
		} catch (Exception e) {
			System.out.println("Errore nell'ottenimento dei corsi dell'insegnante");
		}

		return null;
	}
	
	/**
	 * Metodo che chiede al DB una lista di docenti bloccati
	 * @param conn connessione al DB
	 * @return lista docenti bloccati
	 */
	public static ArrayList<Teacher> getLockedDocenti(Connection conn) {
		String query = "SELECT * FROM \"Teachers\" JOIN \"Userdata\" ON \"Userdata\".\"userid\" = \"Teachers\".\"userid\""
				+ " where \"islocked\" = '" + true + "';";
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(query);
			Teacher newDoc = null;
			ArrayList<Teacher> listaDoc = new ArrayList<Teacher>();
			while (rs.next()) {
				newDoc = new Teacher();
				newDoc.userID = rs.getLong("userid");
				newDoc.name = rs.getString("name");
				newDoc.surname = rs.getString("surname");
				newDoc.Email = rs.getString("email");
				listaDoc.add(newDoc);
			}
			return listaDoc;
		} catch (Exception e) {
			System.out.println("Errore nell'ottenimento dei docenti bloccati");
		}
		return new ArrayList<Teacher>();
	}
	
	
	/**
	 * Metodo che si occupa della gesione del login di un docente
	 * @param conn connessione al DB
	 * @param Email del docente
	 * @param Psw password del docente
	 * @return anagrafica presa dal DB del docente appena loggato
	 */
	public static Teacher login(Connection conn, String Email, String Psw) {
		Teacher newDocente = null;

		String query = "SELECT * FROM \"Userdata\""
				+ " JOIN \"Teachers\" ON \"Userdata\".\"userid\" = \"Teachers\".\"userid\""
				+ " WHERE \"email\" LIKE '" + Email + "' AND \"password\" LIKE '" + Psw + "'";
		try {
			Statement s = conn.createStatement();
			ResultSet rs = s.executeQuery(query);
			while (rs.next()) {
				newDocente = new Teacher();
				newDocente.userID = rs.getLong("userid");
				newDocente.name = rs.getString("name");
				newDocente.surname = rs.getString("surname");
				newDocente.Email = rs.getString("email");
				newDocente.Password = rs.getString("password");
				newDocente.trusted = rs.getBoolean("trusted");
				newDocente.department = rs.getLong("department");
				
				try {
				 newDocente.courses = getCorsiDocente(conn, newDocente.userID);
				} catch (Exception ex) {
					ex.getMessage().toString();
				}

			}
			return newDocente;

		} catch (Exception e) {
			System.out.println(e);
			System.out.println("Errore Login Professore");
		}
		return null;
	}
	
	/**
	 * Metodo per chiedere al database la lista dei docenti legata ad un corso. 
	 * @param conn connessione al DB
	 * @param idCorso di cui avere la lista dei docenti
	 * @return lista docenti legata ad un corso
	 */
	public static ArrayList<Teacher> emailDocentefromCorso(Connection conn, long idCorso) {
		ArrayList<Teacher> emailDocentefromCorso = new ArrayList<Teacher> ();
		String query = "SELECT * FROM \"Userdata\" JOIN \"TeachVCourse\" "
				+ "ON \"TeachVCourse\".\"teacher\" = \"Userdata\".\"userid\"\r\n"
				+ "WHERE \"TeachVCourse\".\"course\" = " + idCorso;
		Teacher docente = null;
		try {
		Statement s = conn.createStatement();
		ResultSet rs = s.executeQuery(query);
		while (rs.next()) {
			docente = new Teacher();
			docente.Email = rs.getString("email");
			docente.name = rs.getString("name");
			docente.surname = rs.getString("surname");
			docente.userID = rs.getLong("userid");
			emailDocentefromCorso.add(docente);
		}
		return emailDocentefromCorso;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
		
	}
	
	
	/**
	 * Metodo che restituisce una lista di tutti i docenti
	 * @param conn connessione al DB
	 * @return lista di tutti i docenti
	 */
	public static ArrayList<Teacher> getAllDocenti(Connection conn) {
		String query = "SELECT * FROM \"Teachers\" JOIN \"Userdata\" ON \"Userdata\".\"userid\" = \"Teachers\".\"userid\"";
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(query);
			Teacher newDoc = null;
			ArrayList<Teacher> listaDoc = new ArrayList<Teacher>();
			while (rs.next()) {
				newDoc = new Teacher();
				newDoc.userID = rs.getLong("userid");
				newDoc.name = rs.getString("name");
				newDoc.surname = rs.getString("surname");
				newDoc.department = Cache.getDipartimento(rs.getLong("department"));
				newDoc.Email = rs.getString("email");
				listaDoc.add(newDoc);
			}
			return listaDoc;
		} catch (Exception e) {
			System.out.println("Errore nell'ottenimento di tutti i docenti");
		}
		return new ArrayList<Teacher>();
	}

}
