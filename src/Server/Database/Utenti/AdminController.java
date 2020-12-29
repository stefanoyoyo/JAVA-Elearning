package Server.Database.Utenti;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;
import Common.DBType.Admin;
import Common.DBType.Userdatas;
import Server.Database.DatabaseManager;

/**
 *  @author Amenta Stefano, Moroni Paolo 
 * Classe per la gestione degli admin e le operazioni a loro relative su DB
 */
public class AdminController extends AnagraficaController {

	/**
	 * Metodo che si occupa della gesione del login di un admin
	 * @param conn connessione al DB
	 * @param Email dell'admin
	 * @param Psw password dell'admin
	 * @return anagrafica presa dal DB dell'admin appena loggato
	 */
	public static boolean LoginAdmin(Connection conn) {
		@SuppressWarnings("resource")
		Scanner s = new Scanner(System.in);
		try {

			System.out.println("Inserisci Email");
			String Email = s.nextLine();
			System.out.println("Inserisci Password");
			String Psw = s.nextLine();

			String query = "Select Count(*) from \"Admins\" JOIN \"Userdata\" ON "
					+ "\"Admins\".\"userid\" = \"Userdata\".\"userid\"" + "WHERE \"email\" LIKE \'" + Email
					+ "\'AND \"password\" LIKE \'" + Psw + "\'";

			java.sql.Statement statement = conn.createStatement();
			ResultSet rs = statement.executeQuery(query);
			while (rs.next()) {
				int ris = rs.getInt(1);
				if (ris == 1) {
					return true;
				}
			}

		} catch (Exception e) {

			System.out.println("Errore LOGIN_USER ADMIN");
			System.out.println(e.toString());
		}
		return false;
	}
	
	
	
	/**
	 * Metodo per chiedere al database una lista di admin bloccati
	 * @return lista admin bloccati
	 */
	public static ArrayList<Admin> getLockedAdmin (Connection conn) {
		String query = "SELECT * FROM \"Admins\" JOIN \"Userdata\" ON \"Userdata\".\"userid\" = \"Admins\".\"userid\""
				+ " where \"islocked\" = '" + true + "';";
		ArrayList<Admin> listaDoc = null;
		try {			
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery(query);
		Admin newDoc = null;
		listaDoc = new ArrayList<Admin>();
		while (rs.next()) {
			newDoc = new Admin();
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
	 * Metodo che si occupa della gesione del login di un admin
	 * @param conn connessione al DB
	 * @param Email dell'admin
	 * @param Psw password dell'admin
	 * @return anagrafica presa dal DB dell'admin appena loggato
	 */
	public static Admin login(Connection conn, String Email, String Psw) {
		Admin admin = null;
		try {
			String query = "Select * from \"Admins\" JOIN \"Userdata\" ON "
					+ "\"Admins\".\"userid\" = \"Userdata\".\"userid\"" + "WHERE \"email\" LIKE \'" + Email
					+ "\'AND \"password\" LIKE \'" + Psw + "\'";

			java.sql.Statement statement = conn.createStatement();
			ResultSet rs = statement.executeQuery(query);
			while (rs.next()) {
				admin = new Admin();
				admin.userID = rs.getLong("userID");
				admin.Email = rs.getString("Email");
				admin.name = rs.getString("name");
				admin.Password = rs.getString("Password");
				admin.surname = rs.getString("surname");
				admin.trusted = rs.getBoolean("trusted");

			}
			return admin;
		}

		catch (Exception e) {

			System.out.println("Errore LOGIN_USER ADMIN");
			System.out.println(e.toString());
		}
		return null;
	}

	
	/**
	 * Metodo che consente la crezione di un nuovo utente di tipo admin
	 * @param conn connessione al DB
	 * @return true se è stato creato con sucesso, false otherwise
	 */
	public static boolean creaAdmin(Connection conn) {
		Scanner s = new Scanner(System.in);
		boolean adminCreato = false;
		Userdatas newAdmin = new Userdatas();
		
		// TODO: creare un metodo che controlla sul DB quale è stata l'ultima matricola assegnata e assegni all'admin una matricola
		long matr = getMaxId(conn,"\"Userdata\"", "userid");
		newAdmin.userID = ++matr;
		
		System.out.println("Inserire E-Mail");
		newAdmin.Email = s.nextLine();
		System.out.println("Inserire il surname");
		newAdmin.surname = s.nextLine();
		System.out.println("Inserire il name");
		newAdmin.name = s.nextLine();
		System.out.println("Inserire la Password");
		newAdmin.Password = s.nextLine();
		newAdmin.activationCode = DatabaseManager.generateAttCode();
		try {
			PreparedStatement st = conn.prepareStatement(
					"INSERT INTO \"Userdata\" (\"userid\", \"email\", \"name\", \"surname\", \"password\", \"activationcode\", \"trusted\") VALUES (?,?,?, ?, ?, ?, ?)");
			st.setLong(1, newAdmin.userID);
			st.setString(2, newAdmin.Email);
			st.setString(3, newAdmin.name);
			st.setString(4, newAdmin.surname);
			st.setString(5, newAdmin.Password);
			st.setLong(6, 123456);
			st.setBoolean(7, true);
			
			st.executeUpdate();
			st.close();
			java.sql.Statement st1 = conn.createStatement();
			ResultSet rs = st1.executeQuery(
					"SELECT \"userid\" FROM \"Userdata\" WHERE \"email\" LIKE \'" + newAdmin.Email + "\'");

			String matS = "-1";
			while (rs.next()) {
				matS = rs.getString(1);
			}
			System.out.println(matS);
			st1.close();
			long mat = Long.parseLong(matS);
			PreparedStatement stF = conn.prepareStatement("INSERT INTO \"Admins\" (\"userid\") VALUES (?)");
			stF.setLong(1, mat);
			stF.executeUpdate();
			stF.close();
			Server.Utilities.EmailSender.send_uninsubria_email(newAdmin.Email, "Registrazione SimpleElearning",
					"Operazione registrazione nuovo admin andata a buon fine");
			adminCreato = true;
		} catch (Exception e) {
			System.out.println("");
			System.out.println(e.getMessage());
			adminCreato = false;
		}
		s.close();
		return adminCreato;
		
	}
	
	/**
	 * Metodo che provvede alla creazione di un admin alla 
	 * @param conn connessione al DB
	 * @param newAdmin anagrafica presa dalla vita dell'utente da creare
	 */
	public static void creaAdmin(Connection conn, Admin newAdmin) {
		try {
			long codAtt = AnagraficaController.genCodAtt();
			boolean isZero = newAdmin.userID == 0? true:false;
			long matr = 0;
				if (isZero) {
				matr = getMaxId(conn,"\"Userdata\"", "userid");
				newAdmin.userID = ++matr;
				}	
			PreparedStatement st = conn.prepareStatement(
					"INSERT INTO \"Userdata\" (\"userid\", \"email\", \"name\", \"surname\", \"password\","
					+ " \"activationcode\", \"trusted\", \"islocked\", \"loginattempts\") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
			st.setLong(1, newAdmin.userID);
			st.setString(2, newAdmin.Email);
			st.setString(3, newAdmin.name);
			st.setString(4, newAdmin.surname);
			st.setString(5, newAdmin.Password);
			st.setLong(6, codAtt);
			st.setBoolean(7, newAdmin.trusted);
			st.setBoolean(8, newAdmin.isLocked);
			st.setLong(9, newAdmin.loginattempts);
			st.executeUpdate();
			st.close();
			java.sql.Statement st1 = conn.createStatement();
			ResultSet rs = st1.executeQuery(
					"SELECT \"userid\" FROM \"Userdata\" WHERE \"email\" LIKE \'" + newAdmin.Email + "\'");

			long mat = -1;
			while (rs.next()) {
				mat = rs.getLong(1);
			}
			st1.close();
			PreparedStatement stF = conn.prepareStatement("INSERT INTO \"Admins\" (\"userid\") VALUES (?)");
			stF.setLong(1, mat);
			stF.executeUpdate();
			stF.close();
			Server.Utilities.EmailSender.send_uninsubria_email(newAdmin.Email, "Registrazione SimpleElearning", "Operazione registrazione nuovo admin andata a buon fine");
		} catch (Exception e) {
			System.out.println("");
			System.out.println(e.getMessage());
		}
	}


	
	/**
	 * Metodo che controlla se esistono admin
	 * @param conn connessione al DB
	 * @return true se esiste amdin, false otherwise
	 */
	public static boolean isThereAdmin(Connection conn) {
		try {
			String query = "Select Count(*) from \"Admins\"";
			java.sql.Statement s = conn.createStatement();
			ResultSet r = s.executeQuery(query);
			while (r.next()) {
				String num = r.getString(1);
				if (Integer.parseInt(num) > 0) {
					s.close();
					return true;
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		return false;
	}
	
	/**
	 * Metodo che assegna un numero di matricola ad un admin 
	 * @param conn
	 * @return
	 */
	public static long setMatricola(Connection conn) {
		String query = "select max(userid) from \"Admins\"";
		long num = 0;
		java.sql.Statement s;
		try {
			s = conn.createStatement();
			ResultSet r = s.executeQuery(query);
			while (r.next()) {
			num = r.getLong(1);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return ++num;
	}



	public static ArrayList<Admin> getAllAdmin(Connection conn) {
		String query = "select * from \"Userdata\" JOIN \"Admins\" "
				+ "on \"Userdata\".\"userid\" = \"Admins\".\"userid\";";
		ArrayList<Admin> getAllAdmin = new ArrayList<Admin>(); 
		Admin admin = null;
		try {
			java.sql.Statement statement = conn.createStatement();
			ResultSet rs = statement.executeQuery(query);
			while (rs.next()) {
				admin = new Admin();
				admin.userID = rs.getLong("userid");
				admin.name = rs.getString("name");
				admin.surname = rs.getString("surname");
				admin.trusted = rs.getBoolean("trusted");
				getAllAdmin.add(admin);
			}
			return getAllAdmin;
			
		} catch(Exception ex) {
			
		}
		return null;
	}

}
