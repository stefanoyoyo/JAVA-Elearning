package Client.GUI;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;

import Client.ClientConnection;


/**
 *  GUI
 * 
 * @author Amenta Stefano, Moroni Paolo
 *
 */
public class AccountActivation {

	private JFrame frame;
	private JTextField codiceatt;

	/**
	 * Launch the application.
	 */
	public static void main(String mail) 
	{
		EventQueue.invokeLater(new Runnable() 
		{
			public void run() 
			{
				try 
				{
					AccountActivation window = new AccountActivation( mail);
					window.frame.setVisible(true);
				} catch (Exception e) 
				{
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public AccountActivation(String mail) {
		initialize(mail);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize(String mail) {
		frame = new JFrame();
		frame.setResizable(false);
		frame.setTitle("SEATIN");
		frame.getContentPane().setBackground(SystemColor.inactiveCaption);
		frame.getContentPane().setForeground(SystemColor.inactiveCaption);
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage("media/f.png"));
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JLabel lblAttivazioneNuovoAcount = new JLabel("Attivazione nuovo profilo");
		lblAttivazioneNuovoAcount.setForeground(Color.BLACK);
		lblAttivazioneNuovoAcount.setFont(new Font("Arial Black", Font.ITALIC, 21));
		lblAttivazioneNuovoAcount.setBounds(59, 13, 325, 33);
		frame.getContentPane().add(lblAttivazioneNuovoAcount);

		JLabel LB_newpsw = new JLabel("Nuova password");
		LB_newpsw.setVisible(false);
		LB_newpsw.setHorizontalAlignment(SwingConstants.CENTER);
		LB_newpsw.setFont(new Font("Tahoma", Font.PLAIN, 15));
		LB_newpsw.setBounds(12, 86, 192, 16);
		frame.getContentPane().add(LB_newpsw);

		JTextPane nuova_psw = new JTextPane();
		nuova_psw.setBounds(228, 86, 192, 22);
		frame.getContentPane().add(nuova_psw);
		nuova_psw.setVisible(false);

		JTextPane conferma_psw = new JTextPane();
		conferma_psw.setBounds(228, 122, 192, 22);
		frame.getContentPane().add(conferma_psw);
		conferma_psw.setVisible(false);

		JLabel LB_errori = new JLabel();
		LB_errori.setForeground(Color.RED);
		LB_errori.setVisible(false);
		LB_errori.setHorizontalAlignment(SwingConstants.CENTER);
		LB_errori.setBounds(12, 200, 420, 16);
		frame.getContentPane().add(LB_errori);
         // Dopo la conferma del codice di attivazione viene   aggiornata password e utente rimandato a login iniziale
		JButton BTN_creaprf = new JButton("Attiva profilo");
		BTN_creaprf.setVisible(false);
		BTN_creaprf.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String psw_aggiornata = nuova_psw.getText();
				String conferma = conferma_psw.getText();
				if ((psw_aggiornata.equals(conferma)) == true) 
				{
					
					// frame.dispose è stata spostata prima per vedere se chiude prima frame e poi mostra popup e loginClient
					frame.dispose();
					ClientConnection.cambiaPasswordByMail(mail,psw_aggiornata );
					JOptionPane.showMessageDialog(null, "Password Aggiornata");
					
					LoginClient.main(null);
				} else {
					LB_errori.setText("le password non coincidono");
					LB_errori.setVisible(true);
					nuova_psw.setText("");
					conferma_psw.setText("");
				}

			}
		});
		BTN_creaprf.setBounds(228, 227, 192, 25);
		frame.getContentPane().add(BTN_creaprf);

		JLabel LB_confermapsw = new JLabel("Conferma password");
		LB_confermapsw.setHorizontalAlignment(SwingConstants.CENTER);
		LB_confermapsw.setFont(new Font("Tahoma", Font.PLAIN, 15));
		LB_confermapsw.setBounds(12, 128, 182, 16);
		frame.getContentPane().add(LB_confermapsw);
		LB_confermapsw.setVisible(false);

		JLabel LB_controllocodice = new JLabel("inserisci codice di attivazione");
		LB_controllocodice.setHorizontalAlignment(SwingConstants.CENTER);
		LB_controllocodice.setBounds(12, 164, 192, 16);
		frame.getContentPane().add(LB_controllocodice);

		codiceatt = new JTextField();
		codiceatt.setBounds(225, 161, 195, 22);
		frame.getContentPane().add(codiceatt);
		codiceatt.setColumns(10);

		JButton BTN_creaprofilo = new JButton("Verifica codce");
		BTN_creaprofilo.setBounds(12, 227, 192, 25);
		frame.getContentPane().add(BTN_creaprofilo);
		BTN_creaprofilo.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				// VERIFICO CHE IL CODICE ESISTE SUL SERVER...
				// SE ESISTE :
				Long CODICEATT = Long.parseLong(codiceatt.getText());// parte inutile controlo va eseguito su server
				if (ClientConnection.verificaCod(Client.LoggedUser.anagrafica.userID, CODICEATT))// ..............................................................
				{
					LB_errori.setVisible(false);
					LB_controllocodice.setVisible(false);
					codiceatt.setVisible(false);
					BTN_creaprofilo.setVisible(false);
					LB_newpsw.setVisible(true);
					nuova_psw.setVisible(true);
					LB_confermapsw.setVisible(true);
					conferma_psw.setVisible(true);
					BTN_creaprf.setVisible(true);
				} else {
					LB_errori.setText("codice di aytivazione erratop o scaduto");
					LB_errori.setVisible(true);
				}
			}
		});
	}
}
