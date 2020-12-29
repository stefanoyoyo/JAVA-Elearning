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
import Common.DBType.Student;
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
public class EmailListDocente {

	private JFrame frmStudenteLista;
	static ArrayList<Course> listaUtenti;
	static ArrayList<Course> LcorsiregisteredDocente;
	static String titolodainviare = null;
	static String descrizionedasend = null;
	static long idsend;
	static ArrayList<Student> getStudentiByCorso = null;
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
					EmailListDocente window = new EmailListDocente();
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
	public EmailListDocente() {
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
		NewsLetter.insertedValueEmail1 = new ArrayList<String>();
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 39, 202, 311);
		frmStudenteLista.getContentPane().add(scrollPane);
		DefaultListModel<String> model = new DefaultListModel<>();
		@SuppressWarnings("unchecked")
		JList<String> contactList = new JList<String>(model);  
		contactList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent arg0) {
				++npari; 
				String s = (String) contactList.getSelectedValue();
				boolean isItemInList = false;
				for(String iterate: NewsLetter.insertedValueEmail1) {
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
						NewsLetter.insertedValueEmail1.add(split1[0]);
					}
				}
			}
		});
		scrollPane.setViewportView(contactList);
		
		long idCorso = ClientCourseList.idsend;
		System.out.println("DEBUG: EmailContactList id corso: " + idCorso);
		try {
		getStudentiByCorso = ClientConnection.getStudentiByCorso(idCorso);
		} catch(Exception ex) {
			ex.printStackTrace();
		}

		// Se lo studente loggato è uno studente visualizzo i suoi corsi altrimenti
		// visualizzo quelli del docente
			
		if (LoggedUser.userType == UserType.TEACHER) {
			for (Student studente : getStudentiByCorso) {
				String modelToAdd = "<html><p><span style='color:red'>MATRICOLA: </span>  " + studente.userID + "</p>" 
						+ "<p><span style='color:red'>NOME: </span>" + studente.name + "</span></p>"
						+ "<p><span style='color:red'>COGNOME: </span>" + studente.surname + "</span></p>"
						+ "<p><span style='color:red'>EMAIL: </span>" + studente.Email + "</span></p>"
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
				if (chckbxNewCheckBox.isSelected()) {
					
					for (Student studente : getStudentiByCorso) {
						String modelToAdd = "<html><p><span style='color:red'>MATRICOLA: </span>  " + studente.userID + "</p>" 
								+ "<p><span style='color:red'>NOME: </span>" + studente.name + "</span></p>"
								+ "<p><span style='color:red'>COGNOME: </span>" + studente.surname + "</span></p>"
								+ "<p><span style='color:red'>COGNOME: </span>" + studente.userID + "</span></p>"
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
