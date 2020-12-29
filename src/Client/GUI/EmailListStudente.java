package Client.GUI;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.DefaultListModel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import Client.ClientConnection;
import Client.LoggedUser;
import Common.DBType.Course;
import Common.DBType.Teacher;
import Common.Enumerators.UserType;
import javax.swing.JList;
import java.awt.Font;
import javax.swing.JScrollPane;

import java.util.ArrayList;
import java.awt.Toolkit;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.JCheckBox;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;



/**
 *  GUI
 * 
 * @author Amenta Stefano, Moroni Paolo
 *
 */
public class EmailListStudente {

	private JFrame frmStudenteLista;
	static ArrayList<Course> listaUtenti;
	static ArrayList<Course> LcorsiregisteredDocente;
	static String titolodainviare = null;
	static String descrizionedasend = null;
	static long idsend;
	static ArrayList<Teacher> getStudentiByCorso = null;
	static long idStudente;
	static JList<String> list = null;
	static DefaultListModel<String> model1;
	static int npari = 0;

	/**
	 * Launch the application.
	 */
	public static void main() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					EmailListStudente window = new EmailListStudente();
					window.frmStudenteLista.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public EmailListStudente() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmStudenteLista = new JFrame();
		frmStudenteLista.setTitle(LoginClient.loginType + " - lista dei corsi");
		frmStudenteLista.setResizable(false);
		frmStudenteLista.setBounds(100, 100, 455, 499);
		frmStudenteLista.setIconImage(Toolkit.getDefaultToolkit().getImage("media/f.png"));
		frmStudenteLista.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmStudenteLista.getContentPane().setLayout(null);
		NewsLetter.insertedValueEmail = new ArrayList<String>();
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 39, 202, 311);
		frmStudenteLista.getContentPane().add(scrollPane);
		DefaultListModel<String> model = new DefaultListModel<>();
		JList<String> contactList = new JList<String>(model);  
		contactList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent arg0) {
				++npari; 
				String s = (String) contactList.getSelectedValue();
				boolean isItemInList = false;
				for(String iterate: NewsLetter.insertedValueEmail) {
					if(iterate.equals(s)) {
						isItemInList = true;
						break;
					}
				}
				if(npari % 2 != 0) {
					if (!isItemInList) {
						model1.addElement(s);
						String[] split = s.split("EMAIL: </span>");
						String[] split1 = split[1].split("</span>");
						NewsLetter.insertedValueEmail.add(split1[0]);
					}
				}
				
				System.out.println("DEBUG: EmailContactList valuechange: " + s);
			}
		});
		scrollPane.setViewportView(contactList);
		
		
		// devo trovare il corso del docente per poterlo passare come parametro alla funzione sotto citata. 
		//posso prenderlo dalla schermata dei corsi dei docenti
		
		long idCorso = ClientCourseList.idsend;
		System.out.println("DEBUG: EmailContactList id corso: " + idCorso);
		try {
		getStudentiByCorso = ClientConnection.emailDocentefromCorso(idCorso);
		} catch(Exception ex) {
			ex.printStackTrace();
		}

		// Se lo studente loggato è uno studente visualizzo i suoi corsi altrimenti
		// visualizzo quelli del docente
			
		if (LoggedUser.userType == UserType.STUDENT) {
			for (Teacher docente : getStudentiByCorso) {
				String modelToAdd = "<html><p><span style='color:red'>MATRICOLA: </span>  " + docente.userID + "</p>" 
						+ "<p><span style='color:red'>NOME: </span>" + docente.name + "</span></p>"
						+ "<p><span style='color:red'>COGNOME: </span>" + docente.surname + "</span></p>"
						+ "<p><span style='color:red'>EMAIL: </span>" + docente.Email + "</span></p>"
						+ "<p>&nbsp</p>"
						+ "</p></html>";
				model.addElement(modelToAdd);

			}
			contactList.setModel(model);
		}

		

		JLabel lblcourselist = new JLabel("Studenti iscritti al corso");
		lblcourselist.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblcourselist.setBounds(10, 11, 202, 27);
		frmStudenteLista.getContentPane().add(lblcourselist);

		JButton buttoncourseselection = new JButton("Conferma");
		buttoncourseselection.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				frmStudenteLista.dispose();
				NewsLetter.main();
			}
		});
		buttoncourseselection.setBounds(227, 411, 202, 27);
		frmStudenteLista.getContentPane().add(buttoncourseselection);
		
		
		JScrollPane scrollPane1 = new JScrollPane();
		scrollPane1.setBounds(227, 39, 202, 311);
		frmStudenteLista.getContentPane().add(scrollPane1);
		model1 = new DefaultListModel<>();
		list = new JList<String>(model1);
		scrollPane1.setViewportView(list);
		list.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent arg0) {

			}
		});
		
		
		JButton button = new JButton("Indietro");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				frmStudenteLista.dispose();
				NewsLetter.main();
			}
		});
		button.setBounds(10, 411, 202, 27);
		frmStudenteLista.getContentPane().add(button);
		
		JLabel label = new JLabel("Studenti selezionati");
		label.setFont(new Font("Tahoma", Font.PLAIN, 18));
		label.setBounds(227, 11, 202, 27);
		frmStudenteLista.getContentPane().add(label);
		
		JCheckBox chckbxNewCheckBox = new JCheckBox("Seleziona tutto");
		chckbxNewCheckBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
//				System.out.println(chckbxNewCheckBox.isSelected());
				if (chckbxNewCheckBox.isSelected()) {
					
					for (Teacher docente : getStudentiByCorso) {
						String modelToAdd = "<html><p><span style='color:red'>MATRICOLA: </span>  " + docente.userID + "</p>" 
								+ "<p><span style='color:red'>NOME: </span>" + docente.name + "</span></p>"
								+ "<p><span style='color:red'>COGNOME: </span>" + docente.surname + "</span></p>"
								+ "<p><span style='color:red'>COGNOME: </span>" + docente.userID + "</span></p>"
								+ "<p>&nbsp</p>"
								+ "</p></html>";
						model1.addElement(modelToAdd);
					}
					
					
				} else {
					model1.removeAllElements();
				}
			}
		});

		chckbxNewCheckBox.setBounds(10, 370, 202, 23);
		frmStudenteLista.getContentPane().add(chckbxNewCheckBox);
		chckbxNewCheckBox.setVisible(true);
	}
}
