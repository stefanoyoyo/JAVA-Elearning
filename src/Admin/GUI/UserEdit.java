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
import Common.DBType.Userdatas;
import Common.DBType.Teacher;
import Common.DBType.Admin;
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
import java.awt.Font;



/**
 *  GUI
 * 
 * @author Amenta Stefano, Moroni Paolo
 *
 */
public class UserEdit {

	private JFrame frmEditUser;
	private JTextField NameField;
	private JTextField SurnameField;
	private JTextField emailField;
	private long matricola = 0;
	private String name = null;
	private String surname = null;
	private String mail = null;
	private String activationCode = null;
	private String psw = null;
	private boolean verified = false;
	private UserType ut;
	private Teacher Di;
	private Student st;
	private Admin ad;
	Userdatas anagrafica;
	private static String userSelected;
	private static ArrayList<String> userSelectedList = new ArrayList<String>();;
	

	/**
	 * Launch the application.
	 */
	public static void main() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UserEdit window = new UserEdit();
					window.frmEditUser.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public UserEdit() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmEditUser = new JFrame();
		frmEditUser.setResizable(false);
		frmEditUser.setTitle("Edit User");
		frmEditUser.setBounds(100, 100, 497, 393);
		frmEditUser.setIconImage(Toolkit.getDefaultToolkit().getImage("media/f.png"));
		frmEditUser.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				MainAdmin.main();
				frmEditUser.dispose();
			}
		});
		frmEditUser.getContentPane().setLayout(null);

		NameField = new JTextField();
		NameField.setBounds(304, 81, 127, 22);
		frmEditUser.getContentPane().add(NameField);
		NameField.setColumns(10);

		JLabel lblNewLabel = new JLabel("Nome:");
		lblNewLabel.setBounds(304, 52, 56, 16);
		frmEditUser.getContentPane().add(lblNewLabel);

		SurnameField = new JTextField();
		SurnameField.setBounds(304, 170, 127, 22);
		frmEditUser.getContentPane().add(SurnameField);
		SurnameField.setColumns(10);

		emailField = new JTextField();
		emailField.setBounds(304, 262, 127, 22);
		frmEditUser.getContentPane().add(emailField);
		emailField.setColumns(10);

		JLabel lblCognome = new JLabel("Cognome:");
		lblCognome.setBounds(304, 141, 74, 16);
		frmEditUser.getContentPane().add(lblCognome);

		JLabel lblEmail = new JLabel("Email:");
		lblEmail.setBounds(304, 227, 56, 22);
		frmEditUser.getContentPane().add(lblEmail);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 11, 276, 332);
		frmEditUser.getContentPane().add(scrollPane);
		DefaultListModel<String> model = new DefaultListModel<String>();
		JList<String> UserList = new JList<String>();
		scrollPane.setViewportView(UserList);
		

		ArrayList<Teacher> L_docenti = ClientConnection.getAllDocenti();
		ArrayList<Student> L_studenti = ClientConnection.getAllStudenti();
		ArrayList<Admin> L_admin = ClientConnection.getAllAdmin();
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
			userSelectedList.add(temp);
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
			userSelectedList.add(temp);
			} catch (NullPointerException nul) {
				
			}

			model.addElement(nome_cognome);
		}
		
		UserList.setModel(model);

		
				UserList.addListSelectionListener(new ListSelectionListener() {
		
					@Override
					public void valueChanged(ListSelectionEvent arg0) {
						
						String s = (String) UserList.getSelectedValue();
						
						String removeHtml = s;
						removeHtml = removeHtml.replace("MATRICOLA: ","");
						removeHtml = removeHtml.replace("NOME: ","");
						removeHtml = removeHtml.replace("COGNOME: ","");
						
						Pattern intsOnly = Pattern.compile("\\d+");
						Matcher makeMatch = intsOnly.matcher(s);
						makeMatch.find();
						String inputStr = makeMatch.group();
						long inputLong = Long.parseLong(inputStr);
						
						for(String str : userSelectedList) {
							String testStr = str;
							makeMatch = intsOnly.matcher(testStr);
							makeMatch.find();
							inputStr = makeMatch.group();
							long subStr = Long.parseLong(inputStr);
							
							if( subStr == inputLong) {
								userSelected = testStr;
								break;
							}
						}
						
						String[] user_split = userSelected.split("-");
						
						emailField.setText(user_split[3]);
						NameField.setText(user_split[2]);
						SurnameField.setText(user_split[1]);
						for (Teacher docente : L_docenti) {
							if (docente.userID == Long.parseLong(user_split[0])) {
								Di = docente;
								anagrafica = Di;
							}
						}
						
						for (Student studente : L_studenti) {
							if (studente.userID == Long.parseLong(user_split[0])) {
								st = studente;
								anagrafica = studente;
							}
						}
						
						for (Admin admin : L_admin) {
							if (admin.userID == Long.parseLong(user_split[0])) {
								ad = admin;
								anagrafica = admin;
							}
						}
						
					}
				});
		
		JButton EditButton = new JButton("Applica modifiche");
		EditButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String nw_name = NameField.getText();
				String nw_surname = SurnameField.getText();
				String nw_email = emailField.getText();
				
				try {
				activationCode = Di.getActivationCode();
				matricola = Di.getUserID();
				psw = Di.getPassword();
				name = nw_name;
				surname = nw_surname;
				mail = nw_email;
				ut = Di.getT();
				verified = Di.getTrusted();
				} catch (Exception ex) {System.out.println(ex.getLocalizedMessage());}

				Userdatas a = new Userdatas();

				a.setActivationCode(activationCode);
				a.setSurname(surname);
				a.setEmail(mail);
				a.setUserID(matricola);
				a.setPassword(psw);
				a.setT(ut);
				a.trusted = verified;
				a.setName(name);

				if (a.Email.matches(GetUserData.EMAIL_PATTERN)) {
					ClientConnection.cambiaAnagrafica(a);
					JOptionPane.showMessageDialog(null, "Modifica effettuata");
					MainAdmin.main();
					frmEditUser.dispose();
				}
				else
					JOptionPane.showMessageDialog(null, "Mail non valida");

			}
		});
		EditButton.setBounds(304, 312, 175, 31);
		frmEditUser.getContentPane().add(EditButton);
		
		JLabel label = new JLabel("Modifica dati utente");
		label.setFont(new Font("Tahoma", Font.BOLD, 16));
		label.setBounds(304, 13, 175, 16);
		frmEditUser.getContentPane().add(label);
		
		

	}
}
