package Admin.GUI;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Toolkit;
import javax.swing.SwingConstants;

import Client.ClientConnection;

import javax.swing.JButton;
import java.awt.SystemColor;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import java.awt.Font;

/**
 *  GUI
 * 
 * @author Amenta Stefano, Moroni Paolo
 *
 */
public class AdminEmailReset {

	private JFrame frame;
	private JTextField email_conferma;

	/**
	 * Launch the application.
	 */
	public  static void main() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AdminEmailReset window = new AdminEmailReset();
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
	public AdminEmailReset() {
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
		frame.setBounds(100, 100, 450, 300);
		frame.getContentPane().setLayout(null);
		frame.addWindowListener(new WindowAdapter() 
		{
			@Override
			public void windowClosing(WindowEvent arg0) 
			{
				Login.main(null);
				frame.dispose();
			}
		});

		
        
		
		email_conferma = new JTextField();
		
		email_conferma.setBounds(20, 137, 414, 22);
		frame.getContentPane().add(email_conferma);
		

		email_conferma.setColumns(10);
		
		
		JButton btnverificamail = new JButton("Reset password");
		btnverificamail.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				
		ClientConnection.bloccaAccount(email_conferma.getText());		
					
		frame.setVisible(false);
					Login.main(null);
				}

		
		});
		btnverificamail.setBounds(20, 220, 414, 25);
		frame.getContentPane().add(btnverificamail);

		JLabel lblInserisciCodiceAttivazione = new JLabel("Inserisci e-mail amministratore");
		lblInserisciCodiceAttivazione.setHorizontalAlignment(SwingConstants.CENTER);
		lblInserisciCodiceAttivazione.setBounds(10, 110, 424, 16);
		frame.getContentPane().add(lblInserisciCodiceAttivazione);
		
		JLabel label = new JLabel("Ripristino password");
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setFont(new Font("Tahoma", Font.BOLD, 17));
		label.setBounds(80, 11, 269, 27);
		frame.getContentPane().add(label);
		
		JLabel label_1 = new JLabel("La nuova password sar\u00E0 inviata via e-mail");
		label_1.setBounds(95, 37, 244, 27);
		frame.getContentPane().add(label_1);

	}
}
