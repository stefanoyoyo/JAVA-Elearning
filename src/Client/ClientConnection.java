package Client;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Date;
import Common.Logger;
import Common.DBType.*;
import Common.Enumerators.DocumentType;
import Common.Enumerators.RequestType;
import Common.Enumerators.UserType;
import Common.Pacchetti.RequestContent;
import Common.Pacchetti.ReceiveContent;
import Server.Database.Corsi.CorsiLaureaController;


/**
 * @author Amenta Stefano, Moroni Paolo 
 * 
 * Classe che permette all'utente di richiedere delle funzionalità al server
 *
 */
public class ClientConnection {

	private static int PORT = 50123;
	private static String address = "localhost";
	private static InetAddress inetAdd;
	private static Socket s;
	private static ObjectInputStream ois;
	private static ObjectOutputStream oos;

	/**
	 * Invio dei dati client -> server
	 * @param rp
	 */
	public static void send(RequestContent rp) {
		try {
			oos.writeObject(rp);
			oos.flush();
		} catch (Exception e) {
			Logger.WriteError(e, "ClientConnection", "Send");
		}
	}
	
	/**
	 * CLIENT 
	 * Ricezione dati client <- server
	 * @param rp
	 * @return
	 */
	public static ReceiveContent sendReceive(RequestContent rp) {
		try {
			send(rp); 
			ReceiveContent res = null;
			try {
				res = (ReceiveContent) ois.readObject(); 
			} catch (SocketException ec) {
				System.err.println("DEBUG: ClientConnection.SendReceive(RequestContent rp) IOEXCEPTION: " + ec.getMessage() + 
						"\nec.getLocalizedMessage() " + ec.getLocalizedMessage() + 
						"\nec.getCause() " + ec.getCause());
//				System.out.print("ec.printStackTrace(): "); ec.printStackTrace();
			}
			return res;
		} catch (Exception e) {
			Logger.WriteError(e, "ClientConnection", "SendReceive");
		}
		return null;
	}
	
	/**
	 * Metodo per resettare il numeri di tentativi dopo che l'utente è stato sbloccato dall'admin
	 * @param email
	 */
	public static void setZeroNumeroTentativiLogin(String email) {
		RequestContent rp = new RequestContent();
		rp.parameters = new Object[] { email };
		rp.type = RequestType.SET_ZERO_LOGIN_ATTEMPTS;
		send(rp);
	}
	
	/**
	 * Metodo per aggiornare sul server il conteggio nel numero di download di un file da parte di un utente specifico
	 * @param userId
	 * @param docId
	 */
	public static void updateDownloadCont(long userId, long docId) {
		RequestContent rp = new RequestContent();
		rp.parameters = new Object[] { userId, docId };
		rp.type = RequestType.UPDATE_DOWNLOAD_COUNT;
		send(rp);
	}
	
	/**
	 * Metodo per eliminare un file esistente
	 * @param idSezione
	 */
	public static void eliminaFile(long idSezione) {
		RequestContent rp = new RequestContent();
		rp.parameters = new Object[] { idSezione };
		rp.type = RequestType.DELETE_FILE;
		send(rp);
	}
	
	/**
	 * Metodo per eliminare una sezione
	 * @param rif_Sezione
	 */
	public static void eliminaSezione(long idSezione) {
		// TODO Auto-generated method stub
		RequestContent rp = new RequestContent();
		rp.type = RequestType.DELETE_FOLDER;
		rp.parameters = new Object[] { idSezione };
		send(rp);
	}
	
	/**
	 * Metodo per ottenere il numeri di accessi compiuti da una mail speifica
	 * @param email
	 * @return
	 */
	public static long getNumeroTentativiLogin(String email) {
		RequestContent rp = new RequestContent();
		rp.parameters = new Object[] { email };
		rp.type = RequestType.GET_LOGIN_ATTEMPTS_NUMBER;
		ReceiveContent rpp = sendReceive(rp);
		long getNumeroTentativiLogin = (long) rpp.parameters[0];
		return getNumeroTentativiLogin;
	}
	
	/**
	 * Metodo per sincronizzare client e server sul numero di tentativi di login effettuati da un utente
	 */
	public static void incNumeroTentativiLogin(String email) {
		RequestContent rp = new RequestContent();
		rp.parameters = new Object[] { email };
		rp.type = RequestType.INCREMENT_LOGIN_ATTEMPTS;
		send(rp);
	}
	
	/**
	 * Metodo per sbloccare un'utente bloccato a seguito di 10 tentativi di login errati
	 * @param matricola
	 */
	public static void unlockUser(long matricola) {
		RequestContent rp = new RequestContent();
		rp.parameters = new Object[] { matricola };
		rp.type = RequestType.UNLOCK_USER;
		send(rp);
	}
	
	/**
	 * Metodo che restituisce una lista dei docenti attualmente bloccati
	 * @return
	 */
	public static ArrayList<Teacher> getLockedDocenti() {
		RequestContent rp = new RequestContent();
		rp.type = RequestType.GET_LOCKED_TEACHERS;
		ReceiveContent rpp = sendReceive(rp);
		ArrayList<Teacher> docentiBloccati = (ArrayList<Teacher>) rpp.parameters[0];
		return docentiBloccati;
	}
	
	/**
	 * Metodo che restituisce una lista degli studenti attualmente bloccati
	 * @return
	 */
	public static ArrayList<Student> getLockedStudenti() {
		RequestContent rp = new RequestContent();
		rp.type = RequestType.GET_LOCKED_STUDENTS;
		ReceiveContent rpp = sendReceive(rp);
		ArrayList<Student> studentiBloccati = (ArrayList<Student>) rpp.parameters[0];
		return studentiBloccati;
	}
	
	/**
	 * Metodo che restituisce una lista degli admin attualmente bloccati
	 * @return
	 */
	public static ArrayList<Admin> getLockedAdmin() {
		RequestContent rp = new RequestContent();
		rp.type = RequestType.GET_LOCKED_ADMINS;
		ReceiveContent rpp = sendReceive(rp);
		ArrayList<Admin> adminBloccati = (ArrayList<Admin>) rpp.parameters[0];
		return adminBloccati;
	}

	/**
	 * Metodo per richiedere al server i corsi dello studente passato come parametro
	 * @param studID
	 * @return
	 */
	public static ArrayList<Course> getCorsiStud(long studID) {
		try {
			RequestContent rp = new RequestContent();
			rp.parameters = new Object[] { studID };
			rp.type = RequestType.GET_STUDENT_COURSES;
			ReceiveContent rpp = sendReceive(rp);
			ArrayList<Course> lis = (ArrayList<Course>) rpp.parameters[0];
			return lis;
		} catch (Exception e) {
			Logger.WriteError(e, "ServerThread", "GET_STUDENT_COURSES");
		}
		return new ArrayList<Course>();
	}

	/**
	 * Metodo per richiedere al server una lista di corsi di laurea dato il dipartimento
	 * @param dipI
	 * @return
	 */
	public static ArrayList<DegreeCourse> getCorsiLaureaByDip(long dipI) {
		try {
			RequestContent rp = new RequestContent();
			rp.type = RequestType.GET_DEGREE_COURSES;
			rp.parameters = new Object[] { dipI };
			ReceiveContent resP = sendReceive(rp);
			ArrayList<DegreeCourse> lista = (ArrayList<DegreeCourse>) resP.parameters[0];
			return lista;
		} catch (Exception e) {
			System.out.println("Errore nell'ottenimento della lista dei corsi");
		}
		return new ArrayList<DegreeCourse>();
	}

	/**
	 * Metodo per ottenere una lista di dipartimenti
	 * @return
	 */
	public static ArrayList<Department> getDipartimenti() {
		try {
			if (s == null || s.isClosed()) {
				try {
				connect2Server();
				} catch (Exception ex) {
					System.out.println("DEBUG: try catch GET_DEPARTMENTS() " 
				+ "\n ex.getMessage() " + ex.getMessage() 
				+ "\n ex.toString() " + ex.toString());
				}
			}
			RequestContent rp = new RequestContent();
			
			rp.type = RequestType.GET_DEPARTMENTS;
			
			ReceiveContent rp1 = null;
			
			try {
			rp1 = sendReceive(rp);
			} catch (Exception ex) {
				System.out.println("DEBUG: try catch GET_DEPARTMENTS() ReceiveContent rp1 = SendReceive(rp) " 
						+ "\n ex.getMessage() " + ex.getMessage() 
						+ "\n ex.toString() " + ex.toString());
			}
			
			ArrayList<Department> lista = (ArrayList<Department>) rp1.parameters[0];
			return lista;
		} catch (Exception e) {
			
		}
		return null;
	}
	
	
	/**
	 * Metodo per chiedere al server di aggiornare la descrizione di una cartella sul db
	 * @param id
	 * @param description
	 */
	public static void updateSezDescription (long id, String description) {
		RequestContent rp = new RequestContent();
		rp.parameters = new Object[] { id, description };
		rp.type = RequestType.UPDATE_FOLDER_DESCRIPTION;
		send(rp);
	}

	/**
	 * Metodo per chiedere a server di iscrivere ad un corso l'utente indicato
	 * @param idUtente
	 * @param idCorso
	 */
	public static void subscribeCourse(long idUtente, long idCorso) {
		try {
			RequestContent rp = new RequestContent();
			rp.parameters = new Object[] { idUtente, idCorso };
			rp.type = RequestType.SUBSCRIVE_COURSE;
			send(rp);
		} catch (Exception e) {
			Logger.WriteError(e, "ClientConnection", "SUBSCRIVE_COURSE");
		}
	}

	/**
	 * Metodo per chiedere a server di restituire una lista dei corsi materia dato il corso 
	 * di laurea
	 * @param index
	 * @return
	 */
	public static ArrayList<Course> getCorsiMateriaByLaurea(long index) {
		try {
			RequestContent rp = new RequestContent();
			rp.type = RequestType.GET_COURSES;
			rp.parameters = new Object[] { index };
			ReceiveContent resP = sendReceive(rp);
			ArrayList<Course> lista = (ArrayList<Course>) resP.parameters[0];
			
			return lista;
		} catch (Exception e) {
			System.out.println("Errore nell'ottenimento della lista dei corsi");
		}
		return new ArrayList<Course>();
	}

	/**
	 * Metodo per chiedere a server di costruire il file system dei corsi da mostrare agli utenti
	 * @param index
	 * @return
	 */
	public static CourseFileSystem getFileSystem(long index) {
		try {
			RequestContent rp = new RequestContent();
			rp.type = RequestType.GET_FILE_SYSTEM;
			rp.parameters = new Object[] { index };
			ReceiveContent rpc = sendReceive(rp);
			CourseFileSystem fs = (CourseFileSystem) rpc.parameters[0];
			
			return fs;
		} catch (Exception e) {
			System.out.println("Errore reperimento file system client instance");
		}
		return null;
	}
	
	/**
	 * Metodo che restitisce la description di un corso
	 * @param id
	 * @return
	 */
	public static String courseDescription(long idCorso) {
		RequestContent rp = new RequestContent();
		rp.type = RequestType.COURSE_DESCRIPTION;
		rp.parameters = new Object[] { idCorso };
		ReceiveContent rpc = sendReceive(rp);
		String descr = (String) rpc.parameters[0];
		return descr;
		
	}

	/**
	 * Metodo per chiedere a server di  aggiornare il numero di documenti scaricati specificato 
	 * dal parametro
	 * @param idDoc numero del documento di cui tenere conto dei download effettuati
	 * @param userID dell'utente che lo ha scaricato
	 */
	private static void uploadDocumentCont(long idDoc, long userID) {
		try {
			RequestContent rp = new RequestContent();
			rp.type = RequestType.UPDATE_DOCUMENT_COUNT;
			rp.parameters = new Object[] { idDoc, userID };
			send(rp);
		} catch (Exception e) {
			System.out.println("Errore UploadDocumentCont");
		}
	}

	/**
	 * Metodo per chiedere a server di cambiare la password drelativa alla mail passata come parametro
	 * @param email
	 * @param newPsw
	 */
	public static void cambiaPasswordByMail(String email, String newPsw) {
		try {
			RequestContent rp = new RequestContent();
			rp.type = RequestType.PASSWORD_CHANGE;
			rp.parameters = new Object[] { email, newPsw };
			send(rp);
		} catch (Exception e) {

		}
	}

	/**
	 * Metodo per chiedere a server di cambiare l'anagrafica dell'utente passato come parametro
	 * @param mod
	 */
	public static void cambiaAnagrafica(Userdatas mod) {
		try {
			RequestContent rp = new RequestContent();
			rp.type = RequestType.MODIFY_USER_DATA;
			rp.parameters = new Object[] { mod };
			send(rp);
		} catch (Exception e) {

		}
	}

	/**
	 * Metodo per chiedere a server di fornire l'anagrafica di uno studente data la matricola
	 * @param matricola
	 * @return
	 */
	public static Student getStudenteByMat(long matricola) {
		try {
			RequestContent rp = new RequestContent();
			rp.type = RequestType.GET_STUDENT_BY_USERID;
			rp.parameters = new Object[] { matricola };
			ReceiveContent rpp = sendReceive(rp);
			return (Student) rpp.parameters[0];
		} catch (Exception e) {
			System.out.println(e);
		}
		return null;
	}
	
	/**
	 * CLIENT
	 * Metodo che si occupa di inizializzare l'oggetto di tipo Document con i dati ca caricare sul database.
	 * @param path
	 * @param idSez
	 * @return
	 */
	public static boolean documentUpload(String path, long idSez) {
		try {
			Document newDoc = new Document();
			byte[] array = Files.readAllBytes(new File(path).toPath());
			newDoc.data = array;
			newDoc.idSez = idSez;
			String replace = path.replace("\\", "©");

			// Ottengo nome
			String[] split = replace.split("©");
			String last = split[split.length - 1]; // prelevo l'ultimo token dell'array che ceramente conterrà il nome del file
			String splitq[] = last.split("\\.");

			if (splitq.length == 1) {
				// caso particolare senza estensione
				newDoc.name = last;
				newDoc.docType = "";
			} else {
				newDoc.name = splitq[0];
				newDoc.docType = splitq[1];
			}
			
			RequestContent rp = new RequestContent();
			rp.type = RequestType.UPLOAD_DOCUMENT;
			rp.parameters = new Object[] { newDoc };
			send(rp);

			return true;
		} catch (Exception e) {
			System.out.println("err doc upload");

		}
		return false;
	}

	/**
	 * Metodo per chiedere a server di ottenere l'anagrafica di un corso di laurea dato l'id
	 * @param corsoLaureaID
	 * @return
	 */
	public static DegreeCourse getCorsoLaureaByID(long corsoLaureaID) {
		try {
			RequestContent rp = new RequestContent();
			rp.parameters = new Object[] { corsoLaureaID };
			rp.type = RequestType.GET_DEGREE_COURSES_BY_ID;
			ReceiveContent rpp = sendReceive(rp);
			return (DegreeCourse) rpp.parameters[0];//
		} catch (Exception e) {

		}
		return null;
	}

	/**
	 * Metodo per ottenere dal server una lista dei documenti 
	 * @return
	 */
	public static ArrayList<Document> getDocumentList() {
		RequestContent rp = new RequestContent();
		rp.type = RequestType.GET_DOCUMENTS;
		ReceiveContent rp1 = sendReceive(rp);
		ArrayList<Document> doc = (ArrayList<Document>) rp1.parameters[0];
		return doc;
	}
	
	/**
	 * Metodo per chiedere a server di scaricare un documento
	 * @param idDoc
	 * @param userID
	 * @param path
	 * @param format
	 * @return
	 */
	public static boolean documentDownload(long idDoc, long userID, String path, String format) {
		try {
			RequestContent rp = new RequestContent();
			rp.type = RequestType.DOWNLOAD_DOCUMENT;
			rp.parameters = new Object[] { idDoc };
			ReceiveContent rp1 = sendReceive(rp);
			byte[] doc = (byte[]) rp1.parameters[0];
			String finalPath = path;
			try (FileOutputStream fos = new FileOutputStream(finalPath + "." + format)) {
				fos.write(doc);
			}
			return true;
		} catch (Exception e) {
			System.out.println(e + "\nErrore nel download del documento");
			e.printStackTrace();
			Logger.WriteError(e, "ClientConnection", "DocumentDownload");
		}
		return false;
	}

	/**
	 * Metodo per chiedere a server di ottenere il corso del docente
	 * @param idDocente
	 * @param idCorso
	 */
	public static void linkDocenteCorso(long idDocente, long idCorso) {
		try {
			RequestContent rp = new RequestContent();
			rp.type = RequestType.LINK_DOCENTE_CORSO;
			rp.parameters = new Object[] { idDocente, idCorso };
			send(rp);
		} catch (Exception e) {
			System.out.println("LINK_DOCENTE_CORSO");
		}
	}

	/**
	 * Metodo che permette di invare una mail a tutti gli utenti iscritti ad un corso.
	 * @param idCorso
	 * @param oggetto
	 * @param messaggio
	 */
	public static void sendNewsLetterToAllStudents(long idCorso, String oggetto, String messaggio) {
		try {
			RequestContent rp = new RequestContent();
			rp.parameters = new Object[] { idCorso, oggetto, messaggio };
			rp.type = RequestType.SEND_NEWSLETTER_TO_ALL_STUDENTS;
			send(rp);
		} catch (Exception e) {
			System.out.println("SendNewsLetter error");
		}
	}
	
	/**
	 * Metodo per inviare una mail solo ad un gruppo specifico di studenti
	 * @param idCorso
	 * @param oggetto
	 * @param messaggio
	 */
	public static void sendNewsLetterToSomeStudents(long idCorso, ArrayList<String> emailStudenti, String oggetto, String messaggio) {
		try {
			RequestContent rp = new RequestContent();
			rp.parameters = new Object[] { idCorso, oggetto, messaggio, emailStudenti };
			rp.type = RequestType.SEND_NEWSLETTER_TO_SOME_USERS;
			send(rp);
		} catch (Exception e) {
			System.out.println("SendNewsLetter error");
		}
	}

	/**
	 * Metodo per chiedere a server di ottenere una lista degli studenti
	 * @return
	 */
	public static ArrayList<Student> getAllStudenti() {
		try {
			RequestContent rp = new RequestContent();
			rp.parameters = new Object[] {};
			rp.type = RequestType.GET_ALL_STUDENTS;
			ReceiveContent rps = sendReceive(rp);
			return (ArrayList<Student>) rps.parameters[0];
		} catch (Exception e) {
			System.out.println("SendNewsLetter error");
		}
		return new ArrayList<Student>();
	}

	/**
	 * Metodo per chiedere a server di otterene la lista dei corsi dato il docente
	 * @param idDocente
	 * @return
	 */
	public static ArrayList<Course> getCorsiByDocente(long idDocente) {
		try {
			RequestContent rp = new RequestContent();
			rp.parameters = new Object[] { idDocente };
			rp.type = RequestType.GET_COURSE_DOCUMENTS;
			ReceiveContent rps = sendReceive(rp);
			return (ArrayList<Course>) rps.parameters[0];
		} catch (Exception e) {
			System.out.println("SendNewsLetter error");
		}
		return new ArrayList<Course>();
	}

	/**
	 * Metodo per chiedere a server di bloccare un accont specificato dalla mail passata come parametro
	 * @param Email
	 */
	public static void bloccaAccount(String Email) {
		try {
			if (s == null || s.isClosed()) {
				connect2Server();
			}
			RequestContent rp = new RequestContent();
			rp.type = RequestType.PASSWORD_FORGET;
			rp.parameters = new Object[] { Email };
			send(rp);
		} catch (Exception e) {
			Logger.WriteError(e, "ClientConnection", "BloccaAccount");
		}
	}

	/**
	 * Metodo per chiedere a server di modificare l'anagrafica di un corso materia
	 * @param corsoModificato
	 */
	public static void modificaCorsoMateria(Course corsoModificato) {
		try {
			RequestContent rp = new RequestContent();
			rp.type = RequestType.MODIFY_COURSE;
			rp.parameters = new Object[] { corsoModificato };
			send(rp);
		} catch (Exception e) {
			System.out.println("Errore MODIFY_COURSE");
		}
	}

	/**
	 * Metodo per chiedere a server di cambiare la visibilità di una cartella
	 * @param idSez
	 * @param vis
	 */
	public static void cambiaVisibilita(long idSez, Common.Enumerators.FolderVisibility vis) {
		try {
			RequestContent rp = new RequestContent();
			rp.type = RequestType.CHANGE_FOLDER_VISIBILITY;
			rp.parameters = new Object[] { idSez, vis };
			send(rp);
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	/**
	 * Metodo per chiedere a server di creare un corso materia
	 * @param newCorso
	 * @return
	 */
	public static boolean createCorsoMateria(Course newCorso) {
		try {
			RequestContent rp = new RequestContent();
			rp.type = RequestType.CREATE_COURSE;
			rp.parameters = new Object[] { newCorso };
			send(rp);
			return true;
		} catch (Exception e) {
			System.out.println("Errore nella creazione di un corso materia");
		}
		return false;
	}

	/**
	 * Metodo che consente di inizializzare i canali di comunicazione col server
	 * @return
	 */
	public static boolean connect2Server() {
		try {
			inetAdd = InetAddress.getByName(null);
			s = new Socket(inetAdd, PORT);
			oos = new ObjectOutputStream(s.getOutputStream());
			ois = new ObjectInputStream(s.getInputStream());
			return true;
		} catch (Exception e) {
			System.out.println("Errore connessione al server");
		}
		return false;
	}

	/**
	 * Metodo per chiedere a server di ottenere una lista di tutti i corsi
	 * @return
	 */
	public static ArrayList<Course> ottieniCorsi() {
		try {
			RequestContent rp = new RequestContent();
			rp.type = RequestType.GET_COURSES;
			ReceiveContent resP = sendReceive(rp);
			ArrayList<Course> lista = (ArrayList<Course>) resP.parameters[0];
		} catch (Exception e) {
			System.out.println("Errore nell'ottenimento dei corsi");
		}
		return new ArrayList<Course>();
	}

	/**
	 * Metodo per chiedere a server di creare una nuova cartella sul database
	 * @param newFolder
	 * @param idCorso
	 */
	public static void creaSezione(Folder newFolder, long idCorso) {
		try {
			RequestContent rp = new RequestContent();
			rp.parameters = new Object[] { newFolder, idCorso };
			rp.type = RequestType.CREATE_FOLDER;
			send(rp);
		} catch (Exception e) {
			System.out.println("Errore nella generazione della nuova cartella");
		}
	}

	/**
	 *
	 * Metodo per fare una richiesta di login al server
	 */
	public static boolean loginRequest(String Email, String Psw, UserType tipo_user) {
		try {
			if (s == null || s.isClosed()) {
				connect2Server();
			}
			
			RequestContent rq = new RequestContent();
			rq.type = RequestType.LOGIN_USER;
			rq.parameters = new Object[] { Email, Psw };
			rq.userType = tipo_user;
			ReceiveContent rs = sendReceive(rq); 
			if (rs.parameters[0] != null) {
				switch (tipo_user) {
				case ADMIN: 
					Client.LoggedUser.userType = UserType.ADMIN;
					Client.LoggedUser.anagrafica = (Admin) rs.parameters[0];
					break;
				case TEACHER:
					Client.LoggedUser.userType = UserType.TEACHER;
					Client.LoggedUser.anagrafica = (Teacher) rs.parameters[0];
					break;
				case STUDENT:
					Client.LoggedUser.userType = UserType.STUDENT;
					Client.LoggedUser.anagrafica = (Student) rs.parameters[0];
					break;
				default:
					break;
				}
				return true;
			}

		} catch (Exception e) {
			System.out.println("Errore nell'inoltro della richiesta \"login\"");
		}
		return false;
	}
	
	/**
	 * Metodo per l'invio della richiesta al server di upload del file csv passato come parametro
	 * @param file
	 * @param userType
	 */
	public static void importCSV(byte[] file, UserType userType) {
		RequestContent rp = new RequestContent();
		rp.type = RequestType.IMPORT_CSV_FILE;
		rp.userType = userType;
		rp.parameters = new Object[] { file };
		send(rp);
	}
	
	/**
	 * Metodo per l'invio della richiesta al server di upload del file csv passato come parametro
	 * @param file lista dei file csv da inviare al server
	 * @param userType tipo di documento che deve essere caricato
	 */
	public static void importCSV(Object[] fileToSend) {
		RequestContent rp = new RequestContent();
		rp.type = RequestType.IMPORT_CSV_FILE;
		rp.parameters = new Object[] { fileToSend };
		send(rp);
	}
	
	/**
	 * Metodo da usare per eliminare l'utente presente sul db relativo all'id specificato 
	 * @param userID
	 * @param userType
	 */
	public static void deleteUser(long userID) {
		RequestContent rp = new RequestContent();
		rp.type = RequestType.DELETE_USER;
		rp.parameters = new Object[] { userID };
		send(rp);
	}
	
	/**
	 * Metodo per conoscere che tipo di utente è l'utente specificato come parametro
	 * @param userID
	 */
	public static void getUserType(long userID) {
		RequestContent rp = new RequestContent();
		rp.type = RequestType.GET_USER_TYPE; 
		rp.parameters = new Object[] { userID };
		send(rp);
	}
	
	
	/**
	 * CLIENT
	 * Metodo che manda la richiesta al server di inserimento nuovo utente nel database
	 * @param stud
	 */
	public static void creaStudente(Student stud) {
		try {
			RequestContent rp = new RequestContent();
			rp.type = RequestType.CREATE_USER;
			rp.userType = UserType.STUDENT;
			rp.parameters = new Object[] { stud };
			send(rp);
		} catch (Exception e) {

		}
	}

	/**
	 * Metodo per chiedere a server di creare un nuovo docente sul db
	 * @param doc
	 */
	public static void creaDocente(Teacher doc) {
		try {
			RequestContent rp = new RequestContent();
			rp.type = RequestType.CREATE_USER;
			rp.userType = UserType.TEACHER;
			rp.parameters = new Object[] { doc };
			send(rp);
		} catch (Exception e) {

		}
	}

	/**
	 * Metodo per chiedere a server di creare un nuovo admin sul db
	 * @param admin
	 */
	public static void creaAdmin(Admin admin) {
		try {
			RequestContent rp = new RequestContent();
			rp.type = RequestType.CREATE_USER;
			rp.userType = UserType.ADMIN;
			rp.parameters = new Object[] { admin };
			send(rp);
		} catch (Exception e) {

		}
	}

	/**
	 * Metodo per chiedere a server di scaricare dal database una lista di tutti i docenti
	 * @return
	 */
	public static ArrayList<Teacher> getAllDocenti() {
		try {
			RequestContent rp = new RequestContent();
			rp.type = RequestType.GET_ALL_TEACHERS;
			ReceiveContent rp1 = sendReceive(rp);
			ArrayList<Teacher> lis = (ArrayList<Teacher>) rp1.parameters[0];
			return lis;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<Teacher>();
	}

	/**
	 * Metodo per chiedere a server di 
	 * @param idUtente
	 * @param idCorso
	 */
	public static void updateLoc(long idUtente, long idCorso) {
		try {
			// idCorso = -1 se si sta uscendo
			RequestContent rp = new RequestContent();
			rp.type = RequestType.UPDATE_LOC;
			rp.parameters = new Object[] { idCorso, idUtente };
			send(rp);
		} catch (Exception e) {

		}
	}

	/**
	 * Metodo per chiedere a server di ottenere il numero di utenti loggati
	 * @return
	 */
	public static int getNumberLoggedUser() {
		try {
			RequestContent rp = new RequestContent();
			rp.type = RequestType.GET_LOGGED_USER_NUMBER;
			rp.parameters = new Object[] {};
			ReceiveContent rpp = sendReceive(rp);
			int logged = (int) rpp.parameters[0];
			return logged;
		} catch (Exception e) {

		}
		return 0;
	}

	/**
	 * Metodo per chiedere a server di ottenere il numero di utenti loggati al corso specificato dal parametro
	 * @param idCorso
	 * @return
	 */
	public static int getNumberLoggedUserByCorso(long idCorso) {
		try {
			RequestContent rp = new RequestContent();
			rp.type = RequestType.GET_LOGGED_NUMBER_BY_CORSO;
			rp.parameters = new Object[] { idCorso };
			ReceiveContent rpp = sendReceive(rp);
			int logged = (int) rpp.parameters[0];
			return logged;
		} catch (Exception e) {

		}
		return 0;
	}

	/**
	 * Metodo per chiedere a server di ottenere il numero di accessi ad un corso nel dato periodo, specificato dai parametri
	 * @param idCorso
	 * @param start
	 * @param end
	 * @return
	 */
	public static int getNumberAccessByPeriod(long idCorso, Date start, Date end) {
		try {
			RequestContent rp = new RequestContent();
			rp.type = RequestType.GET_NUMBER_ACCESS_BY_PERIOD;
			rp.parameters = new Object[] { idCorso, start, end };
			ReceiveContent rpp = sendReceive(rp);
			int logged = (int) rpp.parameters[0];
			return logged;
		} catch (Exception e) {

		}
		return 0;
	}

	/**
	 * Metodo per chiedere a server di ottenere una media del tempo passato sul corso
	 * @param idCorso
	 * @return
	 */
	public static double getMediaCorso(long idCorso) {
		try {
			RequestContent rp = new RequestContent();
			rp.type = RequestType.GET_SECONDS_AVERAGE;
			rp.parameters = new Object[] { idCorso };
			ReceiveContent rpp = sendReceive(rp);
			double seconds = (double) rpp.parameters[0];
			return seconds;
		} catch (Exception e) {

		}
		return 0;
	}

	/**
	 * Metodo per chiedere a server di restituire il numeri dei documenti relativi ad un corso
	 * @param idCorso
	 * @return
	 */
	public static int documentContByCorso(long idCorso) {
		try {
			RequestContent rp = new RequestContent();
			rp.type = RequestType.DOCUMENT_COUNT_BY_CORSO;
			rp.parameters = new Object[] { idCorso };
			ReceiveContent rpp = sendReceive(rp);
			int cont = (int) rpp.parameters[0];
			return cont;
		} catch (Exception e) {
			System.out.println("Error DOCUMENT_COUNT_BY_CORSO");
		}
		return 0;
	}

	/**
	 * Metodo per chiedere a server di fornire il numero di utenti che hanno scaricato nel dato periodo
	 * @param start
	 * @param end
	 * @return
	 */
	public static int contUtentiDownload(Date start, Date end) {
		try {
			RequestContent rp = new RequestContent();
			rp.type = RequestType.DOWNLOAD_USER_COUNT;
			rp.parameters = new Object[] { start, end };
			ReceiveContent rpp = sendReceive(rp);
			int cont = (int) rpp.parameters[0];
			return cont;
		} catch (Exception e) {
			System.out.println("Err DOWNLOAD_USER_COUNT");
		}
		return 0;
	}

	/**
	 * Metodo per chiedere a server di verificare che il codice di attivazione inserito dall'utente coincida
	 *  con quello presente sul db
	 * @param userID
	 * @param code
	 * @return
	 */
	public static boolean verificaCod(long userID, long code) {
		try {
			RequestContent rp = new RequestContent();
			rp.type = RequestType.VERIFY_COD;
			rp.parameters = new Object[] { userID, code };
			ReceiveContent rpp = sendReceive(rp);
			boolean ris = (boolean) rpp.parameters[0];
			return ris;
		} catch (Exception e) {
			Logger.WriteError(e, "ClientConnection", "VERIFY_COD");
		}
		return false;
	}
	
	/**
	 * Metodo per ottenere tutti i corsi materia salvati sul DB
	 * @return
	 */
	public static ArrayList<Course> getAllCorsiMateria() {
		try {
			RequestContent rp = new RequestContent();
			rp.type = RequestType.GET_ALL_COURSES;
			ReceiveContent rp1 = sendReceive(rp);
			ArrayList<Course> lista = (ArrayList<Course>) rp1.parameters[0];
			for (Course m : lista) {
			
			}
			return lista;
		} catch (Exception e) {
			Logger.WriteError(e, "ClientConnection", "GET_ALL_COURSES");
		}
		return new ArrayList<Course>();
	}
	
	/**
	 * Metodo per l'eliminazione di un utente dal database
	 * @param idCorso id del corso da eliminare
	 */
	public static void deleteCorsoMateria(long idCorso) {
		System.out.println("Eliminazione utente in corso");
		RequestContent rp = new RequestContent();
		rp.type = RequestType.DELETE_COURSE;
		rp.parameters = new Object[] { idCorso };
		send(rp);
	}
	
	/**
	 * Metodo che consente di chiudere la comunicazione col server in modo sicuro
	 */
	public static void close() {
		try {
			System.out.println("Chiusura connessione");
			RequestContent rp = new RequestContent();
			rp.type = RequestType.CLOSE;
			send(rp);
			Client.LoggedUser.anagrafica = null;
			ois.close();
			oos.close();
			s.close();
		} catch (Exception e) {
			System.out.println("Errore close client connection");
		}
	}
	
	/**
	 * Metodo usato per richiedere al server una lista di anagrafiche completa
	 * @return
	 */
	public static ArrayList<Userdatas> getAllUsersList() {
		RequestContent rp = new RequestContent();
		rp.type = RequestType.GET_ALL_USERS;
		ReceiveContent rpp = sendReceive(rp);
		ArrayList<Userdatas> anagList = (ArrayList<Userdatas>) rpp.parameters[0];
		return anagList;
	}

	/**
	 * Metodo per chiedere a server di ottenere il numero di studenti dato il corso passato come parametro
	 * @param idCorso
	 * @return
	 */
	public static ArrayList<Student> getStudentiByCorso(long idCorso) {
		RequestContent rp = new RequestContent();
		rp.type = RequestType.GET_STUDENTS_BY_COURSE;
		rp.parameters = new Object[] { idCorso };
		ReceiveContent rpp = sendReceive(rp);
		ArrayList<Student> anagList = (ArrayList<Student>) rpp.parameters[0];
		return anagList;
	}

	/**
	 * Metodo per chiedere al server di aggiornare la description di un file 
	 * @param idSezione
	 * @param description
	 */
	public static void updateFileDescription(long idSezione, String description) {		
	RequestContent rp = new RequestContent();
	rp.parameters = new Object[] { idSezione, description };
	rp.type = RequestType.UPDATE_FILE_DESCRIPTION;
	send(rp);
		
	}

	/**
	 * Metodo per chiedere a server di ottenere la mail del docente dato l'id del corso, passato come 
	 * parametro
	 * @param idCorso
	 * @return
	 */
	public static ArrayList<Teacher> emailDocentefromCorso(long idCorso) {
		RequestContent rp = new RequestContent();
		rp.type = RequestType.GET_EMAIL_TEACHER_FROM_CORSO;
		rp.parameters = new Object[] { idCorso };
		ReceiveContent rpp = sendReceive(rp);
		ArrayList<Teacher> emailDocentefromCorso = (ArrayList<Teacher>) rpp.parameters[0];
		return emailDocentefromCorso;
	}

	/**
	 * Metodo per chiedere a server di ottenere il tipo del documento il cui nome è passato come parametro
	 * @param docName
	 * @return
	 */
	public static String getDocTypeByDocName(String docName) {
		RequestContent rp = new RequestContent();
		rp.type = RequestType.GET_DOCTYPE_BY_DOCNAME;
		rp.parameters = new Object[] { docName };
		ReceiveContent rpp = sendReceive(rp);
		String getDocTypeByDocName = (String) rpp.parameters[0];
		return getDocTypeByDocName;
	}

	public static ArrayList<Admin> getAllAdmin() {
		RequestContent rp = new RequestContent();
		rp.type = RequestType.GET_ALL_ADMIN;
		ReceiveContent rpp = sendReceive(rp);
		ArrayList<Admin> allAdmin = (ArrayList<Admin>) rpp.parameters[0];
		return allAdmin;
	}
	


}
