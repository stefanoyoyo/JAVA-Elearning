package Server.Database.Utenti;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Random;

import Common.Logger;
import Common.DBType.Userdatas;



/**
 *  @author Amenta Stefano, Moroni Paolo 
 * Classe per la gestione dell'anagrafiche e le operazioni a loro relative su DB
 */
public class AnagraficaController {

	/**
	 * Metodo per generare un codice causale da inviare all'utent ein fase di registrazione
	 * @return codice casuale
	 */
	public static long genCodAtt() {
		Random rand = new Random();
		final int Max = 999999;
		final int Min = 100000;
		long randomNum = rand.nextInt((Max - Min) + 1) + Min;
		return randomNum;
	}

	
	/**
	 * Metodo che consente di bloccare un tentativo a seguito di più di 10 tentativi di accesso
	 * @param conn connessione al DB
	 * @param Email di cui bloccare il profilo
	 */
	public static void bloccaAccount(Connection conn, String Email) {
		String subject = "Account bloccato SimpleELearning";
		long codAtt = genCodAtt();
		
		// INVIO MAIL DI ACCOUNT TEMPORANEAMENTE BLOCCATO
		String tempPSW = genCodAtt() + "";
		String body = "Account bloccato...\nPassword Temporanea = " + tempPSW + ";\nCodice d'attivazione:" + codAtt
				+ "\n";
		Server.Utilities.EmailSender.send_uninsubria_email(Email, subject, body);
		
		// SCRIVO SUL DB CHE L'ACCOUNT E' STATO BLOCCATO
		String query = "UPDATE \"Userdata\" SET (\"trusted\",\"activationcode\", \"password\",\"islocked\") =  (false, "
				+ codAtt + ", '" + tempPSW + "','" + true + "') WHERE \"email\" LIKE '" + Email + "'";
		try {
			Statement st = conn.createStatement();
			st.executeUpdate(query);
			Server.Utilities.EmailSender.send_uninsubria_email(Email, "Blocco Account SimpleELearning",
					"Account bloccato ... codice di sblocco:" + codAtt);
		} catch (Exception e) {
			System.out.println("Errore nel blocco dell'account");
		}

	}
	
	/**
	 * Metodo per chiedere al database quale è il numero di login corrente per l'utente richiesto
	 * @param conn connessione al DB
	 * @param matricola
	 * @return
	 */
	public static long getNumeroTentativiLogin(Connection conn, String email) {
		String query = "select \"loginattempts\" from \"Userdata\" where \"email\" = '" + email + "'";
		try {
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery(query);
		long numeroTentativiLogin = 0;
		while (rs.next()) {
			numeroTentativiLogin = rs.getLong(1);
		}
		return numeroTentativiLogin;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return 0;
		
	} 
	
	
	/**
	 * Metodo che consente di portare a 0 il numero di tentativi di login compiuti dall'utente. 
	 * Da usare per sbloccare l'utente
	 * @param conn connessione al DB
	 * @param email da sbloccare 
	 * @return l'attuale numero di login, che sarà sempre 0
	 */
	public static long setZeroNumeroTentativiLogin(Connection conn, String email) {
		long getNumeroTentativiLogin = getNumeroTentativiLogin(conn, email);
		String query = "update \"Userdata\" set \"loginattempts\" = '" + 0 + "'"
				+ " where \"email\" = '" + email + "';" ;
		try {
			Statement st = conn.createStatement();
			st.executeUpdate(query);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		return getNumeroTentativiLogin;
	}
	
	/**
	 * Metodo per scrivere su DB il corretto numero di accessi. 
	 * @return il numero corrente di accessi presente sul DB
	 */
	public static long incNumeroTentativiLogin(Connection conn, String email) {
		long getNumeroTentativiLogin = getNumeroTentativiLogin(conn, email);
		String query = "update \"Userdata\" set \"loginattempts\" = '" + ++getNumeroTentativiLogin + "'"
				+ " where \"email\" = '" + email + "';" ;
		try {
			Statement st = conn.createStatement();
			st.executeUpdate(query);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		return getNumeroTentativiLogin;
	}
	
	/**
	 * Metodo che consente di ottenere l'indirizzo email dell'utente di cui è specificata la matricola come parametro
	 * @param conn connessione al DB
	 * @param Matricola dell'utente di cui ottenere la mail
	 * @return mail dell'utente
	 */
	public static String getEmailByMatricola(Connection conn, long Matricola) {
		String query = "select \"email\" from \"Userdata\" where \"userid\" = '" + Matricola + "';";
		try {
			String email = null;
			Statement st = conn.createStatement();
			st.executeUpdate(query);
			ResultSet rs = st.executeQuery(query);
			while (rs.next()) {
				email = rs.getString(1);
			}
			return email;
		} catch(Exception ex) {
			
		}
		return null;
	}
	
	
	/**
	 * Metodo per sblocare un utente che è stato bloccato in quanto ha effettuato più di 10 tentativi di loign errati
	 * @param conn connessione al DB
	 * @param Matricola dell'utente da sbloccare
	 */
	public static void unlockUser(Connection conn, long Matricola) {
		String email = getEmailByMatricola(conn, Matricola);
		String query = "update \"Userdata\" set(\"trusted\",\"islocked\") = ('" + false + "','" + false + "')"
				+ " where \"userid\" = '" + Matricola + "'" ;
		try {
			Statement st = conn.createStatement();
			st.executeUpdate(query);
			Server.Utilities.EmailSender.send_uninsubria_email(email, "Sblocco Account SimpleELearning",
					"Account sbloccato" );
		} catch(Exception ex) {
			
		}
	}

	/**
	 * Metodo che consente di cambiare la password dell'utente specificato nel parametro
	 * @param conn connessione al DB
	 * @param Matricola utente di cui cambiare la password
	 * @param newPsw password da sostituire a quella vecchia
	 */
	public static void cambiaPsw(Connection conn, long Matricola, String newPsw) {
		String query = "UPDATE \"Userdata\" SET \"password\" = '" + newPsw + "' WHERE \"userid\" = " + Matricola;
		try {
			Statement st = conn.createStatement();
			st.executeQuery(query);
		} catch (Exception e) {
			System.out.println("Errore nella modifica della colonna trusted");
		}
	}
	
	/**
	 * Metodo che permette la modifica di un'anagrafica prelevando dal model i dati aggiornati
	 * @param conn
	 * @param anagAlterata
	 */
	public static void modificaAnagrafica(Connection conn, Userdatas anagAlterata) {
		try {
			String query = "update \"Userdata\" set (\"name\", \"surname\", \"email\", \"password\"  ) = (" + 
					"'" + anagAlterata.name + "','" + anagAlterata.surname + "','" + anagAlterata.Email + "','" + anagAlterata.Password + "');" ;
			Statement st = conn.createStatement();
			st.executeUpdate(query);
		} catch (Exception e) {
			Logger.WriteError(e, "AnagraficaController", "ModificaAnagrafica");
		}
	}
	
	
	/**
	 * Metodo che consente di memorizzare che un utente ah superato il processo di registrazione
	 * @param conn connessione al DB
	 * @param Matricola che ha superato il processo di registrazione
	 */
	public static void verificaTrue(Connection conn, long Matricola) {
		String query = "UPDATE \"Userdata\" SET \"trusted\" = '" + true + "' WHERE \"userid\" = '" + Matricola + "'";
		try {
			Statement st = conn.createStatement();
			st.executeUpdate(query);
		} catch (Exception e) {
			System.out.println("Errore nella modifica della colonna trusted");
		}
	}
	
	/**
	 * Metodo che consente di verificare se il codice di attivazione inserito dall'utente corrisponde con quello che 
	 * dobrebbe aver ricevuto via mail e che è presente sul db
	 * @param conn connessione al DB
	 * @param Matricola dell'utente di cui bisogna verificare il codice di attivazione
	 * @param CodAtt codice di attivazione
	 * @return vero se l'operazione è andata a buon fine, 0 otherwise
	 */
	public static boolean verificaCod(Connection conn, long Matricola, long CodAtt) {
		String query = "SELECT \"activationcode\" FROM \"Userdata\" WHERE \"userid\" =" + Matricola;
		try {
			long codAttDB = -1;
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(query);
			while (rs.next()) {
				codAttDB = rs.getLong(1);
			}
			if (codAttDB == CodAtt) {
				verificaTrue(conn, Matricola);
				return true;
			}
		} catch (Exception e) {
			System.out.println(e);
			System.out.println("Errore in verifica Cod");
		}
		return false;
	}
	
	
	/**
	 * Metodo che consente di aggiornare l'anagrafica, inserendo sul DB quella dell'utente passato come parametro
	 * @param conn connessione al DB
	 * @param mod model anagrafica presa dalla vista e da caricare tramite il controller sul DB
	 */
	public static void updateAnagrafica(Connection conn, Userdatas mod) {
		try {
			System.out.println(mod.userID+" userID");
			String query = "UPDATE \"Userdata\" SET "
					+ "\"email\" =  '"+mod.Email+"', "
					+ "\"name\" = '"+mod.name+"' , "
					+ "\"surname\" = '"+mod.surname+"' "
					+ " WHERE \"userid\" =  "+mod.userID;
			Statement st = conn.createStatement();//
			st.executeUpdate(query);
		} catch (Exception e) {
			System.out.println(e);
			System.out.println("Errore UpdateAnagrafica");
		}
	}
	
	/**
	 * Metodo che consente ad un utente di cambiare la password
	 * @param conn connessione al DB
	 * @param email che richiede il cambiamento
	 * @param newPsw nuova password da associare alla mail dell'utente
	 * @return true se l'operazione è avvenuta con successo, false otherwise
	 */
	public static boolean cambiaPsw(Connection conn, String email, String newPsw) {
		String query = "UPDATE \"Userdata\" SET \"password\" = '" + newPsw + "' WHERE \"email\" LIKE '" + email
				+ "'";
		try {
			Statement st = conn.createStatement();
			st.executeUpdate(query);
		} catch (Exception e) {
			System.out.println(e);
			System.out.println("Errore in verifica Cod");
		}
		return false;
	}

	/**
	 * AnagraficaController
	 * Metodo per l'ottenimento del Id maggiore attualmente in una colonna
	 * @param conn connessione al DB
	 * @param TableName tabella di cui avere l'id più altro
	 * @param ColumnIdName colonna contenente l'id 
	 * @return l'id maggiore della tabella specificata alla colonna specificata
	 */
	public static long getMaxId(Connection conn, String TableName, String ColumnIdName) {
		String query = "select max(" + ColumnIdName + ") from " + TableName + ";";
		try { 
		Statement s = conn.createStatement();
		Userdatas anag = null;
		ResultSet rs = s.executeQuery(query);
		while (rs.next()) {
			anag = new Userdatas();
			anag.userID = rs.getLong(1);
		}
		return anag.userID;
		} catch (Exception ex) {
			System.out.println("Errore ottenimento anagrafica");
		}
		
		return -1;
	}
	
	/**
	 * Metodo per chiedere al DB una lista di utenti 
	 * @param conn  connessione al DB
	 * @return lista di tutti gli utenti
	 */
	public static ArrayList<Userdatas> getAllUsersList(Connection conn) {
		String query = "select * from \"Userdata\"";
		ArrayList<Userdatas> allUsersList = new ArrayList<Userdatas>();
		Userdatas anag = null;
		try {
			Statement st = conn.createStatement();
			st.executeUpdate(query);
			ResultSet rs = st.executeQuery(query);
			while (rs.next()) {
				anag = new Userdatas();
				anag.userID = rs.getLong(1);
				anag.name = rs.getString(2);
				anag.surname = rs.getString(3);
				anag.Email = rs.getString(4);
				anag.activationCode = rs.getString(5);
				anag.Password = rs.getString(6);
				anag.loginattempts = rs.getLong(7);
				anag.trusted = rs.getBoolean(8);
				anag.isLocked = rs.getBoolean(9);
				allUsersList.add(anag);
			}
			return allUsersList;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
		
	}
	
	/**
	 * Metodo che permette l'eliminazione di un utente dalla tabella anagrafiche.
	 * Essendo anagrafica una tabella padre, l'eliminazione si propaghera poi su tutte le tabelle figlie
	 * @param conn connessione al DB
	 * @param matricola utente da cancellare
	 */
	public static void deleteUser(Connection conn, long matricola) {
		String query = "delete from \"Userdata\" where \"userid\" = '" + matricola + "';";
		try { 
			Statement st = conn.createStatement();
	        st.executeUpdate(query); 
		} catch (Exception ex) {
			System.out.println("Errore ottenimento anagrafica");
		}
	}
}
