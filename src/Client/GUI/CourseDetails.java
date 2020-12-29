package Client.GUI;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JSeparator;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.SwingConstants;

import Client.ClientConnection;
import Client.LoggedUser;
import Common.DBType.Course;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;



/**
 *  GUI
 * 
 * @author Amenta Stefano, Moroni Paolo
 *
 */
public class CourseDetails {

	private static String name;
	private JFrame frame;
	private static String description;
	private static long id;

	/**
	 * Launch the application.
	 */
	public static void main(String[] Args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CourseDetails window = new CourseDetails();
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
	public CourseDetails() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setResizable(false);
		frame.setBounds(100, 100, 446, 300);
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage("media/f.png"));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JSeparator separator = new JSeparator();
		separator.setBounds(0, 219, 440, 2);
		frame.getContentPane().add(separator);

		JLabel coursetitle = new JLabel(name);
		coursetitle.setHorizontalAlignment(SwingConstants.CENTER);
		coursetitle.setFont(new Font("Tahoma", Font.PLAIN, 17));
		coursetitle.setBounds(132, 6, 165, 21);
		frame.getContentPane().add(coursetitle);

		JLabel coursedescription = new JLabel(description);
		coursedescription.setHorizontalAlignment(SwingConstants.CENTER);
		coursedescription.setFont(new Font("Tahoma", Font.PLAIN, 15));
		coursedescription.setBounds(10, 6, 420, 202);
		frame.getContentPane().add(coursedescription);

		JButton courseregistration = new JButton("registrazione");
		courseregistration.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				ClientConnection.subscribeCourse(LoggedUser.anagrafica.userID, id);
				JOptionPane.showMessageDialog(null, "Registrazione avvenuta con successo");
				
			}
		});
		courseregistration.setBounds(28, 232, 114, 31);
		frame.getContentPane().add(courseregistration);

		JButton btnback = new JButton("indietro");
		btnback.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e) 
			{

				frame.dispose();
				CourseNotRegistreted.main();
			}

		});
		btnback.setBounds(300, 235, 97, 25);
		frame.getContentPane().add(btnback);
		
	}

	public static void Setter(Course cm) {	
		name = cm.name;
		description = cm.description;
		id = cm.Id;
	}
}
