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
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import Client.ClientConnection;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JMenuBar;
import java.awt.SystemColor;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.ImageIcon;



/**
 *  GUI
 * 
 * @author Amenta Stefano, Moroni Paolo
 *
 */
public class MainAdmin {

	private JFrame frmSe;

	/**
	 * Launch the application.
	 */
	
	public static void main() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainAdmin window = new MainAdmin();
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
	public MainAdmin() {
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
		frmSe.setBounds(100, 100, 448, 394);
		
		frmSe.getContentPane().setLayout(null);
		
		JButton btnAgg_corso = new JButton("<html><center>Assegna<br>Corso</center></html>");
		btnAgg_corso.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				GetTeachersList.main();
				frmSe.dispose();
			}
		});
		btnAgg_corso.setBounds(224, 57, 97, 86);
		frmSe.getContentPane().add(btnAgg_corso);
		
		JButton Btn_Monitoring = new JButton("<html><center>monitoraggio<br>piattaforma</center></html>");
		Btn_Monitoring.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				Object[] options = new Object[] {};
				JComboBox<String> jcd = new JComboBox<String>();
				jcd.addItem("monitoraggio utente");
				jcd.addItem("monitoraggio admin");
				JOptionPane jop = new JOptionPane("Please Select", JOptionPane.QUESTION_MESSAGE,
						JOptionPane.DEFAULT_OPTION, null, options, null);
				
				JButton confirm = new JButton("conferma scelta");
				
				jop.add(jcd);
				jop.add(confirm);
				JDialog diag = new JDialog();
				diag.getContentPane().add(jop);
				diag.pack();
				diag.setVisible(true);
				confirm.addActionListener(new ActionListener() 
				{

					@Override
					public void actionPerformed(ActionEvent e) 
					{
						String control = (String)jcd.getSelectedItem();
						if(control.equals("monitoraggio utente"))
						{
							AdminMonitoring.main();
							diag.dispose();
							
						}
						else if(control.equals("monitoraggio admin"))
						{
							Monitoryng.main();
							diag.dispose();
						}
					}
					
					
				});
			}
		});
		Btn_Monitoring.setBounds(10, 154, 97, 86);
		frmSe.getContentPane().add(Btn_Monitoring);
		
		JButton btnVisualizzaCorsi = new JButton("<html><center>visualizza<br>Corsi</center></html>");
		
		
		btnVisualizzaCorsi.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e)
			{
				CourseList.main();
				frmSe.dispose();
			}
			
		});
		btnVisualizzaCorsi.setBounds(117, 57, 97, 86);
		frmSe.getContentPane().add(btnVisualizzaCorsi);
		
		JButton btn_new_course = new JButton("<html><center>Crea<br>Corso</center></html>");
		btn_new_course.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				new CourseCreate();
				CourseCreate.main(null);
				frmSe.dispose();
			}
		});
		btn_new_course.setBounds(117, 154, 97, 86);
		frmSe.getContentPane().add(btn_new_course);
		

		JButton btnNuovoutente = new JButton("<html><center>Registra<br>nuovo utente</center></html>");
		
	
		btnNuovoutente.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				// GetUserData window_anag = new GetUserData();
				GetUserData.main();
				frmSe.dispose();
			}
		});
		btnNuovoutente.setBounds(10, 57, 97, 86);
		frmSe.getContentPane().add(btnNuovoutente);
		
		JButton btmMod_user = new JButton("<html><center>modifica<br>utente</center></html>");
		btmMod_user.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				new UserEdit();
				UserEdit.main();
				frmSe.dispose();
			}
		});
		btmMod_user.setBounds(331, 57, 97, 86);
		frmSe.getContentPane().add(btmMod_user);
		
		JLabel lblBenvenutoNelProgramma = new JLabel("SeatInAdmin - Menu principale");
		lblBenvenutoNelProgramma.setBackground(Color.WHITE);
		lblBenvenutoNelProgramma.setFont(new Font("Times New Roman", Font.PLAIN, 18));
		lblBenvenutoNelProgramma.setForeground(Color.WHITE);
		lblBenvenutoNelProgramma.setHorizontalAlignment(SwingConstants.CENTER);
		lblBenvenutoNelProgramma.setBounds(14, 32, 414, 14);
		frmSe.getContentPane().add(lblBenvenutoNelProgramma);
		
		JButton button = new JButton("<html><center>Importa<br>Utenti</center></html>");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				frmSe.dispose();
				new UtenteCSV();
				UtenteCSV.main();
			
			}
		});
		button.setBounds(224, 154, 97, 86);
		frmSe.getContentPane().add(button);
		
		JButton button_1 = new JButton("<html><center>Importa<br>Corsi</center></html>");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//				JFileChooser jf = new JFileChooser();
//				int n = jf.showOpenDialog(null);
//				if (n == 0) {
//					File f = jf.getSelectedFile();
//					String str = f.getPath();
//
//					
////					ClientConnection.DocumentUpload(str, "");
//					JOptionPane.showMessageDialog(null, "File aggiunto con successo");
//				}
				frmSe.dispose();
				CorsiCSV.Main();
			}
		});
		button_1.setBounds(331, 154, 97, 86);
		frmSe.getContentPane().add(button_1);
		
		JButton btnGuida = new JButton("");
		btnGuida.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				frmSe.dispose();
				Guide.main();
			}
		});
		btnGuida.setIcon(new ImageIcon(MainAdmin.class.getResource("/com/sun/javafx/scene/control/skin/caspian/dialog-confirm.png")));
		btnGuida.setSelectedIcon(new ImageIcon(MainAdmin.class.getResource("/com/sun/javafx/scene/control/skin/caspian/dialog-confirm.png")));
		btnGuida.setBounds(331, 251, 101, 86);
		frmSe.getContentPane().add(btnGuida);
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBackground(SystemColor.controlHighlight);
		menuBar.setBounds(0, 0, 449, 21);
		frmSe.getContentPane().add(menuBar);
		
		JMenu menu = new JMenu("menu");
		menuBar.add(menu);
		
		JMenuItem menuItem_3 = new JMenuItem("esci");
		menuItem_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frmSe.dispose();
				new Login();
				Login.main(null);
			}
		});
		menu.add(menuItem_3);
		
		JMenuItem menuItem_4 = new JMenuItem("Chiudi");
		menuItem_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int scelta = JOptionPane.showConfirmDialog(null,
						"sei sicuro di voler uscire dal programma", "chiusura programma", JOptionPane.YES_NO_OPTION);
				if(scelta == 0)
				{
					frmSe.dispose();
					Login.close();
					ClientConnection.close();
				}
				else if(scelta == 1)
				{
					frmSe.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
				}	
			}
		});
		menu.add(menuItem_4);
		
		JButton button_2 = new JButton("<html><center>Elimina<br>utente</center></html>");
		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frmSe.dispose();
				UserDelete.main();
			}
		});
		button_2.setBounds(10, 251, 97, 86);
		frmSe.getContentPane().add(button_2);
		
		JButton button_3 = new JButton("<html><center>Elimina<br>Corso</center></html>");
		button_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				frmSe.dispose();
				CourseDelete.main();
			}
		});
		button_3.setBounds(117, 251, 97, 86);
		frmSe.getContentPane().add(button_3);
		
		JButton button_4 = new JButton("<html><center>Sblocca<br>profilo<br>sospeso</center></html>");
		button_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frmSe.dispose();
//				UserDelete userDelete = new UserDelete();
//				userDelete.main();
				UserUnlock.main();
			}
		});
		button_4.setBounds(224, 251, 97, 86);
		frmSe.getContentPane().add(button_4);
		
		
		
	}
}
