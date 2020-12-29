package Server.Database.Corsi;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JOptionPane;

import Common.DBType.Admin;
import Common.DBType.Folder;
import Common.DBType.DegreeCourse;
import Common.DBType.Course;
import Common.DBType.Department;
import Common.DBType.Teacher;
import Common.DBType.Document;
import Common.DBType.CourseFileSystem;
import Common.DBType.Student;
import Common.Enumerators.UserType;
import Common.Enumerators.FolderVisibility;
import Server.Cache;
import Server.Database.Utenti.AdminController;
import Server.Database.Utenti.AnagraficaController;
import Server.Database.Utenti.DocenteController;
import Server.Database.Utenti.StudentController;


/**
 *  @author Amenta Stefano, Moroni Paolo 
 * Classe che contiene tutti i metodi per il file system e le operazione a lui relativi per il DB
 */
public class FileSystemController {
	
	
	/**
	 * Metodo per tradurre la visibilità espressa come tipo enumerativo in stringa
	 * @param vis visibilita as tipo enumerativo
	 * @return stringa corrisponente
	 */
	private static FolderVisibility traduciVisibilità(String vis) {
		switch (vis) {
		case "0":
			return FolderVisibility.PRIVATE;
		case "1":
			return FolderVisibility.PUBLIC;
		default:
			return FolderVisibility.PUBLIC;
		}
	}

	
	/**
	 * Metodo per tradurre la visibilità espressa come tipo enumerativo in int
	 * @param vis visibilita as tipo enumerativo
	 * @return intero corrisponente
	 */
	private static int TraduciVisibilità(FolderVisibility vis) {
		switch (vis) {
		case PRIVATE:
			return 0;
		case PUBLIC:
		default:
			return 1;
		}
	}

	
	/**
	 * Metodo che si occupa di aggiornare il database quando viene effettuato un download, tenendone traccia. 
	 * Successivamente, iil conteggio del numero di download sarà fatto facendo la somma dei download secondo un criterio.
	 * @param conn connessione al DB
	 * @param docID di cui tenere traccia
	 * @param userID di cui tenene traccia
	 */
	public static void updateDownloadCont(Connection conn, long docID, long userID) {
		try {
			
			Date now = new Date();
			long Id = AnagraficaController.getMaxId(conn, "\"Downloads\"", "id");
			SimpleDateFormat format1 = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
			String date1 = format1.format(now); 
			String query = "INSERT INTO \"Downloads\" (\"id\",\"user\", \"document\", \"date\") "
					+ "VALUES ("+ ++Id + "," + userID
					+ "," + docID + ",'" + date1 + "');";
//			System.out.println("DEBUG: FileSystemController.updateDownloadCount: " + Id);
			
			// ottenere il numero dei download realtivo al file
			long ndownload = 0;
			query = "SELECT \"ndownload\" from \"Resources\" where \"id\" = '"+ docID + "';";
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(query);
			while (rs.next()) {
				ndownload = rs.getInt(1);
			}
			
			query = "UPDATE \"Resources\" SET  \"ndownload\" = '" + ++ndownload + "' where \"id\" = '"+ docID + "';";
			st = conn.createStatement();
			st.executeUpdate(query);
		} catch (Exception e) {
			System.out.println("Errore UpdateDownloadCont");
			System.out.println("e.printStackTrace()");
			e.printStackTrace();
		}
	}

	
	/**
	 * Metodo che provvede al cambiamento della visiblità di una seizone sul database
	 * @param conn connessione al DB
	 * @param idSez sezione di cui cambiare la visibilità 
	 * @param vis visiblità privata o pubblica
	 */
	public static void changeVisSez(Connection conn, long idSez, FolderVisibility vis) {
		try {
			int visI = TraduciVisibilità(vis);
			String query = "UPDATE \"Folders\" SET \"visibility\" = "+visI + 
					" WHERE \"folder\" ="+idSez;
			Statement st = conn.createStatement();
			st.executeUpdate(query);
		}
		catch(Exception e) {
			System.out.println(e);
		}
	}
	
	
	/**
	 * Metodo per il conteggio del numero di download all'interno di un arco temporale
	 * @param conn
	 * @param start
	 * @param end
	 * @return
	 */
	public static int contUtentiDownload(Connection conn, Date start, Date end) {
		try {
			int cont = 0;
			SimpleDateFormat format1 = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
			String date1 = format1.format(start); 
			String date2 = format1.format(end); 
			String query = "SELECT COUNT(*) FROM \"Downloads\" WHERE \"date\" > '" + date1
					+ "' AND \"date\" <= '" + date2 + "' GROUP BY \"user\"";
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(query);
			while (rs.next()) {
				cont += rs.getInt(1);
			}
			return cont;

		} catch (Exception e) {
			System.out.println("Err DOWNLOAD_USER_COUNT");
		}
		return 0;
	}
	
	
	
	/**
	 * Metodo per la conta dei download effettuati all'interno di un corso
	 * @param conn connessione al DB
	 * @param corsoID di cui conoscere il numero di download
	 * @return numero di download
	 */
	public static int contDownloadByCorso(Connection conn, long corsoID) {
		try {
			int cont = 0;
			String query = "SELECT COUNT(*) FROM \"Downloads\" JOIN \"Resources\" "
					+ "ON \"Downloads\".\"document\" = \"Resources\".\"id\" "
					+ "JOIN \"Folders\" ON \"Resources\".\"folder\" = \"Folders\".\"id\" WHERE \"Folders\".\"rootcourse\" = "
					+ corsoID;
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(query);
			while (rs.next()) {
				cont = rs.getInt(1);
			}
			return cont;
		} catch (Exception e) {
			System.out.println("ERR ContDownloadByCorso");
		}
		return 0;
	}

	
	/**
	 * Metodo per il salvataggio di un documento all'interno del database
	 * @param conn connessione al DB
	 * @param doc documento da caricare
	 */
	public static void salvaDoc(Connection conn, Document doc) {
		try {
			PreparedStatement st = conn.prepareStatement(
					"INSERT INTO \"Resources\" (\"id\", \"folder\", \"doctype\", \"file\", \"name\", \"description\", \"ndownload\")"
					+ " VALUES (?, ?, ?, ?, ?, ?, ?)");
			long Id = AnagraficaController.getMaxId(conn, "\"Resources\"", "id");
			st.setLong(1, ++Id );
			st.setLong(2, doc.idSez);
			st.setString(3, doc.docType);
			st.setBytes(4, doc.data);
			st.setString(5, doc.name);
			st.setString(6, doc.description);
			st.setLong(7, 0);
			st.executeUpdate();
		} catch (Exception e) {
			System.out.println("Errore nell'inserimento nel db del nuovo documento");
			e.printStackTrace();
		}
	}
	

	/**
	 * Metodo per l'inserimento di un elemento all'interno di una cartella
	 * @param conn
	 * @param sez
	 * @param idCorso
	 */
	public static void creaSez(Connection conn, Folder sez, long idCorso) {
		try {
			String query = "";
			long Id = AnagraficaController.getMaxId(conn, "\"Folders\"", "id");
			long Sezione = AnagraficaController.getMaxId(conn, "\"Folders\"", "folder");
			if (sez.parent == -1) {
				query = "INSERT INTO  \"Folders\" (\"id\", \"rootcourse\",\"folder\", \"name\", \"description\", \"visibility\") VALUES\r\n"
						+ "("+ ++Id + "," + idCorso + "," + ++Sezione + ",'" + sez.name + "','" + sez.description + "', "
						+ TraduciVisibilità(sez.visibility) + ")";
			} else {
				query = "INSERT INTO  \"Folders\" (\"id\",\"rootcourse\",\"folder\", \"name\", \"description\", \"visibility\") VALUES\r\n"
						+ "("+ ++Id + "," + idCorso + "," + ++Sezione + ",'" + sez.name + "', '" + sez.description
						+ "', " + TraduciVisibilità(sez.visibility) + ")";
			}
			Statement st = conn.createStatement();

			st.executeUpdate(query);
		} catch (Exception e) {
			System.out.println("Errore nuova sezione");
			e.printStackTrace();
			
		}
	}

	
	/**
	 * Metodo per ottenere una lista di cartelle padri 
	 * @param conn connessione al DB
	 * @param corsoID in cui cercare le cartelle padri
	 * @return lista delle cartelle padri
	 */
	private static ArrayList<Folder> getSezioniPadri(Connection conn, long corsoID) {
		ArrayList<Folder> lista = new ArrayList<Folder>();
		Folder sez = null;
		String query = "select * from \"Folders\" where \"rootcourse\" = " + corsoID;
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(query);
			while (rs.next()) {
				sez = new Folder();
				sez.idSez = rs.getLong(3);
				sez.parent = -1;
				sez.name = rs.getString(4);
				sez.description = rs.getString(5);
				rs.getString(6).toString();
				sez.visibility = traduciVisibilità(rs.getString(6));
				lista.add(sez);
			}
			
			return lista;
		} catch (Exception e) {
			System.out.println("Errore nell'ottenimento delle cartelle padre del corso " + corsoID);
			e.printStackTrace();
		}

		return null;
	}

	
	/**
	 * Metodo che va alla ricerca di sotto cartelle all'interno di una cartella 
	 * @param conn connessione al DB
	 * @param padre cartella padre da cui iniziare la ricerca delle sottocartelle
	 * @return
	 */
	private static ArrayList<Folder> getSottoCartelle(Connection conn, Folder padre) {
		Folder sez = null;
		String query = "select * from \"Folders\" Where \"folder\" = " + padre.idSez;
		ArrayList<Folder> sottoCartelle = new ArrayList<Folder>();
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(query);
			while (rs.next()) {
				sez = new Folder();
				sez.parent = padre.idSez;
				sez.document = cercaDocumento(conn, rs.getLong(1));
				sez.name = rs.getString("name");//
				sez.description = rs.getString("description");
				sottoCartelle.add(sez);
			}
			if (sottoCartelle.size() > 0) {
				// Vuol dire che ha trovato qualcosa...
				for (Folder sez1 : sottoCartelle) {
					sez1.sonFolders = getSottoCartelle(conn, sez1);
				}
			}
			return sottoCartelle;
		} catch (Exception e) {
			System.out.println("Errore nell'ottenimento della sottocartella");
		}

		return null;
	}

	
	/**
	 * Metodo che provvede alla ricerca di un documento all'interno del database
	 * @param conn connessione al DB
	 * @param sezID id sezione in cui andarlo a cercare
	 * @return
	 */
	private static ArrayList<Document> cercaDocumento(Connection conn, long sezID) {
		String query = "select \"id\", \"name\", \"doctype\" from \"Resources\" where \"folder\" = " + sezID;
		Document doc = null;
		ArrayList<Document> lista = new ArrayList<Document>();
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(query);
			while (rs.next()) {
				doc = new Document();
				doc.idDoc = rs.getLong("id");
				doc.idSez = sezID;
				doc.docType = rs.getString("doctype");
				doc.name = rs.getString("name");
				lista.add(doc);
			}
			return lista;
		} catch (Exception e) {
			System.out.println("Errore nella ricerca del documento");
		}

		return null;
	}
	
	
	
	/**
	 * Metodo perl'aggiornamento della description di una sezione
	 * @param conn connessione al DB
	 * @param id della sezione da aggiornare
	 * @param description nuova descrizione da aggiungere
	 */
	public static void updateSezDescription (Connection conn, long id, String description) {
		String query = "UPDATE \"Folders\" set \"description\" = '" + description + "' where \"id\" = '" + id + "';";
		Statement st;
		try {
			st = conn.createStatement();
			st.executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	} 
	
	
	/**
	 * Metodo che consente la lettura e l'importazione del file csv relativo agli utenti. 
	 * @param conn connessione al DB
	 * @param file fa inserire sul db
	 * @param userType tipo di utente che va inserito sul database
	 */
	public static void importCSV(Connection conn,byte[] file, UserType userType) {
		try (FileOutputStream fos = new FileOutputStream("CSVFile")) {
		    fos.write(file);
		    List<String> lines = readCSVFile("CSVFile");
		    int cont = 0;
		    for(String line: lines) {
		    	++cont;
		    	if(cont != 1) {	// se la riga del file csv non è quellla di intestazione
			    	line = line.replace("\"", "");
			    	String [] result = line.split(",");
			    	switch(userType) {
			    		case TEACHER:
			    			if(!result[result.length-1].equals("Docente")) {
			    				JOptionPane.showMessageDialog(null, 
			    						"ATTENZIONE: alla riga " + cont + "del file CSV non è presente un docente. Riprova");
			    				break;
			    			}
			    			Teacher docente = new Teacher();
			    			docente.userID = Long.parseLong(result[0]);
			    			docente.name = result[1];
			    			docente.surname = result[2];
			    			docente.Email = result[3];
			    			docente.activationCode = result[4];
			    			docente.Password = result[5];
			    			docente.trusted = result[6] == "true"? true:false;
			    			docente.department = Long.parseLong(result[7]);
			    			docente.isLocked = Boolean.parseBoolean(result[8]);
			    			docente.loginattempts = Long.parseLong(result[9]);
			    			DocenteController.creaDocente(conn, docente);
			    			break;
			    		case STUDENT:
			    			if(!result[result.length-1].equals("Studente")) {
			    				JOptionPane.showMessageDialog(null, 
			    						"ATTENZIONE: alla riga " + cont + "del file CSV non è presente uno studente. Riprova");
			    				break;
			    			}
			    			Student studente = new Student();
			    			studente.userID = Long.parseLong(result[0]);
			    			studente.name = result[1];
			    			studente.surname = result[2];
			    			studente.Email = result[3];
			    			studente.activationCode = result[4];
			    			studente.Password = result[5];
			    			studente.trusted = result[6] == "true"? true:false;
			    			studente.cLaurea = Long.parseLong(result[7]);
			    			studente.startYear = Integer.parseInt(result[8]);
			    			studente.status = StudentController.ConvertiStatoCar(result[9]);
			    			studente.isLocked = Boolean.parseBoolean(result[10]);
			    			studente.loginattempts = Long.parseLong(result[11]);
			    			StudentController.creaStudente(conn, studente);
			    			break;
			    		case ADMIN:
			    			if(!result[result.length-1].equals("Admin")) {
			    				JOptionPane.showMessageDialog(null, 
			    						"ATTENZIONE: alla riga " + cont + "del file CSV non è presente un admin. Riprova");
			    				break;
			    			}
			    			Admin admin = new Admin();
			    			admin.userID = Long.parseLong(result[0]);
			    			admin.name = result[1];
			    			admin.surname = result[2];
			    			admin.Email = result[3];
			    			admin.activationCode = result[4];
			    			admin.Password = result[5];
			    			admin.trusted = result[6] == "true"? true:false;
			    			admin.isLocked = Boolean.parseBoolean(result[7]);
			    			admin.loginattempts = Long.parseLong(result[8]);
			    			AdminController.creaAdmin(conn, admin);;
			    			break;
			    	}
		    	}
		    }
		    
		} catch (IOException ioe) {
		    ioe.printStackTrace();
		}

	}

	
	/**
	 * Metodo che consente di prelevare dal file csv le righe che poi saranno salvate sul DB.
	 * Il metodo è utilizzato per importare i corsi.
	 * @param conn connessione al DB
	 * @param fileReceived oggetto contenente 3 file sottoforma di array di byte
	 */
	public static void importCSV(Connection conn, Object[] fileReceived) {
		byte[] fileCorsiLaurea = (byte[]) fileReceived[0];
		byte[] fileCorsiMateria = (byte[]) fileReceived[1];
		byte[] fileDipartimenti = (byte[]) fileReceived[2];
		
		ArrayList<DegreeCourse> corsiLaurea = new ArrayList<DegreeCourse>();
		ArrayList<Course> corsiMateria = new ArrayList<Course>();
		ArrayList<Department> Dipartimenti = new ArrayList<Department>();
		
		
		// Elaboro il file relativo ai corsi di laurea
		try (FileOutputStream fos = new FileOutputStream("CSVFileCorsiLaurea")) {
		    fos.write(fileCorsiLaurea);
		    List<String> lines = readCSVFile("CSVFileCorsiLaurea");
		    int cont = 0;
		    for(String line: lines) {
		    	++cont;
		    	if(cont != 1) {			// se la riga del file csv non è quellla di intestazione
			    	line = line.replace("\"", "");
			    	String [] result = line.split(",");
			    	switch(result[result.length-1]) {
			    		case "Corsi di laurea":
			    			DegreeCourse corsoLaurea = new DegreeCourse();
			    			corsoLaurea.Id = Long.parseLong(result[0]);
			    			corsoLaurea.name = result[1];
			    			corsoLaurea.department = Long.parseLong(result[2]);
			    			corsiLaurea.add(corsoLaurea);
			    			break;
			    		default:
		    				JOptionPane.showMessageDialog(null, 
		    						"ATTENZIONE: alla riga " + cont + "del file CSV non è presente "
		    								+ "un corso di laurea. Riprova");
			    			break;
			    	}
		    	}
		    }
		    
		} catch (IOException ioe) {
		    ioe.printStackTrace();
		}
		
		
		// Elaboro il file relativo alle materie relative ai corsi di laurea
		try (FileOutputStream fos = new FileOutputStream("CSVFileCorsiMateria")) {
		    fos.write(fileCorsiMateria);
		    List<String> lines = readCSVFile("CSVFileCorsiMateria");
		    int cont = 0;
		    for(String line: lines) {
		    	++cont;
		    	if(cont != 1) {			// se la riga del file csv non è quellla di intestazione
			    	line = line.replace("\"", "");
			    	String [] result = line.split(",");
			    	showArr(result);
			    	switch(result[result.length-1]) {
			    		case "Corsi Materia":
			    			Course corsoMateria = new Course();
			    			corsoMateria.Id = Long.parseLong(result[0]);
			    			corsoMateria.degreeCourse = Long.parseLong(result[1]);
			    			corsoMateria.name = result[2];
			    			corsoMateria.activationYear = Integer.parseInt(result[3]);
			    			corsoMateria.description = result[4];
			    			corsiMateria.add(corsoMateria);
			    			break;
			    		default:
		    				JOptionPane.showMessageDialog(null, 
		    						"ATTENZIONE: alla riga " + cont + "del file CSV non è presente "
		    								+ "un corso di laurea. Riprova");
			    			break;
			    	}
		    	}
		    }
		    
		} catch (IOException ioe) {
		    ioe.printStackTrace();
		}
		
		
		try (FileOutputStream fos = new FileOutputStream("CSVFileDipartimenti")) {
		    fos.write(fileDipartimenti);
		    List<String> lines = readCSVFile("CSVFileDipartimenti");
		    int cont = 0;
		    for(String line: lines) {
		    	++cont;
		    	System.out.println("DEBUG: FileSystemController.importCSV: riga: " + line);
		    	if(cont != 1) {			// se la riga del file csv non è quellla di intestazione
			    	line = line.replace("\"", "");
			    	String [] result = line.split(",");
			    	showArr(result);
			    	switch(result[result.length-1]) {
			    		case "Dipartimenti":
			    			Department dipartimento = new Department();
			    			dipartimento.Id = Long.parseLong(result[0]);
			    			dipartimento.name = result[1];
			    			Dipartimenti.add(dipartimento);
			    			break;
			    		default:
		    				JOptionPane.showMessageDialog(null, 
		    						"ATTENZIONE: alla riga " + cont + "del file CSV non è presente "
		    								+ "un department. Riprova");
			    			break;
			    	}
		    	}
		    }
		    
		} catch (IOException ioe) {
		    ioe.printStackTrace();
		}
		
		CorsiLaureaController.importCSV(conn, corsiLaurea, corsiMateria, Dipartimenti);
	}
	
	
	
	/**
	 * Metodo per visualizzare il contenuto di un array. Utile in fase di debug.
	 * @param arr
	 */
	public static void showArr(Object[] arr) {
		for(Object o: arr) {
			System.out.println("DEBUG: FileSystemController.showArr: riga: " + o);
		}
	}
	
	
	/**
	 * Metodo per la lettura di un file CSV
	 * @param path a cui leggere il file csv
	 * @return lista contenente le righe lette dal file
	 */
	public static List<String> readCSVFile(String path) {
		List<String> list = null;
		try {
			list = Files.readAllLines(Paths.get(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	
	/**
	 * Metodo per chiedere al database di scaricare un file sotto forma di array di byte all'id specificato
	 * @param conn connessione al DB
	 * @param docId documento da scaricare
	 * @return array di byte contenente il file serializzato
	 */
	public static byte[] downloadDocumento(Connection conn, long docId) {
		String query = "select \"file\" from \"Resources\" Where \"id\" = " + docId;
		byte[] file = null;
		try {
		
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(query);
			while (rs.next()) {
			
				file = rs.getBytes("file");
			}
			
			return file;
		} catch (Exception e) {
			System.out.println("Errore nel download del documento");
			System.out.println("DEBUG: file to download==null? " + file==null?true:false);
			e.printStackTrace();
		}

		return null;
	}
	
	/**
	 * Metodo per eliminare un file sul database
	 * @param userSezione
	 * @param conn connessione al DB
	 */
	public static void eliminaFile(Connection conn, long userSezione) {
		String query = "delete from \"Resources\" where \"id\" = '" + userSezione + "';";
		Statement st = null;
		try {
			st = conn.createStatement();
			st.executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Metodo che consente di generare ilfile system del corso passato come parametro
	 * @param conn connessione al DB
	 * @param corsoID di cui ottenere il file system
	 * @return oggetto contenente file e cartelle del corso
	 */
	public static CourseFileSystem getFileSystem(Connection conn, long corsoID) {
		CourseFileSystem fs = new CourseFileSystem();
		try {
			fs.course = Cache.getCorsoMateria(corsoID);
			fs.folders = getSezioniPadri(conn, corsoID);	
			// Lista delle folders (cartelle) relative al corso materia associato

			for (Folder sez : fs.folders) {
				sez.document = cercaDocumento(conn, sez.idSez);
				sez.sonFolders = getSottoCartelle(conn, sez);
			}
			return fs;
		} catch (Exception e) {
			System.out.println("Errore nella generazione del file system");
		}

		return null;
	}

	
	
	/**
	 * Metodo per chiedere al database una lista contenente tutti i documenti 
	 * @param conn connessione al DB
	 * @return lista di tutti i documenti sul DB
	 */
	public static ArrayList<Document> getDocumentList(Connection conn) {
		String query = "select * from \"Resources\"";
		ArrayList<Document> docList = new ArrayList<Document>();
		Document doc = null;
		try {
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(query);
			
			while (rs.next()) {
				doc = new Document();
				doc.idDoc = rs.getLong(1);
				doc.idSez = rs.getLong(2);
				doc.docType = rs.getString(3);
				doc.ndownload = rs.getLong(4);
				doc.name = rs.getString(5);
				doc.data = rs.getBytes(6);
				doc.description = rs.getString(7);
				docList.add(doc);
			}
			return docList;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
		
	}
	
	/**
	 * Metodo per eliminare una sezione sul database relativa all'id sezione specificato dal parametro. 
	 * @param conn
	 * @param userSezione
	 */
	public static void eliminaSezione(Connection conn, long userSezione) {
		String query = "delete from \"Folders\" where \"folder\" = '" + userSezione + "';";
		Statement st = null;
		try {
			st = conn.createStatement();
			st.executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Metodo per aggiornare la description di un file presente sul database
	 * @param conn connessione al database
	 * @param id id del file
	 * @param description description da inserire
	 */
	public static void updateFileDescription(Connection conn, long id, String description) {
		String query = "UPDATE \"Resources\" SET  \"description\" = '" + description + "' where \"id\" = '"+ id + "';";
		Statement st = null;
		try {
		st = conn.createStatement();
		st.executeUpdate(query);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	
	/**
	 * Metodo che restituisce l'estensione del file passato come parametro
	 * @param conn id del file
	 * @param docName nome del file di cui prendere l'estensione
	 * @return estensione del file
	 */
	public static String getDocTypeByDocName(Connection conn, String docName) {
	String query = "select \"doctype\" from \"Resources\" where \"name\" like '%" + docName + "%';";
	String docType = null;
	try {
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery(query);
		
		while (rs.next()) {
			docType = rs.getString(1);
		}
		return docType;
	} catch (Exception ex) {
		ex.printStackTrace();
	}
		return null;
	}
}
