package Admin.GUI;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

import Client.ClientConnection;
import Common.DBType.Course;

import javax.swing.JList;
import javax.swing.DefaultListModel;
import javax.swing.JButton;



/**
 *  GUI
 * 
 * @author Amenta Stefano, Moroni Paolo
 *
 */
public class ManageCourseList 
{

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
				try 
				{
					ManageCourseList window = new ManageCourseList();
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
	public ManageCourseList() 
	{
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() 
	{
		frame = new JFrame();
		frame.setResizable(false);
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage("media/f.png"));
		frame.addWindowListener(new WindowAdapter() 
		{
			@Override
			public void windowClosing(WindowEvent arg0) 
			{
				
				frame.dispose();
				GetTeachersList.main();
			}
		});
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 11, 410, 206);
		frame.getContentPane().add(scrollPane);
		
		JList<String> LisfOfCourse = new JList<String>();
		scrollPane.setViewportView(LisfOfCourse);
		
		JButton btnAddcours = new JButton("aggiungi corso");
		btnAddcours.setBounds(10, 228, 410, 23);
		frame.getContentPane().add(btnAddcours);
		
		ArrayList<String> cmByDegree = new ArrayList<String>();
		ArrayList<Course> allCorsi = ClientConnection.getAllCorsiMateria();
		ArrayList<Course> docCorsi = ClientConnection.getCorsiByDocente(GetTeachersList.idDoc);				
		
		
		for(Course m : docCorsi) {
			allCorsi.remove(m);
		}
		

		for (Course CL : allCorsi) {
			cmByDegree.add(CL.Id + " " + CL.name);
		}
		DefaultListModel<String> model = new DefaultListModel<String>();
		


		for (String el : cmByDegree) {

				model.addElement(el);
		}
		LisfOfCourse.setModel(model);
		
		btnAddcours.addActionListener(new ActionListener() 
		{
			
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				String selected = (String) LisfOfCourse.getSelectedValue();
				String[] temp = selected.split(" ");
				long CourseId = Long.parseLong(temp[0]);
				ClientConnection.linkDocenteCorso(GetTeachersList.idDoc, CourseId);
				
				frame.dispose();
				GetTeachersList.main();
				
			}
		});
	}
}
