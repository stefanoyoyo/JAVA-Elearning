package Server;

/**
 * THREAD DEL SERVER
 */
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import Common.Logger;
import Common.DBType.Admin;
import Common.DBType.Userdatas;
import Common.DBType.Folder;
import Common.DBType.DegreeCourse;
import Common.DBType.Course;
import Common.DBType.Department;
import Common.DBType.Teacher;
import Common.DBType.Document;
import Common.DBType.CourseFileSystem;
import Common.DBType.UserMonitoring;
import Common.DBType.Student;
import Common.Enumerators.UserType;
import Common.Enumerators.FolderVisibility;
import Common.Pacchetti.RequestContent;
import Common.Pacchetti.ReceiveContent;
import Server.Database.DatabaseManager;
import Server.Database.Corsi.CorsiLaureaController;
import Server.Database.Corsi.FileSystemController;
import Server.Database.Utenti.AdminController;
import Server.Database.Utenti.AnagraficaController;
import Server.Database.Utenti.DocenteController;
import Server.Database.Utenti.StudentController;

/**
 *  Istanza per la connessione, soddisfa le richieste effettuate attraverso la socket
 * 
 * @author Amenta Stefano, Moroni Paolo
 *
 */
public class ServerThread extends Thread implements Serializable {
	
	private static final long serialVersionUID = 1L;
	Socket s;
	DatabaseManager db;
	private UserMonitoring utenteLoggato;
	public static UserMonitoring loggedUser;
	ObjectOutputStream oos;
	ObjectInputStream ois;

	public ServerThread(Socket sock, DatabaseManager dbm) {
		this.s = sock;
		this.db = dbm;
	}

	
	/**
	 * Metodo di risposta per l'invio dei dati al client
	 * @param rp
	 */
	private void send(ReceiveContent rp) {
		try {
			try {
			oos.writeObject(rp);
			} catch (IOException ex) {
				System.err.println("DEBUG: SERVER oos.writeObject(rp) " + ex.getMessage());
			}
			
			try {
			oos.flush();
			} catch (IOException ex) {
				System.err.println("DEBUG:  SERVER oos.flush() " + ex.getMessage());
			}
		} catch (Exception e) {
			Logger.WriteError(e, "ServerThread", "Send");
		}
	}

	/**
	 * SERVER 
	 * Risposta alla richiesta di ottenere la lista di dipartimenti
	 * 
	 */
	private void manageGetDipartimento() {
		try {
			ArrayList<Department> lista = Cache.getDipartimenti();
			ReceiveContent rp = new ReceiveContent();

			rp.parameters = new Object[] { lista };
			send(rp);
		} catch (Exception e) {

		}
	}

	/**
	 * Metodo che si occupa della crezione di un nuovo corso materia
	 * @param newCorso
	 */
	private void addNewCorsoMateria(Course newCorso) {
		try {
			Cache.inserisciNuovoCorsoMateria(DatabaseManager.conn, newCorso);
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	/**
	 * Metodo che risponde alla richiesta del client di ottenere i corsi materia avendo un corso di laurea
	 * @param idLaurea
	 */
	private void manageGetCorsoMateriaByLaurea(long idLaurea) {
		try {
			ArrayList<Course> lista = Cache.getCorsiMateriaByLaurea(idLaurea);
			ReceiveContent rp = new ReceiveContent();
			rp.parameters = new Object[] { lista };
			send(rp);
		} catch (Exception e) {
			System.out.println("Errore manageGetCorsoMateriaByLaurea");
		}
	}
	
	/**
	 * Server 
	 * Restituisco i corsi di laurea relativi a un singolo dipartimento
	 * @param idDip id del dipartimento di cui ricere i corsi di laurea
	 */
	private void manageGetCorsoLaurea(long idDip) {
		try {
			ReceiveContent rp = new ReceiveContent();
			rp.parameters = new Object[] { Cache.getCorsiLaureaByDip(idDip) };
			send(rp);
		} catch (Exception e) {

		}
	}

	/**
	 * Metodo che risponde alla richiesta del client di creazione di una sessione
	 * @param rp
	 */
	private void creaSezione(RequestContent rp) {
		try {
			FileSystemController.creaSez(DatabaseManager.conn, (Folder) rp.parameters[0], (long) rp.parameters[1]);
		} catch (Exception e) {
			System.out.println("Errore nella ManageCreaSezione");
		}
	}

	/**
	 * Metodo che si occupa di caricare un documento ricevuto dal client sul database
	 * @param rp
	 */
	private void manageUploadDoc(RequestContent rp) {
		FileSystemController.salvaDoc(DatabaseManager.conn, ((Document) rp.parameters[0]));
	}


	/**
	 * Metodo che si occupa di processare il login del client, restituendo le credenziali 
	 * in relazione al tipo di utente loggato. I dati saranno restituiti all'interno di un
	 * oggetto userdatas
	 * @param rp
	 */
	private void manageLogin(RequestContent rp) {
		Userdatas logged = null;
		switch (rp.userType) {
		case ADMIN:
			logged = AdminController.login(DatabaseManager.conn, /*Username*/rp.parameters[0].toString(), /*Password*/rp.parameters[1].toString());
			System.out.println("DEBUG: " + "ServerThread.ManageLogin() case rp.userType = AdminController ");
			break;
		case TEACHER:
			logged = DocenteController.login(DatabaseManager.conn, rp.parameters[0].toString(), rp.parameters[1].toString());
			System.out.println("DEBUG: " + "ServerThread.ManageLogin() case rp.userType = DocenteController ");
			break;
		case STUDENT:
			logged = StudentController.login(DatabaseManager.conn, rp.parameters[0].toString(), rp.parameters[1].toString());
			System.out.println("DEBUG: " + "ServerThread.ManageLogin() case rp.userType = StudentController ");
			break;
		default:
			System.out.println("DEBUG: " + "ServerThread.ManageLogin() rp.userType not admin, studente nor docente ");
			break;
		}
		ReceiveContent pr = new ReceiveContent();
		if (logged != null) {
			logged.t = rp.userType;
			this.setUtenteLoggato(new UserMonitoring(logged));
			LoggedUserController.loggedUserList.add(this.getUtenteLoggato());	// aggiunto l'utente appena connesso alla lista degli utenti loggati. 
			loggedUser = this.getUtenteLoggato();
			pr.parameters = new Object[] { logged };				// anagrafica ritornata alla vista
			System.out.println("DEBUG: " + "ServerThread.ManageLogin() oggetto NON vuoto ");
		} else {
			System.out.println("DEBUG: " + "ServerThread.ManageLogin() oggetto vuoto ");
			pr.parameters = new Object[] {"DEBUG: "+"Errore Oggetto Vuoto"};

		}

		try {
			send(pr);
		} catch (Exception e) {
			System.out.println("Errore invio risposta al client");
		}
	}

	
	/**
	 * Metodo che risponde alla richiesta del client di creare un nuovo utente del tipo specificato dal parametro 
	 * ricevuto
	 * @param rp
	 */
	private void createUser(RequestContent rp) {
		switch (rp.userType) {
		case ADMIN:
			AdminController.creaAdmin(DatabaseManager.conn, (Admin) rp.parameters[0]);
			break;
		case TEACHER:
			DocenteController.creaDocente(DatabaseManager.conn, (Teacher) rp.parameters[0]);
			break;
		case STUDENT:
			StudentController.creaStudente(DatabaseManager.conn, (Student) rp.parameters[0]);
			break;
		default:
			break;
		}
	}

	
	/**
	 * MMetodo che risponde alla richiesta del client di costruire il file system di un corso
	 * @param rp
	 */
	private void getFileSystem(RequestContent rp) {
		try {
			long idCorso = (long) rp.parameters[0];
			ReceiveContent res = new ReceiveContent();
			CourseFileSystem fs = FileSystemController.getFileSystem(DatabaseManager.conn, idCorso);
			if (fs != null) {
				res.parameters = new Object[] { fs };
			} else {
				res.parameters = new Object[] { new CourseFileSystem() };
			}
			send(res);
		} catch (Exception e) {
			System.out.println("Errore nell'invio del pacchetto managedownloaddoc");
			System.out.println("e.printStackTrace()");
			e.printStackTrace();
		}
	}

	/**
	 * Metodo che risponde alla richiesta del client di ottenere un file salvato sul database
	 * @param rp
	 */
	private void downloadDocument(RequestContent rp) {
		try {
			long idDoc = (long) rp.parameters[0];
			ReceiveContent res = new ReceiveContent();
			res.parameters = new Object[] { FileSystemController.downloadDocumento(DatabaseManager.conn, idDoc) };
			send(res);
		} catch (Exception e) {
			System.out.println("Errore nell'invio del pacchetto managedownloaddoc");
		}
	}

	/**
	 * Metodo run del thread 
	 */
	public void run() {
		try {
			
			System.out.println("DEBUG: Thread del server avviato");
			
			boolean uscita = false;
			oos = new ObjectOutputStream(s.getOutputStream());
			ois = new ObjectInputStream(s.getInputStream());
			while (!uscita) {
				RequestContent rp = null;
				
					
					// String s = (String) ois.readObject();
					// oos.writeObject("ciao client");
					
					try {
					rp = (RequestContent) ois.readObject();
					// Attende la lettura di un oggetto che non arriva. Per non bloccare il codice, viene generato un oggetto vuoto. 
				} catch(Exception ex) {
					ex.getMessage();
					System.out.println("DEBUG: ServerThread.run(): rp = (RequestContent) ois.readObject()"); 
					ex.printStackTrace();
					try {
					System.out.println("rp.type " + rp.type);
					} catch (NullPointerException ess) {
						ess.getMessage();
						System.out.println("DEBUG: ServerThread.run(): rp.type "  + rp.type); 
						ess.printStackTrace();
					}
				}
				switch (rp.type) {

				case LOGIN_USER:
					System.out.println("DEBUG: Login Case");
					manageLogin(rp);
					break;

				case CREATE_USER:
					createUser(rp);
					break;

				case GET_FILE_SYSTEM:
					getFileSystem(rp);
					break;
				case DOWNLOAD_DOCUMENT:
					downloadDocument(rp);
					break;
				case CREATE_FOLDER:
					creaSezione(rp);
					break;
				case UPLOAD_DOCUMENT:
					manageUploadDoc(rp);
					break;
				case PASSWORD_FORGET:
					managePasswordDimenticata(rp);
					break;
				case CONFIRM_ACTIVATION_CODE:
					break;
				case GET_DEPARTMENTS:
					System.out.println("DEBUG: SERVER ServerThread.run() " + "GET_DEPARTMENTS ");
					manageGetDipartimento();
					break;
				case GET_DEGREE_COURSES:
					System.out.println("DEBUG: SERVER ServerThread.run() " + "GET_DEGREE_COURSES ");
					Long idDip = (Long) rp.parameters[0];
					manageGetCorsoLaurea(idDip);
					break;
				case GET_COURSES:
					long idCorsoLaurea = (long) rp.parameters[0];
					manageGetCorsoMateriaByLaurea(idCorsoLaurea);
					break;
				case CREATE_COURSE:
					addNewCorsoMateria((Course) rp.parameters[0]);
					break;
				case GET_ALL_TEACHERS:
					manageGetAllDocenti();
					break;
				case UPDATE_LOC:
					manageUpdateLoc(rp);
					break;
				case GET_SECONDS_AVERAGE:
					manageGetMediaCorso(rp);
					break;
				case CLOSE:
					if (this.getUtenteLoggato() != null) {
						LoggedUserController.loggedUserList.remove(this.getUtenteLoggato());
					}
					uscita = true;
					break;
				case GET_LOGGED_USER_NUMBER:
					manageGetAllUserLogged();
					break;
				case GET_LOGGED_NUMBER_BY_CORSO:
					manageGetAllUserLoggedByCorso(rp);
					break;
				case GET_NUMBER_ACCESS_BY_PERIOD:
					manageGetNumberAccessByPeriod(rp);
					break;
				case UPDATE_DOCUMENT_COUNT:
					updateDocumentCount(rp);
					break;
				case DOCUMENT_COUNT_BY_CORSO:
					manageDocumentContByCorso(rp);
					break;
				case DOWNLOAD_USER_COUNT:
					manageContUtentiDownload(rp);
					break;
				case GET_ALL_COURSES:
					manageGetAllCorsiMateria();
					break;
				case SUBSCRIVE_COURSE:
					manageSubscribeCourse(rp);
					break;
				case VERIFY_COD:
					manageVerificaCod(rp);
					break;
				case GET_STUDENT_COURSES:
					manageGetCorsiStud(rp);
					break;
				case MODIFY_COURSE:
					manageModificaCorsoMateria(rp);
					break;
				case SEND_NEWSLETTER_TO_ALL_STUDENTS:
					sendNewsLetterToAllStudents(rp);
					break;
				case SEND_NEWSLETTER_TO_SOME_USERS: 
					sendNewsLetterToSomeStudents(rp);
					break;
				case LINK_DOCENTE_CORSO:
					manageLinkDocenteCorso(rp); 
					break;
				case PASSWORD_CHANGE:
					manageCambiaPassword(rp);
					break;
				case MODIFY_USER_DATA:
					manageModificaAnagrafica(rp);
					break;
				case COURSE_DESCRIPTION: 
					courseDescription(rp);
					break;
				case GET_ALL_STUDENTS:
					manageGetAllStudenti();
					break;
				case GET_COURSE_DOCUMENTS:
					manageGetCorsiDoc(rp);
					break;
				case GET_STUDENT_BY_USERID:
					manageGetStudenteByMat(rp);
					break;
				case GET_DEGREE_COURSES_BY_ID:
					manageGetCorsoLaureaByID(rp);
					break;
				case CHANGE_FOLDER_VISIBILITY:
					manageCambiaVisSez(rp);
					break;
				case UPDATE_DEGREE_COURSE: // ricevo tutti i corsi di laurea nel db
//					 Cache.UpdateCorsiLaurea(DatabaseManager.conn);
					 getAllCorsiLaurea(rp);
					break;
				case UPDATE_FOLDER_DESCRIPTION:
					 updateSezDescription(rp);
					break;
				case IMPORT_CSV_FILE: 
					importCSV(rp);
					break;
				case GET_USER_TYPE:
					getUserType(rp);
					break;
				case DELETE_USER: 
					deleteUser(rp);
					break;
				case GET_ALL_USERS: 
					getAllUsersList(rp);
					break;
				case UNLOCK_USER:
					unlockUser(rp);
					break;
				case DELETE_COURSE:
					deleteCorsoMateria(rp);
					break;
				case GET_STUDENTS_BY_COURSE:
					getStudentiByCorso(rp);
					break;
				case GET_LOCKED_TEACHERS: 
					getLockedDocenti(rp);
					break;
				case GET_LOCKED_STUDENTS:
					getLockedStudenti(rp);
					break;
				case GET_LOCKED_ADMINS:
					getLockedAdmin(rp);
					break;
				case INCREMENT_LOGIN_ATTEMPTS: 
					incNumeroTentativiLogin(rp);
					break;
				case GET_LOGIN_ATTEMPTS_NUMBER: 
					getNumeroTentativiLogin(rp);
					break;
				case SET_ZERO_LOGIN_ATTEMPTS:
					setZeroNumeroTentativiLogin(rp);
					break;
				case GET_DOCUMENTS: 
					getDocumentList(rp);
					break;
				case UPDATE_FILE_DESCRIPTION:
					updateFileDescription(rp);
					break;
				case GET_EMAIL_TEACHER_FROM_CORSO:
					emailDocentefromCorso(rp);
					break;
				case UPDATE_DOWNLOAD_COUNT:
					updateDownloadCont(rp);
					break;
				case GET_DOCTYPE_BY_DOCNAME: 
					getDocTypeByDocName(rp);
					break;
				case DELETE_FILE: 
					eliminaFile(rp);
					break;
				case DELETE_FOLDER:
					eliminaSezione(rp);
					break;
				case GET_ALL_ADMIN:
					getAllAdmin(rp);
				break;
					
				
				default:
					System.out.println("DEBUG: case default:  Chiusura del Thread ");
					uscita = true;
					break;

				}
			}

			s.close();
		} catch (Exception e) {
			// In caso di eccezione chiude il socket se per qualche motivo non fosse già
			// chiuso
			System.out.println(e.toString() 
					+ "\n e.getMessage(): " + e.getMessage() 
					+ "\n  e.getCause(): " + e.getCause());
			System.out.print("e.printStackTrace(): "); e.printStackTrace();
			
			System.out.println("Chiusura socket inaspettata");
			LoggedUserController.loggedUserList.remove(this.getUtenteLoggato());
			try {
				this.s.close();
			} catch (IOException e1) {
			}
		}
	}
	
	private void getAllAdmin(RequestContent rp) {
		ArrayList<Admin> getAllAdmin = AdminController.getAllAdmin(DatabaseManager.conn);
		ReceiveContent rpp = new ReceiveContent();
		rpp.parameters = new Object[] { getAllAdmin };
		send(rpp);
	}


	/**
	 * Metodo che risponde alla richiesta del client di ottenere il tipo del documento dove il nome
	 * viene ricevuto come parametro
	 * @param rp
	 */
	private void getDocTypeByDocName(RequestContent rp) {
		String docName = (String) rp.parameters[0];
		String docType = FileSystemController.getDocTypeByDocName(DatabaseManager.conn, docName);
		ReceiveContent rpp = new ReceiveContent();
		rpp.parameters = new Object[] { docType };
		send(rpp);
	}

	/**
	 * Metodo che ritorna le mail dei docenti che insegnano un corso passato come parametro
	 * @param rp
	 */
	private void emailDocentefromCorso(RequestContent rp) {
		long idCorso = (long) rp.parameters[0]; 
		ArrayList<Teacher> emailDocentefromCorso = DocenteController.emailDocentefromCorso(DatabaseManager.conn, idCorso);
		ReceiveContent rpp = new ReceiveContent();
		rpp.parameters = new Object[] { emailDocentefromCorso };
		send(rpp);
	}

	/**
	 * Metodo per cambiare la descrizionedel file passato come parametro 
	 * @param rp
	 */
	private void updateFileDescription(RequestContent rp) {
		long idSezione = (long) rp.parameters[0]; 
		String description = (String) rp.parameters[1];
		FileSystemController.updateFileDescription(DatabaseManager.conn, idSezione, description);
	}

	/**
	 * Metodo che risponde alla richiesta del client di ottenere una lista di studenti dato un corso 
	 * ricevuto come parametro
	 * @param rp
	 */
	private void getStudentiByCorso(RequestContent rp) {
		long idCorso = (long) rp.parameters[0];
		ArrayList<Student> anagListByCorso = StudentController.getStudentiByCorso(DatabaseManager.conn, idCorso);
		ReceiveContent rpp = new ReceiveContent();
		rpp.parameters = new Object[] { anagListByCorso };
		send(rpp);
	}

	/**
	 * Metodo per chiedere al DB una lista utenti registrati
	 * @param rp
	 */
	private void getAllUsersList(RequestContent rp) {
		ArrayList<Userdatas> anagList = AnagraficaController.getAllUsersList(DatabaseManager.conn);
		ReceiveContent rpp = new ReceiveContent();
		rpp.parameters = new Object[] { anagList };
		send(rpp);
	}

	/**
	 * Metodo che risponde alla richiesta del client di ottenere la descrizione di un corso
	 * @param rp
	 */
	private void courseDescription(RequestContent rp) {
		long idCorso = (long) rp.parameters[0];
		String descr = CorsiLaureaController.courseDescription(DatabaseManager.conn, idCorso);
		ReceiveContent rpp = new ReceiveContent();
		rpp.parameters = new Object[] { descr };
		send(rpp);
		
	}

	/**
	 * Metodo che risponde alla richiesta del client di eliminazione di una cartella presente sul database
	 * @param rp
	 */
	private void eliminaSezione(RequestContent rp) {
		// TODO Auto-generated method stub
		long userSezione = (long) rp.parameters[0];
		FileSystemController.eliminaSezione(DatabaseManager.conn, userSezione);
	}

	/**
	 * Metodo per eliminare un file dal DB (relazione contenuto) all'ID sezione specificato
	 * @param rp
	 */
	private void eliminaFile(RequestContent rp) {
		long userSezione = (long) rp.parameters[0];
		FileSystemController.eliminaFile(DatabaseManager.conn, userSezione);
	}

	/**
	 * Metodo per aggiornare il conteggio dei download di un file
	 * @param rp
	 */
	 private void updateDownloadCont(RequestContent rp) {
		 long userId = (long) rp.parameters[0];  
		 long docId = (long) rp.parameters[1]; 
		 FileSystemController.updateDownloadCont(DatabaseManager.conn, docId, userId);
	}

	/**
	  * Metodo per ottenere dal server la lista dei documenti presenti sul DB
	  * @param rp
	  */
	 private void getDocumentList(RequestContent rp) {
		 ArrayList<Document> getDocumentList = null;
		 getDocumentList = FileSystemController.getDocumentList(DatabaseManager.conn);
			ReceiveContent rpp = new ReceiveContent();
			rpp.parameters = new Object[] { getDocumentList };
			send(rpp);
		}

/**	
  * Metodo per ottenere il numero di tentativi relativi ad un utente memorizzato sul DB
  * @param rp
  */
 private void getNumeroTentativiLogin(RequestContent rp) {
	  String email = (String) rp.parameters[0];
		long incNumeroTentativiLogin = AnagraficaController.getNumeroTentativiLogin(DatabaseManager.conn, email);
		ReceiveContent rpp = new ReceiveContent();
		rpp.parameters = new Object[] { incNumeroTentativiLogin };
		send(rpp);
	}
	
/**
 * Metodo che si occupa di resettare il numero di tentativi di login computi dall'utente 
 * specificato tramire il pramatetro ricevuto via mail
 * @param rp
 */
 private void setZeroNumeroTentativiLogin(RequestContent rp) {
	  	String email = (String) rp.parameters[0];
		AnagraficaController.setZeroNumeroTentativiLogin(DatabaseManager.conn, email);
	}
 
 /**
  * Metodo che risponde alla richiesta del client di aumentare il numero di tentativi errati compiuti dall'utente
  * @param rp
  */
  private void incNumeroTentativiLogin(RequestContent rp) {
	  	String email = (String) rp.parameters[0];
		AnagraficaController.incNumeroTentativiLogin(DatabaseManager.conn, email);
	}

/**
   * Metodo che ritorna al client una lista di docenti bloccati
   * @param rp
   * @return
   */
  private void getLockedDocenti (RequestContent rp) {
	  ArrayList<Teacher> getLockedDocenti = DocenteController.getLockedDocenti(DatabaseManager.conn);
		ReceiveContent rpp = new ReceiveContent();
		rpp.parameters = new Object[] { getLockedDocenti };
		send(rpp);
  }
  
  /**
   * Metodo che ritorna al client una lista di studenti bloccati
   * @param rp
   * @return
   */
  private void getLockedStudenti (RequestContent rp) {
	  ArrayList<Student> getLockedStudenti = StudentController.getLockedStudenti(DatabaseManager.conn);
		ReceiveContent rpp = new ReceiveContent();
		rpp.parameters = new Object[] { getLockedStudenti };
		send(rpp);
  }
  
  /**
   * Metodo che ritorna al client una lista di admin bloccati
   * @param rp
   * @return
   */
  private void getLockedAdmin (RequestContent rp) {
	  ArrayList<Admin> getLockedAdmin = AdminController.getLockedAdmin(DatabaseManager.conn);
		ReceiveContent rpp = new ReceiveContent();
		rpp.parameters = new Object[] { getLockedAdmin };
		send(rpp);
  }
	
  /**
   * Metodo che risponde alla richiesta del client di cancellare un corso materia presente sul database
   * @param rp
   */
   private void deleteCorsoMateria(RequestContent rp)  {
	   long idCorso = (long) rp.parameters[0];
	   // chiamo il controller corrispondente
	   CorsiLaureaController.deleteCorsoMateria(DatabaseManager.conn,idCorso);
	   
   }
	
	/**
	 * Metodo che permette ad un admin di sbloccare un profilo che, a seguito di 10 tentativi di accesso, è stato bloccato
	 * @param rp
	 */
	private void unlockUser(RequestContent rp) {
		long matricola = (long) rp.parameters[0];
		// chiamo il controller corrispondente
		AnagraficaController.unlockUser(DatabaseManager.conn, matricola);
	}

	/**
	 * Metodo che rende disponibili tutti i corsi di laurea presenti sul database
	 * @param rp
	 */
	private void getAllCorsiLaurea(RequestContent rp) {
		ArrayList<DegreeCourse> corsiLaurea = Cache.getCorsi();
		ReceiveContent rpp = new ReceiveContent();
		rpp.parameters = new Object[] { corsiLaurea };
		send(rpp);
	}
	
	/**
	 * Aggiona
	 * @param rp
	 */
	private void updateSezDescription(RequestContent rp) {
		long id = (long) rp.parameters[0];
		String description = (String) rp.parameters[1];
		FileSystemController.updateSezDescription(DatabaseManager.conn, id, description);
		
		
//		ReceiveContent rpp = new ReceiveContent();
//		Send(rpp);
	}
	
	/**
	 * Metodo che risponde alla richiesta del client di eliminare un utente presente sul database
	 * @param rp
	 */
	private void deleteUser(RequestContent rp) {
		long matricola = (long) rp.parameters[0];
		// chiamo il controller che la query
		AnagraficaController.deleteUser(DatabaseManager.conn, matricola);
	}
	
	/**
	 * Metodo che restituisce il tipo dell'utente specificto dal parametro ricevuto
	 * @param rp
	 */
	private void getUserType(RequestContent rp) {
		long id = (long) rp.parameters[0];
		UserType found = StudentController.getUserType(DatabaseManager.conn, id);
	}
	
	/**
	 * Metodo che risponde alla richiesta del client di importare un file CSV 
	 * @param rp
	 */
	private void importCSV(RequestContent rp) { 
		byte[] byteReceived = null;
		Object[] objectReceived = null;
		if (rp.parameters[0] instanceof Object[]) {
			objectReceived = (Object[]) rp.parameters[0];
		} else {
			byteReceived = (byte[]) rp.parameters[0];
		}
		UserType userType = (UserType) rp.userType;
		new FileSystemController();
		// Se ricevo un array di byte significa che sto ricevendo il CSV di un utente
		if (rp.parameters[0] instanceof byte[]) {
			FileSystemController.importCSV(DatabaseManager.conn, byteReceived, userType);
		} 
		// Se ricevo un array di Object significa che sto ricevendo il CSV di un corso
		else {
			FileSystemController.importCSV(DatabaseManager.conn, objectReceived);
		}
		
	}
	
	
	/**
	 * Metodo che risponde alla richiesta del client di cambiare la visibilità di una cartella. 
	 * @param rp
	 */
	private void manageCambiaVisSez(RequestContent rp) {
		try {
			long idSez = (long) rp.parameters[0];
			FolderVisibility vis = (FolderVisibility) rp.parameters[1];
			FileSystemController.changeVisSez(DatabaseManager.conn, idSez, vis);
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	
	/**
	 * Metodo che risponde alla richiesta del client di ottenere dal db un corso di laurea dato il suo id
	 * ricevuto come parametro
	 * @param rp
	 */
	private void manageGetCorsoLaureaByID(RequestContent rp) {
		try {
			long corsoID = (long) rp.parameters[0];
			DegreeCourse l = Cache.getCorsoLaurea(corsoID);
			ReceiveContent rpp = new ReceiveContent();
			rpp.parameters = new Object[] { l };
			send(rpp);
		} catch (Exception e) {

		}
	}

	
	/**
	 * Metodo che risponde alla richiesta del client di ottenere uno studente ricevuta la matricola come 
	 * parametro
	 * @param rp
	 */
	private void manageGetStudenteByMat(RequestContent rp) {
		ReceiveContent rp1 = new ReceiveContent();
		long matricola = (long) rp.parameters[0];
		rp1.parameters = new Object[] { StudentController.getStudenteByMat(DatabaseManager.conn, matricola) };
		send(rp1);
	}

	
	/**
	 * Metodo che risponde alla richiesta del client di ottenere i corsi tenuti da un docente,
	 * di cui l'id è ricevuto come parametro
	 * @param rp
	 */
	private void manageGetCorsiDoc(RequestContent rp) {
		try {
			long idDocente = (long) rp.parameters[0];
			ArrayList<Course> lista = DocenteController.getCorsiDocente(DatabaseManager.conn, idDocente);
			ReceiveContent rp1 = new ReceiveContent();
			rp1.parameters = new Object[] { lista };
			send(rp1);
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	
	/**
	 * Metodo che risponde alla richiesta del client di ricevere tutti gli studenti
	 */
	private void manageGetAllStudenti() {
		try {
			ReceiveContent rp = new ReceiveContent();
			ArrayList<Student> lista = StudentController.getAllStudenti(DatabaseManager.conn);
			rp.parameters = new Object[] { lista };
			send(rp);
		} catch (Exception e) {

		}

	}

	/**
	 * Metodo che risponde alla richiesta del client di aggiornare l'anagrafica di un utente presente sul 
	 * database
	 * @param rp
	 */
	private void manageModificaAnagrafica(RequestContent rp) {
		try {
			Userdatas mod = (Userdatas) rp.parameters[0];
			AnagraficaController.updateAnagrafica(DatabaseManager.conn, mod);
		} catch (Exception e) {

		}
	}

	/**
	 * Metodo che risponde alla richiesta del client di cambiare la password presente sul profilo
	 * di un utente memorizzato sul db
	 * @param rp
	 */
	private void manageCambiaPassword(RequestContent rp) {
		try {
			String email = (String) rp.parameters[0];
			String newPsw = (String) rp.parameters[1];
			AnagraficaController.cambiaPsw(DatabaseManager.conn, email, newPsw);
		} catch (Exception e) {

		}

	}

	/**
	 * Metodo che risponde alla richiesta del client di ottenere il corso tenuto dal docente
	 * @param rp
	 */
	private void manageLinkDocenteCorso(RequestContent rp) {
		try {
			long idDocente = (long) rp.parameters[0];
			long idCorso = (long) rp.parameters[1];
			DocenteController.linkDocenteCorso(DatabaseManager.conn, idDocente, idCorso);
		} catch (Exception e) {
			System.out.println("Error ManageLinkDocenteCorso");
		}
	}

	/**
	 * Metodo per inviare una mail a tutti gli studentiiscritti ad un corso
	 * @param rp
	 */
	private void sendNewsLetterToAllStudents(RequestContent rp) {
		try {
			long idCorso = (long) rp.parameters[0];
			String oggetto = (String) rp.parameters[1];
			String messaggio = (String) rp.parameters[2];
			CorsiLaureaController.sendNewsLetterToAllStudents(DatabaseManager.conn, idCorso, oggetto, messaggio);
		} catch (Exception e) {
			System.out.println("Error SendNewsLetter");
		}
	}
	
	/**
	 * Metodo per l'invio di mail ad un gruppo specifico di utenti
	 * @param rp
	 */
	private void sendNewsLetterToSomeStudents(RequestContent rp) {
		long idCorso = (long) rp.parameters[0];
		String oggetto = (String) rp.parameters[1];
		String messaggio = (String) rp.parameters[2];
		@SuppressWarnings("unchecked")
		ArrayList<String> emailStudenti = (ArrayList<String>) rp.parameters[3];
		CorsiLaureaController.sendNewsLetterToSomeStudents(DatabaseManager.conn, idCorso, oggetto, messaggio, emailStudenti);
	}

	/**
	 * Metodo che risponde alla richiesta del client di modificare i dati presenti sul db relativi a un corso materia
	 * @param rp
	 */
	private void manageModificaCorsoMateria(RequestContent rp) {
		try {
			Course corso = (Course) rp.parameters[0];
			CorsiLaureaController.updateCorsoMateria(DatabaseManager.conn, corso);
		} catch (Exception e) {
			System.out.println("Errore MODIFY_COURSE");
		}
	}

	
	/**
	 * Metodo che risponde alla richiesta del client di cambio password 
	 * @param rp
	 */
	private void managePasswordDimenticata(RequestContent rp) {
		try {
			String Email = (String) rp.parameters[0];
			AnagraficaController.bloccaAccount(DatabaseManager.conn, Email);
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	/**
	 * Metodo che risponde alla richiesta del client di ottenere i corsi di uno studente
	 * @param rp
	 */
	private void manageGetCorsiStud(RequestContent rp) {
		try {
			long userID = (long) rp.parameters[0];
			ArrayList<Course> lista = StudentController.reperisciCorsi(DatabaseManager.conn, userID);
			ReceiveContent rpp = new ReceiveContent();
			rpp.parameters = new Object[] { lista };
			send(rpp);
		} catch (Exception e) {
			Logger.WriteError(e, "ServerThread", "ManageGetCorsiStud");
		}
	}

	
	/**
	 * Metodo che risponde alla richiesta del client di verificare il codice di attivazione ricevuto come parametro.
	 * @param rp
	 */
	private void manageVerificaCod(RequestContent rp) {
		try {
			long userID = (long) rp.parameters[0];
			long cod = (long) rp.parameters[1];
			boolean ris = AnagraficaController.verificaCod(DatabaseManager.conn, userID, cod);
			ReceiveContent rpp = new ReceiveContent();
			rpp.parameters = new Object[] { ris };
			send(rpp);
		} catch (Exception e) {
			Logger.WriteError(e, "ServerThread", "ManageVerificaCod");
		}
	}

	/**
	 * Metodo che risponde alla richiesta del client di iscrizione a un corso
	 * @param rp
	 */
	private void manageSubscribeCourse(RequestContent rp) {
		try {
			long idUtente = (long) rp.parameters[0];
			long idCorso = (long) rp.parameters[1];
			StudentController.subscribeCourse(DatabaseManager.conn, idUtente, idCorso);
		} catch (Exception e) {
			Logger.WriteError(e, "ServerThread", "ManageSubscribeCourse");
		}
	}

	/**
	 * Metodo che risponde alla richiesta del client di ottenere una lista di tutti i corsi 
	 * materia presenti sul database
	 */
	private void manageGetAllCorsiMateria() {
		try {
			ReceiveContent rp = new ReceiveContent();
			rp.parameters = new Object[] { Cache.getAllCorsiMateria(DatabaseManager.conn) };
			send(rp);
		} catch (Exception e) {
			Logger.WriteError(e, "ServerThread", "ManageGetAllCorsiMateria");
		}
	}
	
	/**
	 * Metodo per gestire l'incremento del numero di download di un file da rendere noto al server. 
	 * @param rp
	 */
	private void updateDocumentCount(RequestContent rp) {

//		FileSystemController.updateDownloadCont(db.conn, docID, userID);
	}

	/**
	 * Metodo che risponde alla richiesta del client di ottenere il numero di accessi ad un corso effettuati
	 * in un periodo
	 * @param rp
	 */
	private void manageGetNumberAccessByPeriod(RequestContent rp) {
		try {
			long idCorso = (long) rp.parameters[0];
			Date start = (Date) rp.parameters[1];
			Date end = (Date) rp.parameters[2];
			int cont = CorsiLaureaController.getAccessByPeriod(DatabaseManager.conn, idCorso, start, end);
			ReceiveContent resP = new ReceiveContent();
			resP.parameters = new Object[] { cont };
			send(resP);
		} catch (Exception e) {
			System.out.println("ERR ManageGetNumberAccessByPeriod");
		}
	}

	/**
	 * Metodo che risponde alla richiesta del client di conoscere gli utenti connessi ad un
	 * corso
	 * @param rp
	 */
	private void manageGetAllUserLoggedByCorso(RequestContent rp) {
		try {
			long idCorso = (long) rp.parameters[0];
			int cont = LoggedUserController.getUserNumberCorso(idCorso);
			ReceiveContent resP = new ReceiveContent();
			resP.parameters = new Object[] { cont };
			send(resP);
		} catch (Exception e) {

		}
	}

	/**
	 * Metodo che risponde alla richiesta del client di ottenere una lista di tutti gli utenti loggati
	 */
	private void manageGetAllUserLogged() {
		try {
			int LoggedUserN = LoggedUserController.loggedUserList.size();
			ReceiveContent rp = new ReceiveContent();
			rp.parameters = new Object[] { LoggedUserN };
			send(rp);
		} catch (Exception e) {

		}
	}

	/**
	 * Metodo che risponde alla richiesta del client di ottenere una media del tempo di connessione 
	 * al corso degli utenti che lo seguono
	 * @param rp
	 */
	private void manageGetMediaCorso(RequestContent rp) {
		try {
			long idCorso = (long) rp.parameters[0];
			double secMedi = CorsiLaureaController.tempoMedioConnessione(DatabaseManager.conn, idCorso);
			ReceiveContent resP = new ReceiveContent();
			resP.parameters = new Object[] { secMedi };
			send(resP);
		} catch (Exception e) {
			System.out.println("Err ManageGetMedia");
		}

	}

	/**
	 * Metodo che risponde alla richiesta del client di conoscere quanti documenti sono stati scaricati all'interno di
	 * corso 
	 * @param rp
	 */
	private void manageDocumentContByCorso(RequestContent rp) {
		try {
			long idCorso = (long) rp.parameters[0];
			int cont = FileSystemController.contDownloadByCorso(DatabaseManager.conn, idCorso);
			ReceiveContent resP = new ReceiveContent();
			resP.parameters = new Object[] { cont };
			send(resP);
		} catch (Exception e) {
			System.out.println("Err ManageDocumentContByCorso");
		}

	}

	/**
	 * Metodo che risponde alla richiesta del client di conoscere quanti utenti hanno scaricato 
	 * una risorsa presente sul db
	 * @param rp
	 */
	private void manageContUtentiDownload(RequestContent rp) {
		try {
			Date start = (Date) rp.parameters[0];
			Date end = (Date) rp.parameters[1];
			int cont = FileSystemController.contUtentiDownload(DatabaseManager.conn, start, end);
			ReceiveContent resP = new ReceiveContent();
			resP.parameters = new Object[] { cont };
			send(resP);
		} catch (Exception e) {
			System.out.println("Err ManageDocumentContByCorso");
		}

	}

	/**
	 * Metodo che risponde alla richiesta del client di aggiornare la posizione di un utente 
	 * @param rp
	 */
	private void manageUpdateLoc(RequestContent rp) {
		long idCorso = (long) rp.parameters[0];
		long idUtente = (long) rp.parameters[1];
		LoggedUserController.updateLocUser(idCorso, idUtente, DatabaseManager.conn);

	}

	/**
	 * Metodo che risponde alla richiesta del client di ottenere una lista di tutti i docenti presenti sul db
	 */
	private void manageGetAllDocenti() {
		try {
			ArrayList<Teacher> doc = DocenteController.getAllDocenti(DatabaseManager.conn);
			ReceiveContent rp = new ReceiveContent();
			rp.parameters = new Object[] { doc };
			send(rp);
		} catch (Exception e) {
			System.out.println("Errore ManageGetAllDocenti");
		}
	}

	/**
	 * Metodo che ritorna l'utente attualmente loggato
	 * @return
	 */
	public UserMonitoring getUtenteLoggato() {
		return utenteLoggato;
	}

	/**
	 * Metodo che imposta il nuovo utente attualmente loggato
	 * @param utenteLoggato da impostare
	 */
	public void setUtenteLoggato(UserMonitoring utenteLoggato) {
		this.utenteLoggato = utenteLoggato;
	}
}
