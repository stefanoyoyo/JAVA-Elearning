package Admin.GUI;
import java.awt.EventQueue;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import Client.ClientConnection;
import Common.Enumerators.UserType;

import java.awt.Color;
import java.awt.Font;



/**
 *  GUI
 * 
 * @author Amenta Stefano, Moroni Paolo
 *
 */
public class UtenteCSV {

	private JFrame frmSe;

	/**
	 * Launch the application.
	 */
	
	public static void main() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UtenteCSV window = new UtenteCSV();
					window.frmSe.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public UtenteCSV() {
		initialize();
	}

	/**
	 *
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmSe = new JFrame();
		frmSe.getContentPane().setBackground(Color.DARK_GRAY);
		frmSe.setResizable(false);
		frmSe.setTitle("seatIn-ADMIN");
		frmSe.setIconImage(Toolkit.getDefaultToolkit().getImage("media/f.png"));
		frmSe.addWindowListener(new WindowAdapter() 
		{
			@Override
			public void windowClosing(WindowEvent arg0) 
			{
				int scelta = JOptionPane.showConfirmDialog(null,
						"sei sicuro di voler uscire dal programma", "chiusura programma", JOptionPane.YES_NO_OPTION);
				if(scelta == 0)
				{
					frmSe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					Login.close();
				}
				else if(scelta == 1)
				{
					frmSe.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
				}
				
			}
		});
		frmSe.setIconImage(Toolkit.getDefaultToolkit().getImage("media/f.png"));
		frmSe.setBounds(100, 100, 340, 210);
		
		frmSe.getContentPane().setLayout(null);
		
		JButton btnAgg_corso = new JButton("<html><center>Importa<br>CSV<br>studenti</center></html>");
		btnAgg_corso.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				JFileChooser jf = new JFileChooser();
				int n = jf.showOpenDialog(null);
				if (n == 0) {
					File f = jf.getSelectedFile();
					String path = f.getPath();
					
					try {
						boolean b = path.contains(".csv")? true: false;
						if (!b) {
							JOptionPane.showMessageDialog(null, "ERRORE: seleziona un file con estensione .csv");
						} else {
							byte[] array = Files.readAllBytes(new File(path).toPath());
							ClientConnection.importCSV(array, UserType.STUDENT);
							// chiamo il controller del server. Creo un metodo dove passo l'array di byte come parametro
							
						}
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					

				}
			}
		});
		btnAgg_corso.setBounds(224, 36, 97, 86);
		frmSe.getContentPane().add(btnAgg_corso);
		
		JButton btnVisualizzaCorsi = new JButton("<html><center>Importa<br>CSV<br>docenti</center></html>");
		
		
		btnVisualizzaCorsi.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e)
			{
				JFileChooser jf = new JFileChooser();
				int n = jf.showOpenDialog(null);
				if (n == 0) {
					File f = jf.getSelectedFile();
					String path = f.getPath();
					System.out.println("DEBUG: importa utenti: " + path);

					
//					ClientConnection.DocumentUpload(str, "");
//					JOptionPane.showMessageDialog(null, "File aggiunto con successo");
					
//					Devo dare in pasto al server il file CSV
//					ClientConnection.DocumentDownload(idDoc, id.Matricola, path, "txt");
					
//					ClientConnection.importCSV(byte[] file);
					
					try {
						boolean b = path.contains(".csv")? true: false;
						if (!b) {
							JOptionPane.showMessageDialog(null, "ERRORE: seleziona un file con estensione .csv");
						} else {
							byte[] array = Files.readAllBytes(new File(path).toPath());
							System.out.println("DEBUG: ciao");
							ClientConnection.importCSV(array, UserType.TEACHER);
							// chiamo il controller del server. Creo un metodo dove passo l'array di byte come parametro
							
						}
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					

				}
			}
			
		});
		btnVisualizzaCorsi.setBounds(117, 36, 97, 86);
		frmSe.getContentPane().add(btnVisualizzaCorsi);
		

		JButton btnNuovoutente = new JButton("<html><center>Importa<br>CSV<br>admin</center></html>");
		
	
		btnNuovoutente.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0) 
			{
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
							System.out.println("DEBUG: ciao");
							ClientConnection.importCSV(array, UserType.ADMIN);
							// chiamo il controller del server. Creo un metodo dove passo l'array di byte come parametro
							
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
					

				}
				
				
				// GetUserData window_anag = new GetUserData();
			}
		});
		btnNuovoutente.setBounds(10, 36, 97, 86);
		frmSe.getContentPane().add(btnNuovoutente);
		
		JLabel lblBenvenutoNelProgramma = new JLabel("SeatInAdmin - Menu principale");
		lblBenvenutoNelProgramma.setBackground(Color.WHITE);
		lblBenvenutoNelProgramma.setFont(new Font("Times New Roman", Font.PLAIN, 18));
		lblBenvenutoNelProgramma.setForeground(Color.WHITE);
		lblBenvenutoNelProgramma.setHorizontalAlignment(SwingConstants.CENTER);
		lblBenvenutoNelProgramma.setBounds(10, 11, 314, 14);
		frmSe.getContentPane().add(lblBenvenutoNelProgramma);
		
		JButton button = new JButton("<html><center>indietro</center></html>");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				frmSe.dispose();
				MainAdmin.main();
			}
		});
		button.setBounds(10, 133, 311, 38);
		frmSe.getContentPane().add(button);
		
		
		
	}
}
