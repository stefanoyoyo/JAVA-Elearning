package Admin.GUI;

import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import Client.ClientConnection;
import Common.Enumerators.UserType;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Login {

	protected static final String Button1 = null;
	protected static final Component MOUSE_CLICKED = null;
	private static JFrame frame;
	private JTextField txtInsertEmail;
	private JPasswordField pwdEmail;
	private JLabel lblNewLabel_1;

	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Login window = new Login();
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
	public Login() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setBackground(SystemColor.inactiveCaption);
		frame.setFont(null);
		frame.setTitle("ADMIN-LOG");
		frame.setResizable(false);
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage("media/f.png"));
		frame.setForeground(Color.WHITE);
		frame.setBackground(Color.GRAY);
		frame.setBounds(100, 100, 316, 411);
		frame.getContentPane().setLayout(null);
		frame.addWindowListener(new WindowAdapter() 
		{
			@Override
			public void windowClosing(WindowEvent arg0) 
			{
				int scelta = JOptionPane.showConfirmDialog(null,
						"sei sicuro di voler uscire dal programma", "chiusura programma", JOptionPane.YES_NO_OPTION);
				if(scelta == 0)
				{
					frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					Login.close();
				}
				else if(scelta == 1)
				{
					frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
				}
				
			}
		});
		
		
		JButton btnNewButton = new JButton("Esegui l'accesso");
		JLabel lblNewLabel = new JLabel(new ImageIcon("C:\\Users\\ament\\Documents\\MEGA\\Documenti\\Documenti\\Progetti JAVA\\Common WORKSPACE\\[b] Lab B Mirko2\\Media\\InsubriaTrasparent.png"));
		
		
		
		lblNewLabel.setBounds(12, 13, 286, 132);
		frame.getContentPane().add(lblNewLabel);
		JLabel lblLogin = new JLabel("Nome utente");
		lblLogin.setHorizontalAlignment(SwingConstants.CENTER);
		lblLogin.setBounds(12, 186, 117, 16);
		frame.getContentPane().add(lblLogin);

		txtInsertEmail = new JTextField();
        

		txtInsertEmail.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				txtInsertEmail.setForeground(Color.BLACK);
				txtInsertEmail.setText("");
			}
		});
		txtInsertEmail.setToolTipText("");
		txtInsertEmail.setForeground(Color.GRAY);
		txtInsertEmail.setText("insert email");
		txtInsertEmail.setHorizontalAlignment(SwingConstants.CENTER);
		txtInsertEmail.setBounds(139, 182, 159, 25);

		frame.getContentPane().add(txtInsertEmail);
		txtInsertEmail.setColumns(10);

		lblNewLabel_1 = new JLabel("Password:");
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1.setBounds(12, 236, 117, 16);
		frame.getContentPane().add(lblNewLabel_1);
		pwdEmail = new JPasswordField();
		pwdEmail.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
                if(e.getKeyChar()==KeyEvent.VK_ENTER){
                    btnNewButton.doClick();
                }
            }
		});
		pwdEmail.addFocusListener(new FocusListener() 
		{
			
			@Override
			public void focusLost(FocusEvent e) 
			{
			}
			
			@Override
			public void focusGained(FocusEvent e) 
			{
				pwdEmail.setText("");
			}
		});
		pwdEmail.addMouseListener(new MouseAdapter() 
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{
				pwdEmail.setText(null);
				
			}
		});
		pwdEmail.setEchoChar('*');
		pwdEmail.setHorizontalAlignment(SwingConstants.CENTER);
		pwdEmail.setToolTipText("password");
		pwdEmail.setForeground(Color.LIGHT_GRAY);
		pwdEmail.setBounds(140, 232, 158, 25);
		pwdEmail.setText("       ");
		frame.getContentPane().add(pwdEmail);
		JLabel contatore_errori = new JLabel("");
		contatore_errori.setHorizontalAlignment(SwingConstants.CENTER);
		contatore_errori.setForeground(Color.RED);
		contatore_errori.setBounds(6, 348, 286, 16);
		frame.getContentPane().add(contatore_errori);
		
		btnNewButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {

				String email = txtInsertEmail.getText();
				String psw = String.valueOf(pwdEmail.getPassword());

				if (ClientConnection.loginRequest(email, psw, UserType.ADMIN)) 
				{
					MainAdmin.main();
					frame.dispose();
				}
				else 
				{

					String stringa_errore = "ERRORE Email o Password ERRATI";
					contatore_errori.setText(stringa_errore);

				}

			}
		});
		btnNewButton.setBounds(12, 276, 286, 25);
		frame.getContentPane().add(btnNewButton);

		JButton btnpsw_dimenticata = new JButton("Hai dimenticato la password?");
		btnpsw_dimenticata.setBorderPainted(false);
		btnpsw_dimenticata.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.setVisible(false);
				AdminEmailReset.main();

			}
		});
		btnpsw_dimenticata.setBackground(SystemColor.inactiveCaption);
		btnpsw_dimenticata.setForeground(Color.BLUE);
		btnpsw_dimenticata.setBounds(12, 312, 286, 25);
		frame.getContentPane().add(btnpsw_dimenticata);

	}

	public static void close() 
	{
		System.exit(0);
		
	}
	

}
