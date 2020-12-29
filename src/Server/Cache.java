package Server;

import java.sql.Connection;
import java.util.ArrayList;
import Server.Database.Corsi.*;
import Common.DBType.*;

/**
 *  @author Amenta Stefano, Moroni Paolo 
 * Classe che contiene le liste utilizzate per fare da cache per i corsi e i dip
 *
 */
public class Cache {
	private static ArrayList<DegreeCourse> CorsiLaurea = null;
	private static ArrayList<Department> Dipartimenti = null;
	private static ArrayList<Course> CorsiMateria = null;

	/**
	 * Metodo he permette la creazione di un nuovo corso
	 * @param conn
	 * @param nuovoCorso
	 */
	public static void inserisciNuovoCorsoMateria(Connection conn, Course nuovoCorso) {
		CorsiLaureaController.nuovoCorsoMateria(conn, nuovoCorso);
		updateCorsiMateria(conn);
	}

	
	/**
	 * Metodo per restituire la lista dei corsi materia presente in cache, dopo averla prima 
	 * sincronizzata coi valori presenti sul database.
	 * @param conn
	 * @return
	 */
	public static ArrayList<Course> getAllCorsiMateria(Connection conn){
		updateCorsiMateria(conn);//
		return CorsiMateria;
	}
	
	
	/**
	 * Metodo che interroga la cache per ottenere un corso materia dato un corso di laurea
	 * @param IdCorsoLaurea id corso di laurea di cui cercare le materie
	 * @return lista dei corsi materia
	 */
	public static ArrayList<Course> getCorsiMateriaByLaurea(long IdCorsoLaurea) {
		ArrayList<Course> lista = new ArrayList<Course>();
		for (Course d : CorsiMateria) {
			if (d.degreeCourse == IdCorsoLaurea) {
				lista.add(d);
			}
		}
		return lista;
	}
	
	
	/**
	 * Metodo che cerca all'interno dei corsi materia salvati in cache un corso materia specificato come 
	 * parametro
	 * @param index
	 * @return
	 */
	public static Course getCorsoMateria(long index) {
		for (Course d : CorsiMateria) {
			if (d.Id == index) {
				return d;
			}
		}
		return null;
	}

	
	/**
	 * Metodo per sincronizzare la cache con i corsi materia presenti sul database
	 * @param conn
	 */
	public static void updateCorsiMateria(Connection conn) {
		if (CorsiMateria == null) {
			CorsiMateria = new ArrayList<Course>();
		}
		ArrayList<Course> risultatoQuery = CorsiLaureaController.getAllCorsiMateria(conn);
		if (risultatoQuery != null) {
			synchronized (CorsiMateria) {
				CorsiMateria = risultatoQuery;
			}
		}
	}

	/**
	 * Metodo per ottenere i corsi presenti in cache
	 * @return
	 */
	public static ArrayList<DegreeCourse> getCorsi() {
		return CorsiLaurea;
	}

	
	/**
	 * Metodo che interroga la cache per prelevare i corsi di laurea dato il dipartimento passato 
	 * come parametro
	 * @param idDip
	 * @return
	 */
	public static ArrayList<DegreeCourse> getCorsiLaureaByDip(long idDip) {
		ArrayList<DegreeCourse> lista = new ArrayList<DegreeCourse>();
		for (DegreeCourse c : CorsiLaurea) {
			if (c.department == idDip) {
				lista.add(c);
			}
		}
		return lista;
	}

	
	/**
	 * Metodo per aggiornare la cache coi corsi di laurea presenti sul db 
	 * @param conn
	 */
	public static void updateCorsiLaurea(Connection conn) {
		if (CorsiLaurea == null) {
			CorsiLaurea = new ArrayList<DegreeCourse>();
		}
		ArrayList<DegreeCourse> risultatoQuery = CorsiLaureaController.getAllCorsiLaurea(conn);
		if (risultatoQuery != null) {
			synchronized (CorsiLaurea) {
				CorsiLaurea = risultatoQuery;
			}
		}
	}

	
	/**
	 * Metodo per la creazione di un nuovo vorso di laurea. Provvede inoltre all'aggiornamento dei corsi 
	 * di laurea presenti in cache
	 * @param conn
	 * @param nuovoCorso
	 */
	public static void inserisciNuovoCorsoLaurea(Connection conn, DegreeCourse nuovoCorso) {
		CorsiLaureaController.nuovoCorsoLaurea(conn, nuovoCorso);
		updateCorsiLaurea(conn);
	}

	
	/**
	 * 
	 * @param id corso dell'utente loggato
	 * @return
	 */
	public static DegreeCourse getCorsoLaurea(long idCorso ) {
		try {
		for (DegreeCourse c : CorsiLaurea) {
			if (c.Id == idCorso) {
				return c;
			}
		}
		} catch (Exception ex) {
			System.out.println("DEBUG: ex.getMessage() " + ex.getMessage() + 
					"\nNon esistono corsi di laurea");
		}
		return null;
	}

	
	/**
	 * Metodo che restituisce una lista dei dipartimenti
	 * @return
	 */
	public static ArrayList<Department> getDipartimenti() {
		return Dipartimenti;
	}

	
	/**
	 * Metodo che ritorna l'Id del dipartimento
	 * @param Id del dipartimento da cercare
	 * @return l'id del dipartimento trovato
	 */
	public static long getDipartimento(long Id) {
		for (Department d : Dipartimenti) {
			if (d.Id == Id) {
				return d.Id;
			}
		}
		return -1;
	}
	
	
	/**
	 * Metodo per la sincronizzazione dei dipartimenti contenuti in cache con quelli presenti sul database
	 * @param conn
	 */
	public static void updateDipartimenti(Connection conn) {
		// Chiamata al db.
		if (Dipartimenti == null) {
			Dipartimenti = new ArrayList<Department>();
		}
		ArrayList<Department> risultatoQuery = CorsiLaureaController.getAllDipartimenti(conn);
		if (risultatoQuery != null) {
			synchronized (Dipartimenti) {
				Dipartimenti = risultatoQuery;
			}
		}
	}
	
	/**
	 * Metodo per la crezione di un nuovo dipartimento
	 * @param conn 
	 * @param nuovoDipartimento
	 */
	public static void inserisciNuovoDipartimento(Connection conn, Department nuovoDipartimento) {
		CorsiLaureaController.nuovoDipartimento(conn, nuovoDipartimento);
		updateDipartimenti(conn);
	}

}