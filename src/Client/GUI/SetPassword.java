package Client.GUI;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Toolkit;

import javax.swing.JPasswordField;
import java.awt.Window.Type;



/**
 *  GUI
 * 
 * @author Amenta Stefano, Moroni Paolo
 *
 */
public class SetPassword {

	private JFrame frmChangePassord;
	private JPasswordField passwordField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SetPassword window = new SetPassword();
					window.frmChangePassord.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public SetPassword() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmChangePassord = new JFrame();
		frmChangePassord.setType(Type.UTILITY);
		frmChangePassord.setTitle("Cambia password");
		frmChangePassord.setIconImage(Toolkit.getDefaultToolkit().getImage("media/f.png"));
		frmChangePassord.setBounds(100, 100, 279, 165);
		frmChangePassord.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frmChangePassord.getContentPane().setLayout(null);
		
		JLabel lblInserN = new JLabel("Inserisci la nuova password");
		lblInserN.setFont(new Font("Tahoma", Font.PLAIN, 17));
		lblInserN.setBounds(10, 26, 243, 27);
		frmChangePassord.getContentPane().add(lblInserN);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(10, 66, 243, 22);
		frmChangePassord.getContentPane().add(passwordField);
	}
}
