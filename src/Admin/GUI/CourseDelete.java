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
import Common.DBType.Course;
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
public class CourseDelete {

	private JFrame frmEditUser;
	private static String courseSelected;
	private static long idSelected;
	private static ArrayList<String> courseList = new ArrayList<String>();
	
	

	/**
	 * Launch the application.
	 */
	public static void main() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CourseDelete window = new CourseDelete();
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
	public CourseDelete() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmEditUser = new JFrame();
		frmEditUser.setResizable(false);
		frmEditUser.setTitle("Elimina corso");
		frmEditUser.setBounds(100, 100, 497, 393);
		frmEditUser.setIconImage(Toolkit.getDefaultToolkit().getImage("media/f.png"));
		frmEditUser.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				frmEditUser.dispose();
				MainAdmin.main();
				
			}
		});
		frmEditUser.getContentPane().setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 11, 471, 300);
		frmEditUser.getContentPane().add(scrollPane);
		DefaultListModel model = new DefaultListModel<String>();
		JList coursesList = new JList();
		scrollPane.setViewportView(coursesList);
		

//		ArrayList<Teacher> L_docenti = ClientConnection.GetAllDocenti();
//		ArrayList<Student> L_studenti = ClientConnection.GetAllStudenti();
		
		 ArrayList<Course> corsiMateria = ClientConnection.getAllCorsiMateria();
		
		for (Course corsi : corsiMateria) {
			
			String listaMaterie = "<html>"  
					+ "<p style=\"color:red; visibility:none \">ID: <span style='color:black; visibility:none'>" + corsi.Id + "</span> </p>" 
					+ "<p style=\"color:red; display:inline\">NOME MATERIA: <span style='color:black'>" + corsi.name + "</span></p>"
					+ "<p style=\"color:red;\">CORSO DI LAUREA: <span style='color:black'>" + ClientConnection.getCorsoLaureaByID(corsi.degreeCourse).name + "</span> </p>"
					+ "<p style=\"color:red;\">ANNO ATTIVAZIONE: <span style='color:black'>" + corsi.activationYear + "</span> </p>"
					+ "<p>&nbsp</p>"
					+ " </html>";
			
			String temp = new Long(corsi.Id).toString();
			temp = temp.concat("-");
			temp = temp.concat(corsi.name.toString());
			temp = temp.concat("-");
			temp = temp.concat(ClientConnection.getCorsoLaureaByID(corsi.degreeCourse).name);
			temp = temp.concat("-");
			temp = temp.concat(corsi.activationYear + "");
			
			try {
				if(temp!=null)
			courseList.add(temp);
			} catch (NullPointerException nul) {
				
			}

			model.addElement(listaMaterie);
		}
	
		
		coursesList.setModel(model);

		
				coursesList.addListSelectionListener(new ListSelectionListener() {
		
					@Override
					public void valueChanged(ListSelectionEvent arg0) {
						
						String s = (String) coursesList.getSelectedValue();
						Pattern intsOnly = null;
						Matcher makeMatch = null;
						
						try {
						String removeHtml = s;
						removeHtml = removeHtml.replace("NOME MATERIA: ","");
						removeHtml = removeHtml.replace("CORSO DI LAUREA: ","");
						removeHtml = removeHtml.replace("ANNO ATTIVAZIONE: ","");
						}
						catch (Exception ex) {}
						
						try {
						intsOnly = Pattern.compile("\\d+");
						makeMatch = intsOnly.matcher(s);
						makeMatch.find();
						String inputStr = makeMatch.group();
						long inputLong = Long.parseLong(inputStr);
						idSelected = inputLong;
						System.out.println("DEBUG: inputLong: " + inputLong);
						
						for(String str : courseList) {
							String testStr = str;
							makeMatch = intsOnly.matcher(testStr);
							makeMatch.find();
							inputStr = makeMatch.group();
							long subStr = Long.parseLong(inputStr);
							
							if( subStr == inputLong) {
								courseSelected = testStr;
								break;
							}
						}
						
						String[] courseSplit = courseSelected.split("-");
						

						for (Course corsi : corsiMateria) {
							if (corsi.Id == Long.parseLong(courseSplit[0])) {
		
							}
		
						}
						} catch (Exception ex) { System.out.println("Error Eliminazione corso");
						
						}
						
		
					}
				});
		
		JButton EditButton = new JButton("Elimina");
		EditButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ClientConnection.deleteCorsoMateria(idSelected);
				updateUserList(model,coursesList);
				frmEditUser.dispose();
				CourseDelete.main();
			}
		});
		EditButton.setBounds(252, 322, 229, 31);
		frmEditUser.getContentPane().add(EditButton);
		
		JButton button = new JButton("Indietro");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frmEditUser.dispose();
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
	public static void updateUserList(DefaultListModel model, JList UserList) {
		
		 ArrayList<Course> corsiMateria = ClientConnection.getAllCorsiMateria();
		
		
			
			for (Course corsi : corsiMateria) {
				
				String listaMaterie = "<html>"  
						+ "<p style=\"color:red; visibility:none \">ID: <span style='color:black; visibility:none'>" + corsi.Id + "</span> </p>" 
						+ "<p style=\"color:red; display:inline\">NOME MATERIA: <span style='color:black'>" + corsi.name + "</span></p>"
						+ "<p style=\"color:red;\">CORSO DI LAUREA: <span style='color:black'>" + ClientConnection.getCorsoLaureaByID(corsi.degreeCourse).name + "</span> </p>"
						+ "<p style=\"color:red;\">ANNO ATTIVAZIONE: <span style='color:black'>" + corsi.activationYear + "</span> </p>"
						+ "<p>&nbsp</p>"
						+ " </html>";
				
				String temp = new Long(corsi.Id).toString();
				temp = temp.concat("-");
				temp = temp.concat(corsi.name.toString());
				temp = temp.concat("-");
				temp = temp.concat(ClientConnection.getCorsoLaureaByID(corsi.degreeCourse).name);
				temp = temp.concat("-");
				temp = temp.concat(corsi.activationYear + "");
				
				try {
					if(temp!=null)
				courseList.add(temp);
				} catch (NullPointerException nul) {
					
				}

				model.addElement(listaMaterie);
		}
	

		
		UserList.setModel(model);
		
	}
}

