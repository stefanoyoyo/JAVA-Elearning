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
import Common.Enumerators.UserType;

import javax.swing.JList;
import java.awt.Font;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import java.awt.Component;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JMenu;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.SwingConstants;



/**
 *  GUI
 * 
 * @author Amenta Stefano, Moroni Paolo
 *
 */
public class ClientCourseList {

	private JFrame frmStudenteLista;
	static ArrayList<Course> LcorsiregisteredStudent;
	static ArrayList<Course> LcorsiregisteredDocente;
	static String titolodainviare = null;
	static String descrizionedasend = null;
	public static long idsend;

	/**
	 * Launch the application.
	 */
	public static void main() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClientCourseList window = new ClientCourseList();
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
	public ClientCourseList() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	
	/**
	 * Metodo per stampare correttamente il tipo di utente su questa vista quando viene caricata
	 * @param loginType
	 * @return
	 */
	private String translateLoginType(UserType loginType) {
		switch(loginType) {
		case STUDENT:
			return "Studente";
		case TEACHER: 
			return "Docente";
		default:
			break;
		}
		return null;
	}
	
	private void initialize() {
		frmStudenteLista = new JFrame();
		frmStudenteLista.setTitle(translateLoginType(LoginClient.loginType) + " - lista dei corsi");
		frmStudenteLista.setResizable(false);
		frmStudenteLista.setBounds(100, 100, 455, 499);
		frmStudenteLista.setIconImage(Toolkit.getDefaultToolkit().getImage("media/f.png"));
		frmStudenteLista.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmStudenteLista.getContentPane().setLayout(null);

		JList<String> courselist = new JList<String>();
		DefaultListModel<String> model = new DefaultListModel<String>();
		courselist.setBounds(7, 39, 432, 338);
		frmStudenteLista.getContentPane().add(courselist);

		// Se lo studente loggato è uno studente visualizzo i suoi corsi altrimenti
		// visualizzo quelli del docente
		if (LoggedUser.userType.toString().equals("STUDENT")) {
			LcorsiregisteredStudent = ClientConnection.getCorsiStud(LoggedUser.anagrafica.getUserID());

			for (Course cm : LcorsiregisteredStudent) {
				// nel model si inserisce nome e id
				String modelToAdd = "<html><p><span style='color:red'>MATRICOLA: </span>  " + cm.Id + "</p>" +
						"<p><span style='color:red'>NOME: </span>" + cm.name + "</span></p>"
						+ "<p>&nbsp</p>"
						+ "</p></html>";
//				model.addElement(cm.Id + " " + cm.name);
				model.addElement(modelToAdd);

			}
			courselist.setModel(model);

		} else if (LoggedUser.userType.toString().equals("TEACHER")) {

			LcorsiregisteredDocente = ClientConnection.getCorsiByDocente(LoggedUser.anagrafica.getUserID());
			System.out.println(LcorsiregisteredDocente.size());

			for (Course cmd : LcorsiregisteredDocente) {
				
				String modelToAdd = "<html><p><span style='color:red'>MATRICOLA: </span>  " + cmd.Id + "</p>" +
						"<p><span style='color:red'>NOME: </span>" + cmd.name + "</span></p>"
						+ "<p>&nbsp</p>"
						+ "</p></html>";

				System.out.println(cmd.Id + " " + cmd.name);
				model.addElement(modelToAdd);
			}

			courselist.setModel(model);
		}

		JButton buttoncourseselection = new JButton("visualizza corso");
		buttoncourseselection.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (LoggedUser.userType.toString().equals("STUDENT")) {
					String s = (String) courselist.getSelectedValue();
					Pattern intsOnly = Pattern.compile("\\d+");
					Matcher makeMatch = intsOnly.matcher(s);
					makeMatch.find();
					String inputStr = makeMatch.group();
					long inputLong = Long.parseLong(inputStr);
					System.out.println(inputLong);
//					String[] courseData = temp.split(" ");

					for (Course cm : LcorsiregisteredStudent) {
						if (/*Long.parseLong(courseData[0])*/ inputLong == cm.Id) {

							titolodainviare = cm.name;
							descrizionedasend = cm.description;
							idsend = cm.Id;
						}
					}

				} else {
					String s = (String) courselist.getSelectedValue();
					Pattern intsOnly = Pattern.compile("\\d+");
					Matcher makeMatch = intsOnly.matcher(s);
					makeMatch.find();
					String inputStr = makeMatch.group();
					long inputLong = Long.parseLong(inputStr);
					System.out.println(inputLong);

					for (Course cm : LcorsiregisteredDocente) {
						if (inputLong == cm.Id) {

							titolodainviare = cm.name;
							descrizionedasend = cm.description;
							idsend = cm.Id;
						}
					}

				}
				
				frmStudenteLista.dispose();
				if (LoggedUser.userType.toString().equals("STUDENT"))
					ClientConnection.updateLoc(LoggedUser.anagrafica.userID, idsend);
				CourseFile.Main();

			}
		});
		buttoncourseselection.setBounds(10, 388, 429, 27);
		frmStudenteLista.getContentPane().add(buttoncourseselection);
		
		JLabel label = new JLabel("Elenco dei corsi");
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setFont(new Font("Tahoma", Font.BOLD, 17));
		label.setBounds(-3, 11, 384, 27);
		frmStudenteLista.getContentPane().add(label);

		JMenuBar menuBar = new JMenuBar();
		menuBar.setBackground(SystemColor.controlHighlight);
		frmStudenteLista.setJMenuBar(menuBar);

		JMenu mnNewMenu = new JMenu("menu");
		menuBar.add(mnNewMenu);

		JMenuItem menuothercourse = new JMenuItem("Visualizza altri corsi");
		mnNewMenu.add(menuothercourse);

		// controllo per far vedere solo a studente altri corsi

		if (LoggedUser.userType.toString().equals("TEACHER")) {
			menuothercourse.setVisible(false);
		}

		menuothercourse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				new CourseNotRegistreted();
				frmStudenteLista.dispose();
				CourseNotRegistreted.main();
				
				

			}
		});

		// anagrafica visibile solo per studente

		JMenuItem menuiteinformation = new JMenuItem("Informazioni anagrafiche");

		if (LoggedUser.userType.toString().equals("TEACHER")) {
			menuiteinformation.setVisible(false);
		}

		menuiteinformation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				new ClientAccountData();
				// apre finestra dati anagrafici
				ClientAccountData.main(null);

			}
		});

		mnNewMenu.add(menuiteinformation);

		JMenuItem mntmMonitoraggio = new JMenuItem("Monitoraggio");
		mnNewMenu.add(mntmMonitoraggio);

		// controllo monitoraggio visibile solo per studente

		if (LoggedUser.userType.toString().equals("STUDENT")) {

			mntmMonitoraggio.setVisible(false);
		}

		mntmMonitoraggio.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				frmStudenteLista.dispose();
				TeacherMonitoring.main(null);
			}
		});
		
		JMenuItem mntmEsci_1 = new JMenuItem("Esci");
		mntmEsci_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
			}
		});
		mntmEsci_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				frmStudenteLista.dispose();
				new LoginClient();
				LoginClient.main(null);
			}
		});
		mnNewMenu.add(mntmEsci_1);
		
				JMenuItem mntmEsci = new JMenuItem("Chiudi");
				
						mnNewMenu.add(mntmEsci);
						
						JMenu mnGuida = new JMenu("guida");
						menuBar.add(mnGuida);
						
						JMenuItem mntmInformazioni = new JMenuItem("informazioni");
						mntmInformazioni.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								frmStudenteLista.dispose();
								Guide.main();
							}
						});
						mnGuida.add(mntmInformazioni);
		mntmEsci.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				frmStudenteLista.dispose();
				ClientConnection.close();
			}
		});

	}
}
