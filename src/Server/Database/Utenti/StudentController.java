package Server.Database.Utenti;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import Common.Logger;
import Common.DBType.Course;
import Common.DBType.Student;
import Server.Cache;
import Server.Database.DatabaseManager;
import Common.Enumerators.*;


/**
 *  @author Amenta Stefano, Moroni Paolo 
 * Classe per la gestione degli studenti e le operazioni a loro relative su DB
 */
public class StudentController extends AnagraficaController {

	/**
	 * Metodo per chiedere al DB una lista degli utenti bloccati a seguito di 10 tentativi di login falliti
	 * @param conn connessione al db
	 * @return lista di utenti
	 */
	public static ArrayList<Student> getLockedStudenti(Connection conn) {
		String query = "SELECT * FROM \"Students\" JOIN \"Userdata\" ON \"Userdata\".\"userid\" = \"Students\".\"userid\""
				+ " where \"islocked\" = '" + true + "';";
		
		ArrayList<Student> listaDoc = null;
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(query);
			Student newDoc = null;
			listaDoc = new ArrayList<Student>();
			while (rs.next()) {
				newDoc = new Student();
				newDoc.userID = rs.getLong("userid");
				newDoc.name = rs.getString("name");
				newDoc.surname = rs.getString("surname");
				newDoc.Email = rs.getString("email");
				listaDoc.add(newDoc);
			}
		} catch (Exception ex) {
			
		}
		
		return listaDoc;
	}
	
	
	/**
	 * Metodo che conterte l'intero passato come parametro nel corrispettivo tipo enumerativo
	 * @param sc numero da convertire
	 * @return tipo enumerativo corrispondente
	 */
	public static CareerStatus ConvertiStatoCar(int sc) {
		switch (sc) {
		case 1:
			return CareerStatus.I;
		case 2:
			return CareerStatus.II;
		case 3:
			return CareerStatus.III;
		default:
			return CareerStatus.LATE;
		}
	}
	
	
	/**
	 * Metodo che conterte l'intero passato come parametro sotto forma di stirnga nel corrispettivo tipo enumerativo
	 * @param sc numero da convertire
	 * @return tipo enumerativo corrispondente
	 */
	public static CareerStatus ConvertiStatoCar(String sc) {
		switch (sc) {
		case "I":
			return CareerStatus.I;
		case "II":
			return CareerStatus.II;
		case "III":
			return CareerStatus.III;
		default:
			return CareerStatus.LATE;
		}
	}
	
	
	/**
	 * Metodo per creare l'anagrafica di un nuovo studente sul DB
	 * @param conn connessione al DB
	 * @param newStudente da inserire sul DB
	 */
	public static void creaStudente(Connection conn, Student newStudente) {
		newStudente.activationCode = DatabaseManager.generateAttCode();
		try {
			boolean isZero = newStudente.userID == 0? true:false;
			long matr = 0;
				if (isZero) {
				matr = getMaxId(conn,"\"Userdata\"", "userid");
				newStudente.userID = ++matr;
				}	
			long codAtt = AnagraficaController.genCodAtt();
			System.out.println("DEBUG: creaStudente getMaxId(.. ) + 1: " + matr);
			
			PreparedStatement st = conn.prepareStatement(
					"INSERT INTO \"Userdata\" (\"userid\", \"email\", \"name\", \"surname\", \"password\","
					+ " \"activationcode\", \"trusted\",  \"islocked\", \"loginattempts\") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
			st.setLong(1, newStudente.userID);		
			st.setString(2, newStudente.Email);
			st.setString(3, newStudente.name);
			st.setString(4, newStudente.surname);
			st.setString(5, newStudente.Password);
			st.setBoolean(7, newStudente.trusted);
			st.setBoolean(8, newStudente.isLocked);
			st.setLong(9, newStudente.loginattempts);
			st.setLong(6, codAtt);
			st.executeUpdate();
			st.close();
			java.sql.Statement st1 = conn.createStatement();
			ResultSet rs = st1.executeQuery(
					"SELECT \"userid\" FROM \"Userdata\" WHERE \"email\" LIKE \'" + newStudente.Email + "\'");
			String matS = "-1";
			while (rs.next()) {
				matS = rs.getString(1);
			}
			st1.close();
			long mat = Long.parseLong(matS);
			PreparedStatement stF = conn
					.prepareStatement("INSERT INTO \"Students\" (\"userid\", \"course\", \"startyear\", \"status\") "
							+ "VALUES (?,?,?,?)");
			stF.setLong(1, mat);
			stF.setLong(2, newStudente.cLaurea);
			stF.setInt(3, newStudente.startYear);
			stF.setString(4, newStudente.status.toString());
			stF.executeUpdate();
			stF.close();
			Server.Utilities.EmailSender.send_uninsubria_email(newStudente.Email, "Registrazione SimpleElearning",
					"Codice d'attivazione:" + codAtt);
		} catch (Exception e) {
			Logger.WriteError(e, "StudentController", "CreaStudente");
		}
	}

	
	/**
	 * Metodo per prelevare dal database la lista dei corsi di uno studente
	 * @param conn connessione al DB
	 * @param studID id dello studente
	 * @return una lista di corsi
	 */
	public static ArrayList<Course> reperisciCorsi(Connection conn, long studID) {
		String query = "SELECT \"Courses\".\"id\" FROM \"Courses\" JOIN \"StudVCourse\""
				+ "ON \"Courses\".\"id\" = \"StudVCourse\".\"degreecourse\" "
				+ "JOIN \"Students\" on \"Students\".\"userid\" = \"StudVCourse\".\"student\" "
				+ "WHERE \"Students\".\"userid\" = " + studID;
		ArrayList<Course> lista = new ArrayList<Course>();
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(query);
			while (rs.next()) {
				lista.add(Cache.getCorsoMateria(rs.getLong(1)));
			}
			return lista;

		} catch (Exception e) {
			Logger.WriteError(e, "StudentController", "ReperisciCorsi");
		}
		return null;
	}
	
	
	/**
	 * Metodo che si occupa della gesione del login di uno studente
	 * @param conn connessione al DB
	 * @param Email dello studente
	 * @param Psw password dello studente
	 * @return anagrafica presa dal DB dello studente appena loggato
	 */
	public static Student login(Connection conn, String Email, String Psw) {
		Student newStudente = null;
		String query = "SELECT * FROM \"Userdata\""
				+ " JOIN \"Students\" ON \"Userdata\".\"userid\" = \"Students\".\"userid\""
				+ " WHERE \"email\" LIKE '" + Email + "' AND \"password\" LIKE '" + Psw + "'";
		try {
			Statement s = conn.createStatement();
			ResultSet rs = s.executeQuery(query);
			while (rs.next()) {
				
				System.out.println("DEBUG: " + "StudentController.Login() rs contiene valori: " + true);
				
				newStudente = new Student();
				newStudente.userID = rs.getLong("userid");
				newStudente.name = rs.getString("name");
				newStudente.surname = rs.getString("surname");
				newStudente.Email = rs.getString("Email");
				newStudente.Password = rs.getString("Password");
				newStudente.trusted = rs.getBoolean("trusted");
				newStudente.cLaurea = rs.getLong("course");	
				newStudente.startYear = rs.getInt("startyear");
				
				try {
				 newStudente.status = ConvertiStatoCar(rs.getString("status"));
				
				} catch (Exception ex) {
					System.out.println("DEBUG: " + "StudentController.Login() newStudente.statoCarriera?  " + ex.getMessage());
				}
					
				newStudente.activationCode = rs.getString("status");											
			}
			
			
			return newStudente;

		} catch (Exception e) {
			Logger.WriteError(e, "StudentController", "Login");
		}

		return null;
	}

	
	/**
	 * Metodo che permette a uno studente di iscriversi ad un corso 
	 * @param conn connessione al DB
	 * @param idUtente che intende iscriversi al corso 
	 * @param idCorso al quale occorre iscriversi
	 */
	public static void subscribeCourse(Connection conn, long idUtente, long idCorso) {
		try {
			long Id = AnagraficaController.getMaxId(conn, "\"StudVCourse\"", "id");
			long numeroCorsi = -1;
			// controllo se lo studente si era già registrato al corso 
			String query = "select count(\"degreecourse\") from \"StudVCourse\" where \"degreecourse\" = "+ idCorso + " and \"student\" = " + idUtente +";";
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(query);
			while (rs.next()) {
				numeroCorsi = rs.getLong(1);
			}
			System.out.println("DEBUG: numeroCorsi: " + numeroCorsi) ;
			if (numeroCorsi>1) {
				System.out.println("Sei già iscritto a questo corso");
				return;
			}
			
			query = "INSERT INTO \"StudVCourse\" (\"id\",\"student\", \"degreecourse\")" + " VALUES ("+ ++Id + "," + idUtente + ","
					+ idCorso + ")";
			st.executeUpdate(query);
			
			query = "SELECT \"email\" FROM \"Userdata\" WHERE \"userid\" = '" + idUtente + "';";
			rs = st.executeQuery(query);
			String emailStud = "";
			while (rs.next()) {
				emailStud = rs.getString("Email");
			}
			query = "SELECT \"name\", \"activationyear\" FROM \"Courses\" WHERE \"id\" = " + idCorso;
			rs = st.executeQuery(query);
			String nomeCorso = "";
			long annoAtt = 0;
			while (rs.next()) {
				nomeCorso = rs.getString("name");
				annoAtt = rs.getLong("activationyear");
			}
			query = "SELECT \"email\" FROM \"Userdata\" JOIN \"TeachVCourse\" "
					+ "ON \"TeachVCourse\".\"teacher\" = \"Userdata\".\"userid\"\r\n"
					+ "WHERE \"TeachVCourse\".\"course\" = " + idCorso;
			Server.Utilities.EmailSender.send_uninsubria_email(emailStud, "Iscrizione corso",
					"Complimenti, ti sei iscritto al corso " + nomeCorso + "-" + annoAtt);
			rs = st.executeQuery(query);
			while (rs.next()) {
				rs.getString("Email");
				Server.Utilities.EmailSender.send_uninsubria_email(emailStud, "Iscrizione studente al corso",
						"Lo studente " + "" + emailStud + " si è iscritto al corso");
			}
		} catch (Exception e) {
			Logger.WriteError(e, "StudenteController", "SubscribeCourse");
		}
	}
	
	
	/**
	 * Metodo che restituisce una lista di tutti gli studenti 
	 * @param conn connessione al DB
	 * @return
	 */
	public static ArrayList<Student> getAllStudenti(Connection conn) {
		try {
			ArrayList<Student> lista = new ArrayList<Student>();

			Student newStudente = null;
			String query = "SELECT * FROM \"Userdata\""
					+ " JOIN \"Students\" ON \"Userdata\".\"userid\" = \"Students\".\"userid\"";
			Statement s = conn.createStatement();
			ResultSet rs = s.executeQuery(query);
			while (rs.next()) {
				newStudente = new Student();
				newStudente.userID = rs.getLong("userid");
				newStudente.name = rs.getString("name");
				newStudente.surname = rs.getString("surname");
				newStudente.Email = rs.getString("email");
				newStudente.trusted = rs.getBoolean("trusted");
				newStudente.cLaurea = Cache.getCorsoLaurea(rs.getLong("course")).Id;
				newStudente.startYear = rs.getInt("startyear");
				newStudente.activationCode = rs.getString("activationcode");
				lista.add(newStudente);
			}
			return lista;
		} catch (Exception e) {
			Logger.WriteError(e, "StudentController", "GET_ALL_STUDENTS");
		}
		return null;
	}

	
	/**
	 * Metodo che restituisce l'anagrafica di uno studente 
	 * @param conn connessione al DB
	 * @param matricola dello studente 
	 * @return anagrafica studente
	 */
	public static Student getStudenteByMat(Connection conn, long matricola) {
		try {
			Student newStudente = null;
			String query = "SELECT * FROM \"Userdata\""
					+ "					JOIN \"Students\" ON \"Userdata\".\"userid\" = \"Students\".\"userid\""
					+ "					WHERE  \"Userdata\".\"userid\" = " + matricola;
			Statement s = conn.createStatement();
			ResultSet rs = s.executeQuery(query);
			while (rs.next()) {
				newStudente = new Student();
				newStudente.userID = rs.getLong("userid");
				newStudente.name = rs.getString("name");
				newStudente.surname = rs.getString("surname");
				newStudente.Email = rs.getString("email");
				newStudente.trusted = rs.getBoolean("trusted");
				newStudente.cLaurea = Cache.getCorsoLaurea(rs.getLong("course")).Id;
				newStudente.startYear = rs.getInt("startYear");
				newStudente.activationCode = rs.getString("activationcode");
			}
			return newStudente;
		} catch (Exception e) {
			Logger.WriteError(e, "StudentController", "GET_STUDENT_BY_USERID");
		}
		return null;
	}

	
	/**
	 * Metodo che consente di conoscere il tipo di utente passato come parametro
	 * @param conn connessione al DB
	 * @param matricola dell'utente di cui occorre conoscere il tipo
	 * @return tipo dell'utente
	 */
	public static UserType getUserType(Connection conn, long matricola) {
		String query = "select \"userid\" from \"Userdata\" where \"userid\" = " + matricola + ";";
		try {
			Statement s = conn.createStatement();
			ResultSet rs = s.executeQuery(query);
			long matr = 0;
			while (rs.next()) {
				matr = rs.getLong("userid");
			}
			if(matr != 0) {
				return UserType.STUDENT;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	
		return null;
		
	}

	
	/**
	 * Metodo che restituisce una lista di studenti passato un corso come parametro
	 * @param conn connessione al DB
	 * @param idCorso seguito dagli studenti restituiti
	 * @return lista studenti che seguono il corso
	 */
	public static ArrayList<Student> getStudentiByCorso(Connection conn, long idCorso) {
		String query = "select * from \"Students\" join \"Userdata\" on \"Students\".\"userid\" = \"Userdata\".\"userid\""
				+ "where \"Students\".\"course\" in (select \"course\" from \"StudVCourse\"); " ;
		Student newStudente = null;
		ArrayList<Student> studentiByCorso = new ArrayList<Student> ();
		try {			
		Statement s = conn.createStatement();
		ResultSet rs = s.executeQuery(query);
		while (rs.next()) {
			
			newStudente = new Student();
			newStudente.userID = rs.getLong("userid");
			newStudente.name = rs.getString("name");
			newStudente.surname = rs.getString("surname");
			newStudente.Email = rs.getString("email");
			studentiByCorso.add(newStudente);
		}
		return studentiByCorso;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		return null;
	}
}
