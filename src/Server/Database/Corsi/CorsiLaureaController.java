package Server.Database.Corsi;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import Common.DBType.*;
import Server.Cache;
import Server.Database.Utenti.AnagraficaController;

/**
 * Classe che contiene tutti i metodi per i corsi e le operazioni a loro relativi per il DB
 */
public class CorsiLaureaController {
	
	/**
	 *  @author Amenta Stefano, Moroni Paolo 
	 * Metodo per la cancellazioen di un corso materia dal database
	 * @param conn connessione al DB
	 * @param idCorso da cancellare
	 */
	public static void deleteCorsoMateria(Connection conn, long idCorso) {
		try {
		String query = "delete from \"Courses\" where \"id\" = '" + idCorso + "';";
		Statement st = conn.createStatement();
        st.executeUpdate(query); 
		} catch(Exception ex) {
			
		}
	}

	
	/**
	 * Metodo che recupera dal database la mail degli studenti iscritti ad un corso e li mette in una lista
	 * @param conn connessione al DB
	 * @param idCorso di cui ottenere la lista studenti
	 * @return lista studenti
	 */
	public static ArrayList<String> getStudentiEmailByCorso(Connection conn, long idCorso) {
		try {
			ArrayList<String> listaEmails = new ArrayList<String>();
			String query = "Select * from \"StudVCourse\" JOIN \"Userdata\" "
					+ "ON \"StudVCourse\".\"student\" = \"Userdata\".\"userid\""
					+ " Where \"course\" = " + idCorso;
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(query);
			String mail;
			while (rs.next()) {
				mail = rs.getString("Email");
				listaEmails.add(mail);
			}
			return listaEmails;
		} catch (Exception e) {
			System.out.println("Errore getStudentiByCorso");
		}
		return new ArrayList<String>();
	}

	
	/**
	 * Metodo che restituisce i corsi relativi ad un docente sotto forma di lista
	 * @param conn  conn connessione al DB
	 * @param idDocente passato come parametro
	 * @return una lista di corsi
	 */
	public static ArrayList<Course> getCorsiByDocente(Connection conn, long idDocente) {
		try {
			ArrayList<Course> lista = new ArrayList<Course>();
			String query = "SELECT * FROM \"Courses\" JOIN \"TeachVCourse\" ON \"Courses\".\"ID\" = \"TeachVCourse\".\"course\""
					+ " WHERE \"TeachVCourse\".\"teacher\" = " + idDocente;
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(query);
			Course corso = null;
			while (rs.next()) {
				corso = new Course();
				corso.Id = rs.getLong("id");
				corso.degreeCourse = rs.getLong("degreeCourse");
				corso.name = rs.getString("name");
				corso.activationYear = rs.getInt("activationyear");
				corso.description = rs.getString("description");
				lista.add(corso);
			}
		} catch (Exception e) {
			System.out.println("Err GetCorsiByDocente");
		}
		return new ArrayList<Course>();
	}
	
	/**
	 * Metodo per l'invio di una mail a tutti gli utenti iscritti ad un corso
	 * @param conn
	 * @param idCorso
	 * @param oggetto
	 * @param messaggio
	 */
	public static void sendNewsLetterToAllStudents(Connection conn, long idCorso, String oggetto, String messaggio) {
		try {
			ArrayList<String> emails = getStudentiEmailByCorso(conn, idCorso);
			for (String account : emails) {
				Server.Utilities.EmailSender.send_uninsubria_email(account, oggetto, messaggio);
			}
		} catch (Exception e) {

		}
	}

	/**
	 * Metodo per inviare una mail ad un gruppo specifico di studenti iscritti ad un corso
	 * @param conn
	 * @param idCorso
	 * @param oggetto
	 * @param messaggio
	 * @param studenti
	 */
	public static void sendNewsLetterToSomeStudents(Connection conn, long idCorso, String oggetto, String messaggio, 
			ArrayList<String> emailStudenti) {
		try {
			for (String account : emailStudenti) {
				Server.Utilities.EmailSender.send_uninsubria_email(account, oggetto, messaggio);
			}
		} catch (Exception e) {

		}
	}
	
	/**
	 * Server 
	 * Restituisco tutti i corsi di laurea presenti sul db e quindi relativi a tutti i dipartimenti 
	 * @param conn
	 * @return
	 */
	public static ArrayList<DegreeCourse> getAllCorsiLaurea(Connection conn) {
		ArrayList<DegreeCourse> listaCorsiLaurea = new ArrayList<DegreeCourse>();
		DegreeCourse newCorso = null;
		try {
			String Query = "Select * From \"DegCourses\"";
			Statement s = conn.createStatement();
			ResultSet rs = s.executeQuery(Query);
			while (rs.next()) {
				newCorso = new DegreeCourse();
				newCorso.Id = rs.getLong(1);
				newCorso.name = rs.getString(2);
				newCorso.department = rs.getLong(3);
				listaCorsiLaurea.add(newCorso);
			}
			return listaCorsiLaurea;
		} catch (Exception e) {
			System.out.println("Errore query per reperire tutti i corsi" 
					+ "\n e.getMessage() " + e.getMessage());
			System.out.print("e.printStackTrace(): "); e.printStackTrace();
		}

		
		return null;
	}

	
	/**
	 * Metodo che crea sul database un nuovo corso di laurea
	 * @param conn  conn connessione al DB
	 * @param nuovoCorso da caricare sul db
	 */
	public static void nuovoCorsoLaurea(Connection conn, DegreeCourse nuovoCorso) {
		try {
			String query = "INSERT INTO \"Departments\" (\"name\", \"department\") VALUES (\'" + nuovoCorso.name
					+ "\'," + nuovoCorso.department + ")";
			Statement s = conn.createStatement();
			s.executeQuery(query);
		} catch (Exception e) {
			System.out.println("Errore inserimento nuovo corso di laurea");
		}
	}

	
	/**
	 * Metodo che restituisce una lista di tutti i dipartimenti presenti sul database
	 * @param conn conn connessione al DB
	 * @return ilsta di tutti i dipartimenti
	 */
	public static ArrayList<Department> getAllDipartimenti(Connection conn) {
		ArrayList<Department> listaDipartimenti = new ArrayList<Department>();
		Department newDipartimento = null;
		try {
			String Query = "Select * From \"Departments\"";
			Statement s = conn.createStatement();
			ResultSet rs = s.executeQuery(Query);
			while (rs.next()) {
				newDipartimento = new Department();
				newDipartimento.Id = rs.getLong(1);
				newDipartimento.name = rs.getString(2);
				listaDipartimenti.add(newDipartimento);
			}
			return listaDipartimenti;
		} catch (Exception e) {
			System.out.println("Errore query per reperire tutti i dipartimenti");
		}

		return null;
	}

	
	/**
	 * Metodo che consente di creare un nuovo dipartimento
	 * @param conn  conn connessione al DB
	 * @param nuovoDipartimento da creare sul db
	 */
	public static void nuovoDipartimento(Connection conn, Department nuovoDipartimento) {
		try {
			String query = "INSERT INTO \"Departments\" (\"name\") VALUES (\'" + nuovoDipartimento.name + "\')";
			Statement s = conn.createStatement();
			s.executeQuery(query);
		} catch (Exception e) {
			System.out.println("Errore inserimento nuovo department");
		}
	}

	/**
	 * Metodo per ottenere tutti i corsi materia presenti sul db
	 * @param conn connessione al DB
	 * @return lista dei corsi materia
	 */
	public static ArrayList<Course> getAllCorsiMateria(Connection conn) {
		ArrayList<Course> listaCorsiMateria = new ArrayList<Course>();
		Course corsoMateria = null;
		String query = "Select * From \"Courses\"";
		try {
			Statement s = conn.createStatement();
			ResultSet rs = s.executeQuery(query);
			while (rs.next()) {
				corsoMateria = new Course();
				corsoMateria.Id = rs.getLong("id");
				corsoMateria.name = rs.getString("name");
				corsoMateria.activationYear = rs.getInt("activationyear");
				corsoMateria.description = rs.getString("description");
				// prendo l'id del corso di laurea relativo alla materia 
				try {
				corsoMateria.degreeCourse = Cache.getCorsoLaurea(rs.getLong("degreecourse")).Id;
				} catch (Exception ex) {
				}
				listaCorsiMateria.add(corsoMateria);
			}
			return listaCorsiMateria;
		} catch (Exception e) {

		}

		return null;
	}

	
	/**
	 * Metodo per la creazione sul db di un nuovo corso materia
	 * @param conn connessione al DB
	 * @param nuovoCorso da creare
	 */
	public static void nuovoCorsoMateria(Connection conn, Course nuovoCorso) {
		try {
			long Id = AnagraficaController.getMaxId(conn, "\"Courses\"", "id");
			String query = "INSERT INTO \"Courses\" (\"id\",\"degreecourse\", \"name\", \"activationyear\", \"description\")"
					+ " VALUES (" + ++Id + ","+ nuovoCorso.degreeCourse + ", '" + nuovoCorso.name + "', " + nuovoCorso.activationYear
					+ ", '" + nuovoCorso.description + "');";
			Statement s = conn.createStatement();
			s.executeUpdate(query);
		} catch (Exception e) {
			System.out.println("Errore inserimento nuovo corso materia");
		}
	}

	/**
	 * Metodo che consente di inserire sul database il numero di secondi trascorsi dall'utente su un corso
	 * @param conn connessione al DB
	 * @param idCorso su cui ha trascorso i secondi l'utente
	 * @param secondiUpdate secodi passati sul corso
	 */
	public static void updateSecondsUser(Connection conn, long idCorso, long secondiUpdate) {
		try {
			String query = "UPDATE \"StudVCourse\" SET \"onlineseconds\" = '" + secondiUpdate
					+ "' WHERE \"id\" =" + idCorso;
			Statement st = conn.createStatement();
			st.executeUpdate(query);
		} catch (Exception e) {
			System.out.println("Errore update seconds number salvata su db");
		}
	}

	
	/**
	 * Metodo per caricare sul database un nuovo corso materia, la cui anagrafica è contenuta nel model passato come parametro
	 * @param conn connessione al DB
	 * @param corso model contenente l'nagrafica del corso da caricare
	 */
	public static void updateCorsoMateria(Connection conn, Course corso) {
		try {
			String query = "UPDATE \"Courses\" SET \"name\" = '" + corso.name + "'" + ", \"description\" = '"
					+ corso.description + "'" + " WHERE \"id\" = " + corso.Id + "; "; 
			Statement st = conn.createStatement();
			st.executeUpdate(query);
			Cache.updateCorsiMateria(conn);//
		} catch (Exception e) {
			System.out.println("Errore UpdateCorsoMateria salvata su db");
			e.printStackTrace();
		}
	}

	/**
	 * Metodo per ottenere la description di un corso presente sul database
	 * @param conn connessione al DB
	 * @param idCorso di cui ottenere la descrizione
	 * @return
	 */
	public static String courseDescription(Connection conn, long idCorso) {
		String query= "Select \"description\" from \"Courses\" where \"id\" = '" + idCorso + "';";
		String result = null; 
		try {
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery(query);	
		while (rs.next()) {
			result = rs.getString(1);
		}
		} catch (Exception ex) {
			System.out.println("e.printStackTrace()");
			ex.printStackTrace();
		}
		return result;
		
	}
	
	/**
	 * Metodo per ottenere un conteggo degli studenti ed il relativo tempo di connessione che consentono di 
	 * calcolare il tempo medio di connessione
	 * @param conn connessione al DB
	 * @param idCorso dic ui calcolare il tempo medio
	 * @return
	 */
	public static double tempoMedioConnessione(Connection conn, long idCorso) {
		try {
			double media = 0;
			String queryStud = "SELECT Count(*) FROM \"Students\" JOIN \"StudVCourse\""
					+ " ON \"Students\".\"userid\" = \"StudVCourse\".\"student\" WHERE \"StudVCourse\".\"degreecourse\" = "
					+ idCorso;
			String querySec = "SELECT \"onlineseconds\" FROM \"StudVCourse\" WHERE \"StudVCourse\".\"degreecourse\" = "
					+ idCorso;
			
			int nStud = 1;
			double seconds = 0;
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(queryStud);
			while (rs.next()) {
				nStud = rs.getInt(1);
			}
			rs = st.executeQuery(querySec);
			while (rs.next()) {
				seconds +=  rs.getLong(1);
			}
			if (nStud != 0) {
				media = seconds / nStud;
			}
			return media;
		} catch (Exception e) {
			System.out.println("Errore calcolo media");
		}
		return -1;
	}

	
	/**
	 * Metodo che permette di caricare sul db l'accesso effettuato da un utente ad un determinato corso
	 * @param conn
	 * @param idCorso
	 * @param idUser
	 */
	public static void updateAccessCount(Connection conn, long idCorso, long idUser) {
		try {
			
			Date now = new Date();
			SimpleDateFormat format1 = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss"); 
			String date1 = format1.format(now); 
			long id = AnagraficaController.getMaxId(conn, "\"CourseLogs\"", "id");
			String query = "INSERT INTO \"CourseLogs\" (\"id\", \"user\", \"course\",\"date\") VALUES " + "(" + ++id +","+ idUser
					+ ", " + idCorso + ", '"+date1+"')";
			Statement st = conn.createStatement();
			st.executeUpdate(query);
		} catch (Exception e) {
			System.out.println("Errore update Access Count");
			System.out.println("e.printStackTrace()");
			e.printStackTrace();
		}
	}

	
	/**
	 * Metodo che permette di calcolare il numero di accessi effettuati dagli utenti nell'arco di un periodo
	 * @param conn connessione al DB
	 * @param idCorso di cui conoscere il numero di accessi
	 * @param start momento iniziale
	 * @param end momento finale
	 * @return numero di accessi in un arco temporale
	 */
	public static int getAccessByPeriod(Connection conn, long idCorso, Date start, Date end) {
		try {
			
			SimpleDateFormat format1 = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
			String date1 = format1.format(start); 
			String date2 = format1.format(end); 
			String query = "SELECT Count(*) FROM \"CourseLogs\" WHERE " + "\"date\" >= '" + date1 + "'"
					+ "  AND \"date\" <=  '" + date2 + "'" + " AND \"course\" = " + idCorso;
			int cont = 0;
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(query);
			while (rs.next()) {
				cont = rs.getInt(1);
			}
			return cont;
		} catch (Exception e) {

		}
		return 0;
	}

	
	/**
	 * Metodo che consente di importare il contenuto di 3 file CSV all'interno del database. L'insieme dei 3 file 
	 * rappresntano i corsi.
	 * @param conn connessione al DB
	 * @param corsiLaurea prelevato da CSV
	 * @param corsiMateria prelevato da CSV
	 * @param Dipartimenti prelevato da CSV
	 */
	public static void importCSV(Connection conn, ArrayList<DegreeCourse> corsiLaurea, ArrayList<Course> corsiMateria, ArrayList<Department> Dipartimenti ) {
		try {
			String queryMaterie = null;
			String queryCorsi = null; 
			String queryDipartimento = null;
			Statement s = null;
			
			try {
			for(Department dipartimento: Dipartimenti) {
				queryDipartimento = "insert into \"Departments\" (\"id\",\"name\") values"
						+ " ('" + dipartimento.Id + "','" + dipartimento.name + "');";
				 s = conn.createStatement();
				s.executeUpdate(queryDipartimento);
			}} catch (Exception ex) {System.out.println("Dipartimento già esistente");}
			
			try {
			for(DegreeCourse corsoLaurea : corsiLaurea) {
				queryCorsi = "insert into \"DegCourses\" (\"id\",\"name\",\"department\") values"
						+ " ('" + corsoLaurea.Id + "','" + corsoLaurea.name + "','" + corsoLaurea.department + "');";
				s = conn.createStatement();
				s.executeUpdate(queryCorsi);
			}} catch(Exception ex) {System.out.println("Corso di laurea già esistente");}
			
			try {
			for(Course corsoMateria: corsiMateria) {
				queryMaterie = "Insert into \"Courses\" (\"id\",\"degreecourse\",\"name\",\"activationyear\",\"description\") "
						+ "values ('" + corsoMateria.Id + "','" + corsoMateria.degreeCourse + "','" + corsoMateria.name + "','" 
						+ corsoMateria.activationYear + "','" + corsoMateria.description + "');";
				s = conn.createStatement();
				s.executeUpdate(queryMaterie);
			}} catch(Exception ex) { System.out.println("Corso materia già esistente");}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}


}
