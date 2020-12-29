package Client.GUI;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.Color;
import javax.swing.SwingConstants;

import Client.ClientConnection;

import javax.swing.JButton;
import java.awt.SystemColor;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;



/**
 *  GUI
 * 
 * @author Amenta Stefano, Moroni Paolo
 *
 */
public class EmailReset {

	private JFrame frame;
	private JTextField mailField;

	/**
	 * Launch the application.
	 */
	public void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					EmailReset window = new EmailReset();
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
	public EmailReset() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setResizable(false);
		frame.setTitle("SEATIN");
		frame.getContentPane().setBackground(SystemColor.inactiveCaption);
		frame.getContentPane().setForeground(SystemColor.inactiveCaption);
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage("media/f.png"));
		frame.setBounds(100, 100, 452, 253);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.addWindowListener(new WindowAdapter() 
		{
			@Override
			public void windowClosing(WindowEvent arg0) 
			{
				LoginClient.main(null);
				frame.dispose();
			}
		});

		JLabel label_titolo = new JLabel("Ripristino della password");
		label_titolo.setForeground(Color.BLACK);
		label_titolo.setFont(new Font("Arial Black", Font.ITALIC, 21));
		label_titolo.setBounds(59, 13, 325, 33);
		frame.getContentPane().add(label_titolo);

		JLabel lblLePasswordNon = new JLabel();
		lblLePasswordNon.setForeground(Color.RED);
		lblLePasswordNon.setVisible(false);
		lblLePasswordNon.setHorizontalAlignment(SwingConstants.CENTER);
		lblLePasswordNon.setBounds(12, 236, 420, 16);
		frame.getContentPane().add(lblLePasswordNon);

		JButton btnVerificaEmail = new JButton("Verifica email");
		btnVerificaEmail.setBounds(12, 164, 420, 25);
		frame.getContentPane().add(btnVerificaEmail);
		
		mailField = new JTextField();
		mailField.setBounds(12, 115, 420, 22);
		frame.getContentPane().add(mailField);
		mailField.setColumns(10);
		
		JLabel label = new JLabel("La nuova password verr\u00E0 inviata tramite e-mail ");
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setFont(new Font("Tahoma", Font.PLAIN, 14));
		label.setBounds(44, 57, 311, 16);
		frame.getContentPane().add(label);
		btnVerificaEmail.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				
			ClientConnection.bloccaAccount(mailField.getText());
			
			frame.dispose();
			LoginClient.main(null);
			
			
			}
		});

	}
}
