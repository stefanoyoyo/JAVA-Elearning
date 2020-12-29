package Admin.GUI;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.awt.SystemColor;
import java.awt.Toolkit;
import Client.ClientConnection;
import Client.CustomJTreeNode;




/**
 *  GUI
 * 
 * @author Amenta Stefano, Moroni Paolo
 *
 */
public class CorsiCSV {

	private JFrame frmAsd;
	private static String titleFolder = null;
	File road = null;
	CustomJTreeNode controlNode;
	private JButton BTN_corsi_materia;
	
	private static byte[] fileCorsiLaurea = null;
	private static byte[] fileCorsiMateria = null;
	private static byte[] fileDipartimenti = null;
	private static Object[] fileToSend = null;
	


	public static void Main() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CorsiCSV window = new CorsiCSV();
					window.frmAsd.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	

	/**
	 * Create the application.
	 */
	public CorsiCSV() {
		initialize();

	}
	
	public static void writeEditorPane(javax.swing.JEditorPane EditorPane) {
		// 000
		if (fileCorsiLaurea == null && fileCorsiMateria == null && fileDipartimenti == null) {
		EditorPane.setText("<html><p style='color:red'>CORSI DI LAUREA: <span style='color:black'>ancora da caricare</span></p>"
				+ "<p>&nbsp</p>"
				+ "<p style='color:red'>MATERIE DEI CORSI: <span style='color:black'>ancora da caricare</span></p>"
				+ "<p>&nbsp</p>"
				+ "<p style='color:red'>DIPARTIMENTI: <span style='color:black'>ancora da caricare</span></p>"
				+ "</html>");
		}
		// 001
		if (fileCorsiLaurea == null && fileCorsiMateria == null && fileDipartimenti != null) {
		EditorPane.setText("<html><p style='color:red'>CORSI DI LAUREA: <span style='color:black'>ancora da caricare</span></p>"
				+ "<p>&nbsp</p>"
				+ "<p style='color:red'>MATERIE DEI CORSI: <span style='color:black'>ancora da caricare</span></p>"
				+ "<p>&nbsp</p>"
				+ "<p style='color:red'>DIPARTIMENTI: <span style='color:black'>file caricato</span></p>"
				+ "</html>");
		}
		// 010
		if (fileCorsiLaurea == null && fileCorsiMateria != null && fileDipartimenti == null) {
		EditorPane.setText("<html><p style='color:red'>CORSI DI LAUREA: <span style='color:black'>ancora da caricare</span></p>"
				+ "<p>&nbsp</p>"
				+ "<p style='color:red'>MATERIE DEI CORSI: <span style='color:black'>file caricato</span></p>"
				+ "<p>&nbsp</p>"
				+ "<p style='color:red'>DIPARTIMENTI: <span style='color:black'>ancora da caricare</span></p>"
				+ "</html>");
		}
		// 011
		if (fileCorsiLaurea == null && fileCorsiMateria != null && fileDipartimenti != null) {
		EditorPane.setText("<html><p style='color:red'>CORSI DI LAUREA: <span style='color:black'>ancora da caricare</span></p>"
				+ "<p>&nbsp</p>"
				+ "<p style='color:red'>MATERIE DEI CORSI: <span style='color:black'>file caricato</span></p>"
				+ "<p>&nbsp</p>"
				+ "<p style='color:red'>DIPARTIMENTI: <span style='color:black'>file caricato</span></p>"
				+ "</html>");
		}
		// 100
		if (fileCorsiLaurea != null && fileCorsiMateria == null && fileDipartimenti == null) {
		EditorPane.setText("<html><p style='color:red'>CORSI DI LAUREA: <span style='color:black'>file caricato</span></p>"
				+ "<p>&nbsp</p>"
				+ "<p style='color:red'>MATERIE DEI CORSI: <span style='color:black'>ancora da caricare</span></p>"
				+ "<p>&nbsp</p>"
				+ "<p style='color:red'>DIPARTIMENTI: <span style='color:black'>ancora da caricare</span></p>"
				+ "</html>");
		}
		// 101
		if (fileCorsiLaurea != null && fileCorsiMateria == null && fileDipartimenti != null) {
		EditorPane.setText("<html><p style='color:red'>CORSI DI LAUREA: <span style='color:black'>file caricato</span></p>"
				+ "<p>&nbsp</p>"
				+ "<p style='color:red'>MATERIE DEI CORSI: <span style='color:black'>ancora da caricare</span></p>"
				+ "<p>&nbsp</p>"
				+ "<p style='color:red'>DIPARTIMENTI: <span style='color:black'>file caricato</span></p>"
				+ "</html>");
		}
		// 110
		if (fileCorsiLaurea != null && fileCorsiMateria != null && fileDipartimenti == null) {
		EditorPane.setText("<html><p style='color:red'>CORSI DI LAUREA: <span style='color:black'>file caricato</span></p>"
				+ "<p>&nbsp</p>"
				+ "<p style='color:red'>MATERIE DEI CORSI: <span style='color:black'>file caricato</span></p>"
				+ "<p>&nbsp</p>"
				+ "<p style='color:red'>DIPARTIMENTI: <span style='color:black'>ancora da caricare</span></p>"
				+ "</html>");
		}
		// 111
		if (fileCorsiLaurea != null && fileCorsiMateria != null && fileDipartimenti != null) {
		EditorPane.setText("<html><p style='color:red'>CORSI DI LAUREA: <span style='color:black'>file caricato</span></p>"
				+ "<p>&nbsp</p>"
				+ "<p style='color:red'>MATERIE DEI CORSI: <span style='color:black'>file caricato</span></p>"
				+ "<p>&nbsp</p>"
				+ "<p style='color:red'>DIPARTIMENTI: <span style='color:black'>file caricato</span></p>"
				+ "</html>");
		}
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		String title = CourseList.title;
		frmAsd = new JFrame();
		frmAsd.setResizable(false);
		frmAsd.setTitle(title);
		frmAsd.getContentPane().setBackground(SystemColor.inactiveCaption);
		frmAsd.setIconImage(Toolkit.getDefaultToolkit().getImage("media/f.png"));
		frmAsd.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				frmAsd.dispose();
				MainAdmin.main();
				
			}
		});
		frmAsd.setBounds(100, 100, 489, 398);
		frmAsd.getContentPane().setLayout(null);

		JEditorPane EditorPane = new JEditorPane();
		EditorPane.setContentType("text/html");
		EditorPane.setText(CourseList.description);
		EditorPane.setEditable(false);
		EditorPane.setBounds(208, 81, 249, 204);
		frmAsd.getContentPane().add(EditorPane);
		writeEditorPane(EditorPane);
		

		BTN_corsi_materia = new JButton("<html><center>Aggiungi CSV<br> delle materie<br>del corso</center></html>");
		BTN_corsi_materia.setVisible(true);
		BTN_corsi_materia.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				JFileChooser jf = new JFileChooser();
				int n = jf.showOpenDialog(null);
				if (n == 0) {
					File f = jf.getSelectedFile();
					String path = f.getPath();
					System.out.println("DEBUG: importa utenti: " + path);
					
					try {
						boolean b = path.contains(".csv")? true: false;
						if (!b) {
							JOptionPane.showMessageDialog(null, "ERRORE: seleziona un file con estensione .csv");
						} else {
							
							byte[] array = Files.readAllBytes(new File(path).toPath());
							fileCorsiMateria = array;

							writeEditorPane(EditorPane);
							
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
					

				}
				
			}
		});
		BTN_corsi_materia.setBounds(25, 158, 141, 47);
		frmAsd.getContentPane().add(BTN_corsi_materia);

		JButton BTN_dipartimenti = new JButton("<html><center>Aggiungi CSV<br> dei dipartimenti</center></html>");
		BTN_dipartimenti.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser jf = new JFileChooser();
				int n = jf.showOpenDialog(null);
				if (n == 0) {
					File f = jf.getSelectedFile();
					String path = f.getPath();
					System.out.println("DEBUG: importa utenti: " + path);
					
					try {
						boolean b = path.contains(".csv")? true: false;
						if (!b) {
							JOptionPane.showMessageDialog(null, "ERRORE: seleziona un file con estensione .csv");
						} else {
							
							byte[] array = Files.readAllBytes(new File(path).toPath());
							fileDipartimenti = array;
							
							writeEditorPane(EditorPane);
						}
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					

				}
			}
		});
		BTN_dipartimenti.setVisible(true);
		BTN_dipartimenti.setBounds(25, 236, 141, 47);
		frmAsd.getContentPane().add(BTN_dipartimenti);
		
		JButton BTN_corsi_laurea = new JButton("<html><center>Aggiungi CSV<br> dei corsi<br>di laurea</center></html>");
		BTN_corsi_laurea.setVisible(true);
		BTN_corsi_laurea.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				JFileChooser jf = new JFileChooser();
				int n = jf.showOpenDialog(null);
				if (n == 0) {
					File f = jf.getSelectedFile();
					String path = f.getPath();
					System.out.println("DEBUG: importa utenti: " + path);
					
					try {
						boolean b = path.contains(".csv")? true: false;
						if (!b) {
							JOptionPane.showMessageDialog(null, "ERRORE: seleziona un file con estensione .csv");
						} else {
							
							byte[] array = Files.readAllBytes(new File(path).toPath());
							fileCorsiLaurea = array;
							
							writeEditorPane(EditorPane);
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
					

				}
				
			}
		});
		BTN_corsi_laurea.setBounds(25, 81, 141, 47);
		frmAsd.getContentPane().add(BTN_corsi_laurea);
		
		JButton btnNewButton = new JButton("<html><center>Indietro</center></html>");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frmAsd.dispose();
				MainAdmin.main();
			}
		});
		btnNewButton.setBounds(25, 317, 167, 23);
		frmAsd.getContentPane().add(btnNewButton);
		
		JButton button = new JButton("<html><center>Invia dati</center></html>");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(fileCorsiMateria!= null && fileCorsiLaurea!= null && fileDipartimenti != null) {
					fileToSend = new Object[] {fileCorsiLaurea, fileCorsiMateria, fileDipartimenti };
					ClientConnection.importCSV(fileToSend);
				}
			}
		});
		button.setBounds(208, 317, 249, 23);
		frmAsd.getContentPane().add(button);

	}

	public static String getTitolo() {

		return titleFolder;
	}
}
