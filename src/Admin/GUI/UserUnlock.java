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
public class UserUnlock {

	private JFrame frmEditUser;
	private static String userSelected;
	private static long idSelected;
	private static ArrayList<String> userSelectedList = new ArrayList<String>();
	
	

	/**
	 * Launch the application.
	 */
	public static void main() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UserUnlock window = new UserUnlock();
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
	public UserUnlock() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmEditUser = new JFrame();
		frmEditUser.setResizable(false);
		frmEditUser.setTitle("Sblocca utente");
		frmEditUser.setBounds(100, 100, 497, 393);
		frmEditUser.setIconImage(Toolkit.getDefaultToolkit().getImage("media/f.png"));
		frmEditUser.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				frmEditUser.dispose();
				new MainAdmin();
				MainAdmin.main();
				
			}
		});
		frmEditUser.getContentPane().setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 11, 471, 300);
		frmEditUser.getContentPane().add(scrollPane);
		DefaultListModel<String> model = new DefaultListModel<String>();
		JList<String> UserList = new JList<String>();
		scrollPane.setViewportView(UserList);
		

//		ArrayList<Teacher> L_docenti = ClientConnection.GetAllDocenti();
//		ArrayList<Student> L_studenti = ClientConnection.GetAllStudenti();
		
		ArrayList<Teacher> L_docenti = ClientConnection.getLockedDocenti();
		ArrayList<Student> L_studenti = ClientConnection.getLockedStudenti();
		ArrayList<Admin> L_admin = ClientConnection.getLockedAdmin();
		
		
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
//			System.out.println("DEBUG: userSelectedList.size(): " + userSelectedList.size() );

			model.addElement(nome_cognome);
		}
		
		
		for (Admin D : L_admin) {
			
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
		
		UserList.setModel(model);

		
				UserList.addListSelectionListener(new ListSelectionListener() {
		
					@Override
					public void valueChanged(ListSelectionEvent arg0) {
						
						String s = (String) UserList.getSelectedValue();
						Pattern intsOnly = null;
						Matcher makeMatch = null;
						
						try {
						String removeHtml = s;
						removeHtml = removeHtml.replace("MATRICOLA: ","");
						removeHtml = removeHtml.replace("NOME: ","");
						removeHtml = removeHtml.replace("COGNOME: ","");
						
						intsOnly = Pattern.compile("\\d+");
						makeMatch = intsOnly.matcher(s);
						makeMatch.find();
						String inputStr = makeMatch.group();
						long inputLong = Long.parseLong(inputStr);
						idSelected = inputLong;
						System.out.println("DEBUG: inputLong: " + inputLong);
						
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

						for (Teacher docente : L_docenti) {
							if (docente.userID == Long.parseLong(user_split[0])) {
		
							}
		
						}
						}catch (Exception ex) { System.out.println("Error");}
		
					}
				});
		
		JButton EditButton = new JButton("Sblocca profilo");
		EditButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ClientConnection.unlockUser(idSelected);
				updateUserList(model,UserList);
				frmEditUser.dispose();
				UserUnlock.main();
			}
		});
		EditButton.setBounds(252, 322, 229, 31);
		frmEditUser.getContentPane().add(EditButton);
		
		JButton button = new JButton("Indietro");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frmEditUser.dispose();
				new MainAdmin();
				MainAdmin.main();
			}
		});
		button.setBounds(13, 322, 229, 31);
		frmEditUser.getContentPane().add(button);
		
		

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
//			System.out.println("DEBUG: userSelectedList.size(): " + userSelectedList.size() );

			model.addElement(nome_cognome);
		}
		
		UserList.setModel(model);
		
	}
}
