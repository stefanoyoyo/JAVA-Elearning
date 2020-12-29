package Admin.GUI;
import java.awt.EventQueue;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import Client.ClientConnection;
import Common.DBType.Admin;
import Common.DBType.Help;
import Common.DBType.Userdatas;
import Common.DBType.Teacher;
import Common.DBType.Student;
import Common.Enumerators.UserType;

import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;



/**
 *  GUI
 * 
 * @author Amenta Stefano, Moroni Paolo
 *
 */
public class Guide {

	private JFrame frmGuide;
	public static Help helpObj;
	private static ArrayList<String> guideSelectedList = new ArrayList<String>();
	public static ArrayList<Help> helpList;

	/**
	 * Launch the application.
	 */
	public static void main() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Guide window = new Guide();
					window.frmGuide.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Guide() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmGuide = new JFrame();
		frmGuide.setResizable(false);
		frmGuide.setTitle("Guide");
		frmGuide.setBounds(100, 100, 497, 393);
		frmGuide.setIconImage(Toolkit.getDefaultToolkit().getImage("media/f.png"));
		frmGuide.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				frmGuide.dispose();
				MainAdmin.main();
				
			}
		});
		frmGuide.getContentPane().setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 11, 471, 302);
		frmGuide.getContentPane().add(scrollPane);
		DefaultListModel<String> model = new DefaultListModel<String>();
		JList<String> UserList = new JList<String>();
		scrollPane.setViewportView(UserList);
		
		helpList = initGuida();
		
		
		for (Help help : helpList) {
			
			String nome_cognome = "<html><p style=\"color:red; display:inline\">GUIDA SU: <span style='color:black'>" + help.title + "</span></p>"
					+ "<p>&nbsp</p>"
					+ " </html>";
			
			String temp = new Long(help.id).toString();
			temp = temp.concat("-");
			temp = temp.concat(help.title);
			temp = temp.concat("-");
			temp = temp.concat(help.description);
			
			try {
				if(temp!=null)
			guideSelectedList.add(temp);
			} catch (NullPointerException nul) {
				
			}

			model.addElement(nome_cognome);
		}
		
		
		
		UserList.setModel(model);
		
		JButton btnNewButton = new JButton("Indietro");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frmGuide.dispose();
				MainAdmin.main();
			}
		});
		btnNewButton.setBounds(10, 324, 203, 23);
		frmGuide.getContentPane().add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("Informazioni");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GuideDescription.main();
				frmGuide.dispose();
			}
		});
		btnNewButton_1.setBounds(239, 324, 242, 23);
		frmGuide.getContentPane().add(btnNewButton_1);

		
				UserList.addListSelectionListener(new ListSelectionListener() {
		
					@Override
					public void valueChanged(ListSelectionEvent arg0) {
						
						String s = (String) UserList.getSelectedValue();
						long inputLong = -1;
						
					if(s.contains("Bottone Registra nuovo utente")) 
						inputLong = 1;
						
						if(s.contains("Bottone visualizza corsi"))
						inputLong = 2;
						
						if(s.contains("Bottone assegna corso"))
						inputLong = 3;

						if(s.contains("Bottone modifica utenti"))
						inputLong = 4;
						
						if(s.contains("Bottone monitoraggio piattaforma"))
						inputLong = 5;

						if(s.contains("Bottone crea corso"))
						inputLong = 6;
						
						if(s.contains("Bottone importa utenti"))
						inputLong = 7;

						if(s.contains("Bottone importa corsi"))
						inputLong = 8;
						
						if(s.contains("Bottone elimina utente"))
						inputLong = 9;

						if(s.contains("Bottone elimina corso"))
						inputLong = 10;

						if(s.contains("Bottone sblocca profilo sospeso"))
						inputLong = 11;

						System.out.println("DEBUG: inputLong: " + inputLong);
						

						for (Help help : helpList) {
							if (help.id == inputLong) {
								helpObj = help;
								break;
							}
		
						}
						
		
					}
				});
		
		

	}
	
	/**
	 * Metodo per aggiornare la JUserList con i valori corretti
	 * @param model
	 * @param UserList
	 */
	public static void updateUserList(DefaultListModel<String> model, JList<String> UserList) {
		ArrayList<Teacher> L_docenti = ClientConnection.getAllDocenti();
		ArrayList<Student> L_studenti = ClientConnection.getAllStudenti();
		for (Teacher D : L_docenti) {
			
			String nome_cognome = "<html><p style=\"color:red; display:inline\">MATRICOLA: <span style='color:black'>" + D.userID + "</span></p>"
					+ "<p style=\"color:red;\">NOME: <span style='color:black'>" + D.name + "</span> </p>"
					+ "<p style=\"color:red;\">COGNOME: <span style='color:black'>" + D.surname + "</span> </p>"
					+ "<p style=\"color:red;\">EMAIL: <span style='color:black'>" + D.Email + "</span> </p>"
					+ "<p>&nbsp</p>"
					+ " </html>";
			
			String temp = new Long(D.userID).toString();
			temp = temp.concat("-");
			temp = temp.concat(D.surname);
			temp = temp.concat("-");
			temp = temp.concat(D.name);
			temp = temp.concat("-");
			temp = temp.concat(D.Email);
			try {
				if(temp!=null)
			guideSelectedList.add(temp);
			} catch (NullPointerException nul) {
				
			}

			model.addElement(nome_cognome);
		}
	
		for (Student S : L_studenti) {
			
			String nome_cognome = "<html><p style=\"color:red; display:inline\">MATRICOLA: <span style='color:black'>" + S.userID + "</span></p>"
					+ "<p style=\"color:red;\">NOME: <span style='color:black'>" + S.name + "</span> </p>"
					+ "<p style=\"color:red;\">COGNOME: <span style='color:black'>" + S.surname + "</span> </p>"
					+ "<p style=\"color:red;\">EMAIL: <span style='color:black'>" + S.Email + "</span> </p>"
					+ "<p>&nbsp</p>"
					+ " </html>";
			
			String temp = new Long(S.userID).toString(); 
			temp = temp.concat("-");
			temp = temp.concat(S.surname);
			temp = temp.concat("-");
			temp = temp.concat(S.name);
			temp = temp.concat("-");
			temp = temp.concat(S.Email);
			try {
				if(temp!=null)
			guideSelectedList.add(temp);
			} catch (NullPointerException nul) {
				
			}
//			System.out.println("DEBUG: guideSelectedList.size(): " + guideSelectedList.size() );

			model.addElement(nome_cognome);
		}
		
		UserList.setModel(model);
		
	}
	
	public ArrayList<Help> initGuida() {
		Help registraNuovoUente = new Help(1,
				"Bottone Registra nuovo utente",
				"Questo bottone consente la registrazione di un nuovo studente/docente,"
				+ " inserendo all'interno dei campi di testo le sue anagrafiche. "
				+ "Durante il processo di registrazione, è necessario specificare il tipo di utente che intende iscriversi alla "
				+ "piattaforma: studente, docente o admin. "
				+ "<p>&nbsp<p>"
				+ "EMAIL: per poter procedere con la registrazione, occorre inserire"
				+ "una mail che contenga minimo un punto e una chiocchiola.\n"
				+ "Sarò inoltre necessario selezionare il corso di laurea e il relativo dipartimento associato all'utente da registrare.\n"
				+ "<p>&nbsp<p>"
				+ "ATTENZIONE: l'utente dovrà in seguito completare il processo di registrazione visionando la posta del profilo "
				+ "email inserito e inserendo all'interno del sistema il codice di attivazione ricevuto. "); 
		Help visualizzaCorsi = new Help(2,"Bottone visualizza corsi","Questo bottone permette la visualizzazione delle materie relative"
				+ "ad ogni corso di laurea.\nPer poterlo fare, occorre inserire il dipartimento ed il corso di laurea della materia "
				+ "voluta, che sarà in seguito visualizzabile, se presente, all'interno dell'elenco delle materie relative al "
				+ "corso."
				+ "Se viene fatto click sul corso, l'applicazione aprirà la pagina corrispondente, che visualizzerà le sezioni e i file"
				+ "contenuti nel corso.\n"
				+ "L'admin sarà in grado di vedere sia le file/sezioni private che quelle pubbliche, creare ed eliminare una nuova cartella, "
				+ "aggiungere un file, scaricare un file/cartella \n"
				+ " ");
		Help assegnaCorso = new Help(3,"Bottone assegna Corso","Questo bottone permette di assegnre uno dei professori presenti nella lista"
				+ "ad un corso di laurea visualizzabili dopo aver fatto click sul bottone \"aggiungi corso\".\n"
				+ "In questo modo sarà possibile abilitare un docente all'insegnamento della materia di uno specifico corso.");
		Help modificaUtente = new Help(4,"Bottone modifica utenti","Questo bottone mette a disposizione dell'utente una lista di "
				+ "docenti/studenti che sarà possibile modificare. Dopo aver fatto click sul profilo da modificare, sarà possibile "
				+ "modificarne nome, cognome ed email. ");
		Help monitoraggioPiattaforma = new Help(5,"Bottone monitoraggio piattaforma","Questo bottone permette di visionare le attività "
				+ "degli altri utenti presenti sulla piattaforma. Sarà di preciso possibile monitare due tipi differenti di utenti: "
				+ "studenti/docenti e altri amministratori.\n"
				+ "Il monitoraggio permette di visualizzare il numero di utenti al momento online al corso selezionabile dal menu a"
				+ "discesa. Sarà inoltre possibile visionare il tempo medio di connessione alla materia da parte degli utenti che ne "
				+ "sono associati.\n"
				+ "Sarà infine possibile visionare il numero di accessi effettuati dagli utenti nell'arco della fascia temporale da "
				+ "inserire. Per poterlo fare, sara necessario specificare un arco temporale valido. ");
		Help creaCorso = new Help(6,"Bottone crea corso","Questo bottone permette la creazione di un corso inserendo un titolo, "
				+ "il dipartimento e il corso di laurea ad esso associati. Sarà possibile inoltre inserire le specifiche del corso "
				+ "che potranno essere visualizzate dagli utenti in fase di iscrizione. ");
		Help importaUtenti = new Help(7,"Bottone importa utenti","Questo bottone metterà a disposizione dell'utente una schermata "
				+ "nella quale sarà possibile scegliere quale tipo di utenti importare all'interno del sistema, prelevandoli da un "
				+ "file CSV."
				+ "<p>&nbsp<p>"
				+ "Per poter caricare con successo gli utenti, occorre che le colonne del file CSV da inserire siano come quelle "
				+ "riportate nell'esempio qui sotto"
				+ "<p>&nbsp<p>"
				+ "ESEMPIO DI STRUTTURA DEL FILE CSV<br>"
				+ "Matricola,Nome,Cognome,Email,CodiceAttivazione,Password,Verificato,TipoUtente\n"
				+ "1,UserName,UserSurmame,userName.userSurname@usermail.it,123456,password0,true,Admin\n\n"
				+ "ATTENZIONE: è possibile caricare solo un tipo di utente per file CSV: non è quindi possibile ad esempio inserire "
				+ "admin e docenti all'interno dello stesso file CSV");
		Help importaCorsi = new Help(8,"Bottone importa corsi","Questo bottone permette di importare da file CSV l'anagrafica completa "
				+ "di un corso, presente all'interno di 3 file CSV separati. Per poter caricare l'anagrafica sarà però necessario che "
				+ "tutti e 3 i file vengano caricati. I file dovranno assolutamente avere le stesse colonne degli esempi sotto"
				+ "elencati\n\n"
				+ "ESEMPIO CSV CORSI DI LAUREA\n"
				+ "ID,Nome,Dipartimento,TipoDocumento\n" + 
				"1000,Materia,1000,Corsi di laurea\n"
				+ "(*la colonna TipoDocumento dovrà sempre contenere la frase \"Corsi di Laurea\")\n\n" + 
				"ESEMPIO CSV CORSI MATERIA\n"
				+"ID,CorsiLaurea,Nome,annoAtt,Descrizione,TipoDocumento\n" + 
				"1000,1000,Nome Materia,2010,descrizione 1,Corsi Materia\n" + 
				"(*la colonna TipoDocumento dovrà sempre contenere la frase \"Corsi Materia\")\n\n"
				+ "ESEMPIO CSV DIPARTIMENTO\n"
				+ "ID,Nome,TipoDocumento\n" + 
				"1000,DIPARTIMENTO MATERIA,Dipartimenti\n" + 
				"(*la colonna TipoDocumento dovrà sempre contenere la frase \"Dipartimenti\")");
		Help eliminaUtente = new Help(9,"Bottone elimina utente","Questo bottone permette l'eliminazione di un utente, "
				+ "previa selezione ");
		Help eliminaCorso = new Help(10,"Bottone elimina corso","Questo bottone permette l'eliminazione di un corso, "
				+ "previa selezione");
		Help sbloccaProfiloSospeso = new Help(11,"Bottone sblocca profilo sospeso","Questo bottone permette lo sblocco di un'utente "
				+ "bloccato a causa di più di 10 tentativi di accesso errati al proprio profilo.");
		
		
		
		
		ArrayList<Help> guideList = new ArrayList<Help>();
		guideList.add(registraNuovoUente);
		guideList.add(visualizzaCorsi);
		guideList.add(assegnaCorso);
		guideList.add(modificaUtente);
		guideList.add(monitoraggioPiattaforma);
		guideList.add(creaCorso);
		guideList.add(importaUtenti);
		guideList.add(importaCorsi);
		guideList.add(eliminaUtente);
		guideList.add(eliminaUtente);
		guideList.add(eliminaCorso);
		guideList.add(sbloccaProfiloSospeso);
		return guideList;
		
	}
}
