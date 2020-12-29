package Client.GUI;

import java.awt.EventQueue;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import Client.ClientConnection;
import Client.LoggedUser;
import Common.DBType.Help;
import Common.DBType.Teacher;
import Common.DBType.Student;
import Common.Enumerators.UserType;

import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
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
	public static ArrayList<Help> helpListDocenti;
	public static ArrayList<Help> helpListStudenti;

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
				ClientCourseList.main();
			}
		});
		frmGuide.getContentPane().setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 11, 471, 302);
		frmGuide.getContentPane().add(scrollPane);
		DefaultListModel<String> model = new DefaultListModel<String>();
		JList<String> UserList = new JList<String>();
		scrollPane.setViewportView(UserList);
		
		if (LoggedUser.userType == UserType.STUDENT) {
			helpListStudenti = initGuida();
		} else {
			helpListDocenti = initGuida();
		}
		
		
		if (LoggedUser.userType == UserType.STUDENT) {
		for (Help help : helpListStudenti) {
			
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
				} 
			catch (NullPointerException nul) {
				
				}
			model.addElement(nome_cognome);
			}
		} if (LoggedUser.userType == UserType.TEACHER) {
				for (Help help : helpListDocenti) {
					
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
						} 
					catch (NullPointerException nul) {
						
						}
					model.addElement(nome_cognome);
					}
				}
		
		
		
		
		UserList.setModel(model);
		
		JButton btnNewButton = new JButton("Indietro");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frmGuide.dispose();
				ClientCourseList.main();
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
						
					if(s.contains("Studente - elenco dei corsi")) 
						inputLong = 1;
						
						if(s.contains("Studente - visualizza altri corsi"))
						inputLong = 2;
						
						if(s.contains("Docente - elenco dei corsi"))
						inputLong = 3;

						if(s.contains("Docente - monitoraggio"))
						inputLong = 4;

						System.out.println("DEBUG: inputLong: " + inputLong);
						
						
						try {
						for (Help help : helpListDocenti) {
							if (help.id == inputLong) {
								helpObj = help;
								break;
							}
						}
						} catch (NullPointerException ex) {}
						
						
						try {
						for (Help help : helpListStudenti) {
							if (help.id == inputLong) {
								helpObj = help;
								break;
							}
						}
						} catch (NullPointerException ex) {}
		
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
		Help StudregistraNuovoUente = new Help(1,
				"Studente - elenco dei corsi",
				"Questa sezione ti consente di visualizzare i corsi ai quali sei iscritto.<br>"
				+ "Potrai accedere alla pagina del corso facendo click sul morso visualizzato nella lista<br>"
				+ "(identificabile tramite la matricola ed il nome).<br><br>"
				+ "Questa pagina mette inoltre a disposizione un menù per che introduce l'utente alla <br>"
				+ "visualizzazione di altri corsi a cui iscriversi, da la possibilità di visualizzare le <br>"
				+ "proprie informazioni personali registrate sul sistema e che permette il logout, come la <br>"
				+ "chiusura del sistema.");
		Help StudvisualizzaAltriCorsi = new Help(2,"Studente - visualizza altri corsi","Questa opzione ti da la possibilità <br>"
				+ "di visualizzare l'elenco dei corsi a cui potrai iscriverti. <br>"
				+ "Il Bottone \"Informazioni sul corso\" ti permetterà di accedere alla pagina di registrazione <br>"
				+ "del corso.  ");
		
		Help DocvisualizzaAltriCorsi = new Help(3,"Docente - elenco dei corsi","Questa sezione ti consente di visualizzare i corsi "
				+ "da te tenuti.<br>"
				+ "Dopo aver fatto click sul corso e aver successivamente fatto click sul tasto \"visualizza corso\", <br>"
				+ "potrai accedere alla pagina del corso, alla quale potrai eseguire le seguenti operazioni:<br> "
				+ "- visualizzazione delle sezioni e dei file del tuo corso<br> "
				+ "- aggiunta di un file ad una sezione del corso<br> "
				+ "- aggiunta di una cartella alla pagina del corso<br> "
				+ "- cambiare la descrizione del corso, di una cartella o di un file"
				+ "- eliminare un file o una cartella ");
		Help DocMonitoraggio = new Help(4,"Docente - monitoraggio","Questa opzione della paittaforma ti permette di accedere <br> "
				+ "alle utility di monitoraggio di utenti online al corso, misura del tempo medio di connessione del corso <br> "
				+ "e conteggio del numero di download effettuati all'interno di un arco temporale.<br> "
				+ "<span style='color:red'>ATTENZIONE</span>: affichè sia possibile visualizzare il numero di download all'interno di una fascia temporale in <br> "
				+ "modo corretto, occorre inserire affianco all'identificativo \"da\" una data antecedente a quella presente<br> "
				+ "affianco all'identificativo \"a\" ");
		
		
		ArrayList<Help> guideList = new ArrayList<Help>();
		if (LoggedUser.userType == UserType.STUDENT) {
			guideList.add(StudregistraNuovoUente); 
			guideList.add(StudvisualizzaAltriCorsi); 
		} else {
			guideList.add(DocvisualizzaAltriCorsi); 
			guideList.add(DocMonitoraggio); 
		}
		return guideList;
		
	}
}
