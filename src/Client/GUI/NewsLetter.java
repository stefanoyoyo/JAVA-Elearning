package Client.GUI;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;

import Client.ClientConnection;
import Client.LoggedUser;
import Common.DBType.Teacher;
import Common.Enumerators.UserType;

import javax.swing.JButton;
import java.awt.Font;
import java.awt.Toolkit;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;



/**
 *  GUI
 * 
 * @author Amenta Stefano, Moroni Paolo
 *
 */
public class NewsLetter {
	private JFrame frame;
	private JTextField textfieldoggettoMail;
	String oggetto;
	String messaggio;
	private JTextArea destinatariField;
	public static ArrayList<Teacher> emailDocentefromCorso;
	static ArrayList<String> insertedValueEmail = null;
	static ArrayList<String> insertedValueEmail1 = null;

	/**
	 * Launch the application.
	 */

	public static void main() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					NewsLetter window = new NewsLetter();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public NewsLetter() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 451, 558);
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage("media/f.png"));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		textfieldoggettoMail = new JTextField();
		textfieldoggettoMail.setBounds(12, 85, 230, 22);
		frame.getContentPane().add(textfieldoggettoMail);
		textfieldoggettoMail.setColumns(10);

		JLabel lblMesaggio = new JLabel("Testo del messaggio:");
		lblMesaggio.setBounds(12, 193, 105, 16);
		frame.getContentPane().add(lblMesaggio);

		JLabel lblOggetto = new JLabel("Oggetto del messaggio");
		lblOggetto.setBounds(12, 62, 161, 22);
		frame.getContentPane().add(lblOggetto);

		JTextPane textPane = new JTextPane();
		textPane.setBounds(12, 220, 408, 237);
		frame.getContentPane().add(textPane);

		JButton btnsendmail = new JButton("Invia mail");
		btnsendmail.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				
				
				
				oggetto = textfieldoggettoMail.getText();
				messaggio = textPane.getText();
				if (LoggedUser.userType == UserType.TEACHER) {
				ClientConnection.sendNewsLetterToSomeStudents(ClientCourseList.idsend, insertedValueEmail1, oggetto, messaggio);
				} 
				if (LoggedUser.userType == UserType.STUDENT) {
					ClientConnection.sendNewsLetterToSomeStudents(ClientCourseList.idsend, insertedValueEmail, oggetto, messaggio);
				}
				frame.dispose();
				CourseFile.Main();

			}

		});
		btnsendmail.setBounds(279, 483, 141, 25);
		frame.getContentPane().add(btnsendmail);

		JButton Indietro = new JButton("Indietro");
		Indietro.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
				CourseFile.Main();
			}

		});
		Indietro.setBounds(12, 483, 141, 25);
		frame.getContentPane().add(Indietro);

		JLabel lblmessaggi = new JLabel("");
		lblmessaggi.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblmessaggi.setBounds(66, 369, 299, 31);
		frame.getContentPane().add(lblmessaggi);
		
		JButton btnNewButton = new JButton("Destinatari");
		btnNewButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if (LoggedUser.userType == UserType.TEACHER) {
				frame.dispose();
				EmailListDocente.main();
				} 
				if (LoggedUser.userType == UserType.STUDENT) {
					frame.dispose();
					EmailListStudente.main();
				}
				
			}
		});
		

		
		btnNewButton.setBounds(276, 85, 144, 23);
		frame.getContentPane().add(btnNewButton);
		
		
		
		JScrollPane scrollPane1 = new JScrollPane();
		scrollPane1.setBounds(12, 141, 408, 41);
		frame.getContentPane().add(scrollPane1);
		
		int click = 0;
		
		destinatariField = new JTextArea();
		if (insertedValueEmail1 == null) {
		destinatariField.setText("Inserisci destinatari");
		} 
			if (LoggedUser.userType == UserType.TEACHER && insertedValueEmail1 != null) {
			for(String s: insertedValueEmail1) {
				destinatariField.append(s + "\n");
				}
			}
			if (LoggedUser.userType == UserType.STUDENT && insertedValueEmail != null) {
			for(String s: insertedValueEmail) {
				destinatariField.append(s + "\n");
				}
			}
			
		
		
		destinatariField.setEditable(false);
		destinatariField.setColumns(10);
		destinatariField.setBounds(12, 88, 408, 41);
//		frame.getContentPane().add(destinatariField);
		scrollPane1.setViewportView(destinatariField);
		
		
		
		JLabel label = new JLabel("Destinatari:");
		label.setBounds(12, 119, 105, 16);
		frame.getContentPane().add(label);
		
		JLabel label_1 = new JLabel("E-mail Newsletter");
		label_1.setHorizontalAlignment(SwingConstants.CENTER);
		label_1.setFont(new Font("Tahoma", Font.BOLD, 17));
		label_1.setBounds(0, 0, 408, 47);
		frame.getContentPane().add(label_1);
		

		if (LoggedUser.userType == UserType.STUDENT) {
			System.out.println("DEBUG: Newsletter ");
			long idCorso = CourseFile.indicecorso;
			ClientConnection.getAllCorsiMateria();
			emailDocentefromCorso = ClientConnection.emailDocentefromCorso(idCorso);
			destinatariField.setText("");
			for(Teacher docente: emailDocentefromCorso) {
			destinatariField.append(docente.Email + "\n");
			}
			
//			for (Course c : corsoMateria) {
//				if (c.Id == CourseFile.indicecorso) {
//					idDocente = c.Id;
//				}
//			}
			
			
			
//			destinatariField.setText("");
		}
		
		
	}
}
