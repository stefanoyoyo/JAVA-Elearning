package Client.GUI;

import java.awt.EventQueue;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JLabel;

import Client.LoggedUser;
import Common.DBType.Student;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;



/**
 *  GUI
 * 
 * @author Amenta Stefano, Moroni Paolo
 *
 */
public class ClientAccountData {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClientAccountData window = new ClientAccountData();
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
	public ClientAccountData() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setResizable(false);
		frame.setBounds(100, 100, 392, 223);
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage("media/f.png"));
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblnew4 = new JLabel("Nome:");
		lblnew4.setBounds(22, 27, 56, 16);
		frame.getContentPane().add(lblnew4);
		
		JLabel lblnew3 = new JLabel("Cognome:");
		lblnew3.setBounds(22, 56, 70, 16);
		frame.getContentPane().add(lblnew3);
		
		JLabel lblnew2 = new JLabel("Mail:");
		lblnew2.setBounds(22, 85, 56, 16);
		frame.getContentPane().add(lblnew2);
		
		JLabel lblnew5 = new JLabel("Anno immatricolazione:");
		lblnew5.setBounds(22, 114, 151, 16);
		frame.getContentPane().add(lblnew5);
		
		JLabel lblyear = new JLabel();
		lblyear.setBounds(199, 114, 56, 16);
		frame.getContentPane().add(lblyear);
		
		JLabel lblmail = new JLabel("");
		lblmail.setBounds(201, 85, 112, 16);
		frame.getContentPane().add(lblmail);
		
		JLabel lblsurename = new JLabel("");
		lblsurename.setBounds(201, 56, 112, 16);
		frame.getContentPane().add(lblsurename);
		
		JLabel lblname = new JLabel("Antonio");
		lblname.setBounds(201, 27, 112, 16);
		frame.getContentPane().add(lblname);
		
		
		 Student studente = (Client.ClientConnection.getStudenteByMat(LoggedUser.anagrafica.userID));
		 
		 
		 
		lblname.setText(LoggedUser.anagrafica.name);
		lblyear.setText(String.valueOf(studente.startYear));
        lblsurename.setText(LoggedUser.anagrafica.surname);
        lblmail.setText(LoggedUser.anagrafica.Email);
        
        
		
		
        JButton btnindietro = new JButton("Torna indietro");
		btnindietro.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			

//			ClientCourseList.main();
			frame.dispose();	
			
			
			}
		});
		btnindietro.setBounds(22, 143, 340, 25);
		frame.getContentPane().add(btnindietro);
	
	}
}
