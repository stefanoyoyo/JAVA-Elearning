package Client.GUI;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JList;

import Client.ClientConnection;
import Client.LoggedUser;
import Common.DBType.Course;

import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Toolkit;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;



/**
 *  GUI
 * 
 * @author Amenta Stefano, Moroni Paolo
 *
 */
public class CourseNotRegistreted {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main() 
	{
		EventQueue.invokeLater(new Runnable()
		{
			public void run() 
			{
				try {
					CourseNotRegistreted window = new CourseNotRegistreted();
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
	public CourseNotRegistreted() 
	{
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		frame = new JFrame();
		frame.setResizable(false);
		frame.setBounds(100, 100, 372, 404);
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage("media/f.png"));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 33, 330, 273);
		frame.getContentPane().add(scrollPane);
		JList<String> list = new JList<String>();
		scrollPane.setViewportView(list);
		
		
		ArrayList<Course> Lcorsinotreg = ClientConnection.getAllCorsiMateria();
		ArrayList<Course> Lcorsiregistered = ClientConnection.getCorsiStud(LoggedUser.anagrafica.getUserID());
		
		for(Course m : Lcorsiregistered) {
			Lcorsinotreg.remove(m);
		}
		
		DefaultListModel<String> model = new DefaultListModel<String>();
		for(Course cm : Lcorsinotreg)
		{
			model.addElement(cm.name);
		}
		
		list.setModel(model);
		JLabel lblListaCorsi = new JLabel("Lista dei corsi");
		lblListaCorsi.setFont(new Font("Times New Roman", Font.PLAIN, 17));
		lblListaCorsi.setBounds(115, 11, 158, 16);
		frame.getContentPane().add(lblListaCorsi);
		
		JButton btnInformation = new JButton("Informazioni sul corso");
		btnInformation.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				int list_index = list.getSelectedIndex();
				CourseDetails.Setter(Lcorsinotreg.get(list_index));
				CourseDetails.main(null);
				frame.dispose();
				
			}
		});
		btnInformation.setBounds(12, 319, 198, 25);
		frame.getContentPane().add(btnInformation);
		
		JButton btnback = new JButton("indietro");
		btnback.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e) 
			{
				ClientCourseList.main();
				frame.dispose();
			}
		});
		btnback.setBounds(220, 319, 122, 25);
		frame.getContentPane().add(btnback);
	}
}
