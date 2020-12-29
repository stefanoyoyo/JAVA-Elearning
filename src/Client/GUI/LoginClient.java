package Client.GUI;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import Client.ClientConnection;
import Client.LoggedUser;
import Common.Enumerators.UserType;

import javax.swing.JComboBox;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;




/**
 *  GUI
 * 
 * @author Amenta Stefano, Moroni Paolo
 *
 */
public class LoginClient {

	private JFrame frmLoginPittaformaSeatin;
	private JTextField txtInsertEmail;
	private JPasswordField pwdEmail;
	private JLabel lblNewLabel_1;
	public static UserType loginType;
	private long serverCount;
	public static LoggedUser loggedUser;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginClient window = new LoginClient();
					window.frmLoginPittaformaSeatin.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public LoginClient() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void initialize() {
		frmLoginPittaformaSeatin = new JFrame();
		frmLoginPittaformaSeatin.setResizable(false);
		frmLoginPittaformaSeatin.getContentPane().setBackground(SystemColor.inactiveCaption);
		frmLoginPittaformaSeatin.setFont(null);
		frmLoginPittaformaSeatin.setTitle("SEATIN");
		frmLoginPittaformaSeatin.setIconImage(Toolkit.getDefaultToolkit().getImage("media/f.png"));
		frmLoginPittaformaSeatin.setForeground(Color.WHITE);
		frmLoginPittaformaSeatin.setBackground(Color.GRAY);
		frmLoginPittaformaSeatin.setBounds(100, 100, 316, 430);
		frmLoginPittaformaSeatin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmLoginPittaformaSeatin.getContentPane().setLayout(null);
		ImageIcon icon = new ImageIcon("media/f.png");
		JLabel lblNewLabel = new JLabel(icon);
		lblNewLabel.setBounds(12, 13, 286, 132);
		frmLoginPittaformaSeatin.getContentPane().add(lblNewLabel);
		JLabel lblLogin = new JLabel("Nome utente: ");
		lblLogin.setHorizontalAlignment(SwingConstants.CENTER);
		lblLogin.setBounds(10, 189, 140, 16);
		frmLoginPittaformaSeatin.getContentPane().add(lblLogin);

		txtInsertEmail = new JTextField();
		txtInsertEmail.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				txtInsertEmail.setForeground(Color.BLACK);
				txtInsertEmail.setText("");
			}
		});
		JButton btnNewButton = new JButton("Esegui l'accesso");
		txtInsertEmail.setToolTipText("");
		txtInsertEmail.setForeground(Color.GRAY);
		txtInsertEmail.setText("insert email");
		txtInsertEmail.setHorizontalAlignment(SwingConstants.CENTER);
		txtInsertEmail.setBounds(147, 185, 140, 25);

		frmLoginPittaformaSeatin.getContentPane().add(txtInsertEmail);
		txtInsertEmail.setColumns(10);

		lblNewLabel_1 = new JLabel("Password:");
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1.setBounds(0, 225, 140, 16);
		frmLoginPittaformaSeatin.getContentPane().add(lblNewLabel_1);

		JComboBox comboBox = new JComboBox();
		comboBox.setToolTipText("");
		comboBox.setMaximumRowCount(3);
		comboBox.addItem(UserType.STUDENT);
		comboBox.addItem(UserType.TEACHER);
		comboBox.setBounds(147, 273, 140, 22);
		frmLoginPittaformaSeatin.getContentPane().add(comboBox);

		pwdEmail = new JPasswordField();
		pwdEmail.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				if (e.getKeyChar() == KeyEvent.VK_ENTER) {
					btnNewButton.doClick();
				}
			}
		});
		pwdEmail.addFocusListener(new FocusListener() {

			@Override
			public void focusLost(FocusEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void focusGained(FocusEvent e) {
				pwdEmail.setText("");

			}
		});
		pwdEmail.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				pwdEmail.setForeground(Color.BLACK);
				pwdEmail.setText("");
			}
		});
		pwdEmail.setEchoChar('*');
		pwdEmail.setHorizontalAlignment(SwingConstants.CENTER);
		pwdEmail.setToolTipText("password");
		pwdEmail.setForeground(Color.LIGHT_GRAY);
		pwdEmail.setBounds(147, 221, 140, 25);
		pwdEmail.setText("       ");
		frmLoginPittaformaSeatin.getContentPane().add(pwdEmail);
		JLabel contatore_errori = new JLabel("");
		contatore_errori.setForeground(Color.RED);
		contatore_errori.setBounds(12, 379, 286, 16);
		frmLoginPittaformaSeatin.getContentPane().add(contatore_errori);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				loggedUser = new LoggedUser();
				LoggedUser.anagrafica = null;
				
				/* CLIENT */
				String email = txtInsertEmail.getText();
				String psw = String.valueOf(pwdEmail.getPassword());
				UserType tipo_login = (UserType) comboBox.getSelectedItem();
				boolean credenziali = false;
				switch (tipo_login) {
				case STUDENT:
					loginType = UserType.STUDENT;
					credenziali = ClientConnection.loginRequest(email, psw, UserType.STUDENT);
					if (credenziali) {
					LoggedUser.anagrafica.Email = email;
					LoggedUser.anagrafica.Password = psw;
					}
					break;
				case TEACHER:
					loginType = UserType.TEACHER;
					credenziali = ClientConnection.loginRequest(email, psw, UserType.TEACHER);
					if (credenziali) {
					LoggedUser.anagrafica.Email = email;
					LoggedUser.anagrafica.Password = psw;
					}
					break;
				default:
					String s = "ERRORE NESSUNA SPECIFICA PER IL LOGIN SELEZIONATA";
					contatore_errori.setText(s);

					break;
				}
				


				if (credenziali == true) {
					ClientConnection.setZeroNumeroTentativiLogin(email);
					if (LoggedUser.anagrafica.trusted)// se true vuol dire che l'utente è verificato
					{

						ClientCourseList.main();
						frmLoginPittaformaSeatin.dispose();

					} else // l'account non è attivo: va avviato
					{
						// invio mail;
						AccountActivation.main(txtInsertEmail.getText());
					}
				} else {
					// creo un contatore degli errori sul server, e quindi sul DB
					
					
					txtInsertEmail.getText();
					serverCount = ClientConnection.getNumeroTentativiLogin(txtInsertEmail.getText());
					
					
					ClientConnection.incNumeroTentativiLogin(txtInsertEmail.getText());
					serverCount = ClientConnection.getNumeroTentativiLogin(txtInsertEmail.getText());
					System.out.println("DEBUG: LoginRequest richieste d'accesso presenti sul server: " + serverCount);
					
					String stringa_errore = "ERRORE Email o Password ERRATI";
					contatore_errori.setText(stringa_errore + ": " + serverCount);
					
//					if (c_errorepsw == 10) {
						if (serverCount == 10) {
						ClientConnection.bloccaAccount(email);
						ClientConnection.setZeroNumeroTentativiLogin(email);
						contatore_errori.setText("account bloccato controllare servizio mail");

					}

				}

			}
		});
		btnNewButton.setBounds(12, 314, 275, 25);
		frmLoginPittaformaSeatin.getContentPane().add(btnNewButton);

		JButton btnpsw_dimenticata = new JButton("Hai dimenticato la password?");
		btnpsw_dimenticata.setBorderPainted(false);
		btnpsw_dimenticata.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				EmailReset finestra = new EmailReset();
				frmLoginPittaformaSeatin.setVisible(false);
				finestra.main(null);

			}
		});
		btnpsw_dimenticata.setBackground(SystemColor.inactiveCaption);
		btnpsw_dimenticata.setForeground(Color.BLUE);
		btnpsw_dimenticata.setBounds(12, 352, 275, 25);
		frmLoginPittaformaSeatin.getContentPane().add(btnpsw_dimenticata);
		
		JLabel label = new JLabel("<html><center>Che utente sei?</center></html>");
		label.setBounds(39, 277, 101, 16);
		frmLoginPittaformaSeatin.getContentPane().add(label);

	}
}
