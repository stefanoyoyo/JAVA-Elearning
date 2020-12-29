package Server.Utilities;

import java.util.Properties;
import java.util.Scanner;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import Server.ServerThread;

/**
 *  @author Amenta Stefano, Moroni Paolo 
 * Classe per l'invio delle mail 
 * 
 */
public class EmailSender {;
	
	private static final String usr = "samenta@studenti.uninsubria.it";
	private static String pwd = null ;

	public static String SetPassword(Scanner s) {
		try {
			System.out.println("Inserire Password Email samenta@studenti.uninsubria.it:\n");
			pwd=s.nextLine();
		}
		catch(Exception e) {
			System.out.println(e);
		}
		return pwd;
	}
	
	/** Function to send an e-mail */
	public static void send_uninsubria_email(String to, String subject, String body) {
		try {
			
			String password = null;
			String username = null;
			
			try {
			password = ServerThread.loggedUser.Password ; 
			username = ServerThread.loggedUser.Email ;
			if (!ServerThread.loggedUser.trusted) {
				password = pwd;
				username = usr;
			}
			
			} catch (NullPointerException nul) {
				System.out.println("DEBUG: email e password per l'invio delle mail nulle. Saranno impostate quelle di default");
				password = pwd;
				username = usr;
			}
			
			String host = "smtp.office365.com";
			String from = username;

			Properties props = System.getProperties();
			props.put("mail.smtp.host", host);
			props.put("mail.smtp.starttls.enable", "true");  
			props.put("mail.smtp.port", 587);

			Session session = Session.getInstance(props);

			Message msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(from));
			msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to, false));
			msg.setSubject(subject);
			msg.setText(body);
			// invia...
			// Se non disabilito l'antivirus, il codice va in eccezione e la mail non
			// viene inviata con successo.
			Transport.send(msg, username, password);	
			System.out.println("\nMail was sent successfully.");
		} catch (Exception e) {
			System.out.println(e);
			System.out.println("Errore nell'invio della mail");
		}
	}
}