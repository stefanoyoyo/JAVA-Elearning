package Admin.GUI;
import java.awt.EventQueue;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Vector;
import java.awt.event.ActionEvent;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import Client.ClientConnection;
import Common.DBType.DegreeCourse;
import Common.DBType.Course;
import Common.DBType.Department;
import javax.swing.JComboBox;
import javax.swing.JScrollPane;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;



/**
 *  GUI
 * 
 * @author Amenta Stefano, Moroni Paolo
 *
 */
public class GetCourseList {

	private JFrame frame;
	private long identificativo;
	public static long identificativo2;
	public static long idDoc;
	private long idCorso;

	/**
	 * Launch the application.
	 */

	public  static void main() {

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GetCourseList window = new GetCourseList();
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
	public GetCourseList() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setResizable(false);
		GetTeachersList w_l = new GetTeachersList();
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage("media/f.png"));
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				
				frame.dispose();
				GetTeachersList.main();
			}
		});
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		JLabel MessageLabel = new JLabel("");
		MessageLabel.setBounds(10, 212, 424, 25);
		frame.getContentPane().add(MessageLabel);
		Vector<String> listadipartimenti = new Vector<String>();
		for (Department D : ClientConnection.getDipartimenti()) {
			listadipartimenti.addElement(D.name);
		}

		JButton AddButton = new JButton("aggiungi");
		AddButton.setVisible(false);
		JButton RemoveButton = new JButton("rimuovi");
		RemoveButton.setVisible(false);
		JButton BackButton = new JButton("indietro");
		BackButton.setVisible(false);
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setVisible(false);
		JList<String> list_c = new JList<String>();
		scrollPane.setViewportView(list_c);
		list_c.setVisible(false);
		JButton btn_add2 = new JButton("aggiungi");
		btn_add2.setVisible(false);
		JButton BackButton2 = new JButton("indietro");
		BackButton2.setVisible(false);
		JComboBox CB_laurebydip = new JComboBox();
		CB_laurebydip.setBounds(262, 147, 172, 20);
		frame.getContentPane().add(CB_laurebydip);
		JComboBox CB_dipartimento = new JComboBox(listadipartimenti);
		CB_dipartimento.setBounds(262, 37, 172, 20);
		frame.getContentPane().add(CB_dipartimento);
		scrollPane.setBounds(10, 11, 242, 190);
		frame.getContentPane().add(scrollPane);
		list_c.setBounds(10, 11, 261, 190);
		JButton BTN_confermalaurea = new JButton("conferma");
		BTN_confermalaurea.setVisible(false);
		BTN_confermalaurea.setBounds(262, 178, 172, 23);
		frame.getContentPane().add(BTN_confermalaurea);

		CB_dipartimento.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				DefaultComboBoxModel<String> listacorsilaurea = new DefaultComboBoxModel<String>();
				String claureaselected = (String) CB_dipartimento.getSelectedItem();
				for (Department D : ClientConnection.getDipartimenti()) {
					if (D.name.equals(claureaselected)) {
						identificativo = D.Id;
						break;
					}
				}
				for (DegreeCourse CL : ClientConnection.getCorsiLaureaByDip(identificativo)) {
					listacorsilaurea.addElement(CL.name);
				}
				
				CB_laurebydip.setModel(listacorsilaurea);
			}
		});
		
		CB_laurebydip.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
			
				BTN_confermalaurea.setVisible(true);
				
			}
		});
		
		BTN_confermalaurea.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String claureaselected = (String) CB_laurebydip.getSelectedItem();
				for (DegreeCourse CL : ClientConnection.getCorsiLaureaByDip(identificativo)) {
					if (CL.name.equals(claureaselected)) {
						identificativo2 = CL.Id;
						break;
					}
				}
				CB_dipartimento.setVisible(false);
				CB_laurebydip.setVisible(false);
				BTN_confermalaurea.setVisible(false);
				AddButton.setVisible(true);
				RemoveButton.setVisible(true);
				BackButton.setVisible(true);
				list_c.setVisible(true);
				btn_add2.setVisible(true);
				scrollPane.setVisible(true);
				
				
				ArrayList<String> cmByDegree = new ArrayList<String>();
				
				ArrayList<Course> allCorsi = ClientConnection.getAllCorsiMateria();
				ArrayList<Course> docCorsi = ClientConnection.getCorsiByDocente(GetTeachersList.idDoc);				
				
				
				for(int i  = 0 ; i<= allCorsi.size()-1; i++)
				{
					
					for(int ii = 0; ii<=docCorsi.size()-1; ii++)
					{
						if(allCorsi.get(i).Id == docCorsi.get(ii).Id)
						{
							allCorsi.remove(i);
						}
					}
				}

				for (Course CL : allCorsi) {
					cmByDegree.add(CL.Id + " " + CL.name);
				}
				DefaultListModel<String> model = new DefaultListModel<String>();
				


				for (String el : cmByDegree) {

						model.addElement(el);
				}
				list_c.setModel(model);
			}
		});
		
		btn_add2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String selected = list_c.getSelectedValue();
				String[] temp = selected.split(" ");
				
				for (Course CL : ClientConnection.getCorsiMateriaByLaurea(identificativo2)) {
					if (CL.Id == Long.parseLong(temp[0])) {
						idCorso = CL.Id;
					}
				}
				GetTeachersList w_l = new GetTeachersList();

				ClientConnection.linkDocenteCorso(GetTeachersList.idDoc, idCorso);
				GetTeachersList.main();
				w_l.set_result(selected);
				frame.dispose();

			}
		});
		btn_add2.setBounds(262, 68, 172, 25);
		frame.getContentPane().add(btn_add2);
		
		BackButton2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				
				GetTeachersList.main();
				frame.dispose();

			}
		});
		BackButton2.setBounds(10, 235, 424, 25);
		frame.getContentPane().add(BackButton2);
		
		JLabel label = new JLabel("Selezionare Dipartimento");
		label.setBounds(273, 13, 149, 16);
		frame.getContentPane().add(label);
		
		JLabel label_1 = new JLabel("<html><center><center>Selezionare Corso <br> di Laurea</center></html>");
		label_1.setBounds(298, 111, 122, 35);
		frame.getContentPane().add(label_1);
		

	}
}
