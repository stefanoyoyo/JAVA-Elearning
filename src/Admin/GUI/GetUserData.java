package Admin.GUI;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import Client.ClientConnection;
import Common.DBType.Admin;
import Common.DBType.Userdatas;
import Common.Enumerators.UserType;

import java.awt.Font;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JFormattedTextField;
import javax.swing.JPasswordField;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;



/**
 *  GUI
 * 
 * @author Amenta Stefano, Moroni Paolo
 *
 */
public class GetUserData {

	private JFrame frame;
	private JFormattedTextField nw_nome;
	private JFormattedTextField nw_cognome;
	private JPasswordField nw_psw;
	private JFormattedTextField nw_email;
	private static String Nome;
	private static String Cognome;
	private static String Email;
	private static String Psw;
	private static Userdatas Dati_an;
	static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

	/**
	 * Launch the application.
	 */
	public static void main() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GetUserData window = new GetUserData();
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
	public GetUserData() {
		initialize();
	}
	
	
	public static String translateUserType (UserType user) {
		switch(user) {
		case STUDENT:
			return "Studente";
		case TEACHER: 
			return "Docente";
		case ADMIN:
			return "Admin";
		}
		return null;
	}

	/**
	 * Initialize the contents of the frame.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void initialize() {
		frame = new JFrame();
		frame.setResizable(false);
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent arg0) {
				MainAdmin.main();
			}
		});
		frame.setTitle("Nuovo utente");
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage("media/f.png"));
		frame.setForeground(Color.WHITE);
		frame.setBackground(Color.GRAY);
		frame.setBounds(100, 100, 342, 300);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		nw_nome = new JFormattedTextField();
		nw_nome.setBounds(97, 39, 229, 22);
		frame.getContentPane().add(nw_nome);
		nw_nome.setColumns(10);

		nw_cognome = new JFormattedTextField();
		nw_cognome.setColumns(10);
		nw_cognome.setBounds(97, 74, 229, 22);
		frame.getContentPane().add(nw_cognome);

		nw_email = new JFormattedTextField();
		nw_email.setColumns(10);
		nw_email.setBounds(97, 109, 229, 22);
		frame.getContentPane().add(nw_email);

		nw_psw = new JPasswordField();
		nw_psw.setEchoChar('*');
		nw_psw.setColumns(10);
		nw_psw.setBounds(97, 144, 229, 22);
		frame.getContentPane().add(nw_psw);

		JLabel lb_nome = new JLabel("NOME");
		lb_nome.setBounds(12, 39, 39, 22);
		frame.getContentPane().add(lb_nome);

		JLabel lb_cognome = new JLabel("COGNOME");
		lb_cognome.setBounds(12, 74, 70, 22);
		frame.getContentPane().add(lb_cognome);

		JLabel lb_email = new JLabel("EMAIL");
		lb_email.setBounds(12, 109, 56, 22);
		frame.getContentPane().add(lb_email);

		JLabel lb_password = new JLabel("PASSWORD");
		lb_password.setBounds(12, 144, 89, 22);
		frame.getContentPane().add(lb_password);
		JButton btn_pgsuc = new JButton("pagina successiva");

		JComboBox comboBox = new JComboBox();
		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				UserType ut = (UserType) comboBox.getSelectedItem();
				if (ut == UserType.ADMIN) {
					btn_pgsuc.setText("Registra");
				}
			}
		});
		
		
		comboBox.setBounds(97, 185, 229, 32);
		comboBox.addItem(UserType.STUDENT);
		comboBox.addItem(UserType.TEACHER);
		comboBox.addItem(UserType.ADMIN);
		frame.getContentPane().add(comboBox);

		btn_pgsuc.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				Nome = nw_nome.getText();
				Cognome = nw_cognome.getText();
				Email = nw_email.getText();
				Psw = String.valueOf(nw_psw.getPassword());

				Dati_an = new Userdatas(Nome, Cognome, Email, Psw, false, false, 0);
				UserType ut = (UserType) comboBox.getSelectedItem();
				switch (ut) {
				case STUDENT:
					if (Email.matches(EMAIL_PATTERN)) {
						frame.setVisible(false);
						NewStudentData.main();
					} else
						JOptionPane.showMessageDialog(null, "Mail non valida");
					break;
				case TEACHER:
					if (Email.matches(EMAIL_PATTERN)) {
						frame.setVisible(false);
						NewTeacherData.main();
					} else
						JOptionPane.showMessageDialog(null, "Mail non valida");
					break;
				case ADMIN:
					if (Email.matches(EMAIL_PATTERN)) {
						Admin newadmin = new Admin();

						newadmin.setName(Nome);
						newadmin.setSurname(Cognome);
						newadmin.setEmail(Email);
						newadmin.setPassword(Psw);
						ClientConnection.creaAdmin(newadmin);
						JOptionPane.showMessageDialog(null, "ADMIN creato con successo");
						frame.dispose();
					} else
						JOptionPane.showMessageDialog(null, "Mail non valida");

					break;
				default:
					break;

				}
			}
		});
		btn_pgsuc.setBounds(169, 228, 157, 32);
		frame.getContentPane().add(btn_pgsuc);
		
		JLabel label = new JLabel("Creazione nuovo account utente");
		label.setForeground(Color.BLACK);
		label.setFont(new Font("Tahoma", Font.BOLD, 17));
		label.setBounds(3, 11, 292, 16);
		frame.getContentPane().add(label);
		
		JLabel label_1 = new JLabel("Tipo di utente");
		label_1.setBounds(1, 194, 89, 16);
		frame.getContentPane().add(label_1);
		
		JButton btnNewButton = new JButton("Indietro");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//				MainAdmin.main();
				frame.dispose();
			}
		});
		btnNewButton.setBounds(12, 228, 147, 32);
		frame.getContentPane().add(btnNewButton);

	}

	protected void ritorno() {
		frame.setVisible(true);
		nw_nome.setText(Nome);
		nw_cognome.setText(Cognome);
		nw_email.setText(Email);
		nw_psw.setText(Psw);

	}

	public static Userdatas getDati() {
		return Dati_an;

	}
}
