package Admin.GUI;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JList;

import Client.ClientConnection;
import Common.DBType.DegreeCourse;
import Common.DBType.Course;
import Common.DBType.Department;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Vector;
import java.awt.event.ActionEvent;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import java.awt.Color;
import java.awt.SystemColor;
import java.awt.Toolkit;

import javax.swing.JComboBox;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;



/**
 *  GUI
 * 
 * @author Amenta Stefano, Moroni Paolo
 *
 */
public class CourseList {

	private JFrame frmListaCorsi;
	public static String title = null;
	public static String description = null;
	public static long iddainviare = -1;
	boolean controllo = false;
	long iDip;
	long idL = -1;
	JComboBox selectCourseL = new JComboBox();
	ArrayList<Course> listaaa = new ArrayList<Course>();
	public static long idLaurea;

	/**
	 * Launch the application.
	 */

	public  static void main() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CourseList window = new CourseList();
					window.frmListaCorsi.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public CourseList() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmListaCorsi = new JFrame();
		frmListaCorsi.setTitle("Lista corsi");
		frmListaCorsi.setResizable(false);
		frmListaCorsi.setIconImage(Toolkit.getDefaultToolkit().getImage("media/f.png"));
		frmListaCorsi.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				MainAdmin.main();
				frmListaCorsi.dispose();
			}
		});

		frmListaCorsi.getContentPane().setBackground(SystemColor.inactiveCaptionBorder);
		frmListaCorsi.setBackground(Color.LIGHT_GRAY);
		frmListaCorsi.setBounds(100, 100, 571, 342);
		frmListaCorsi.getContentPane().setLayout(null);

		// PULSANTE INDIETRO

		JButton BackButton = new JButton("Indietro");
		BackButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				MainAdmin.main();
				frmListaCorsi.dispose();
			}
		});
		BackButton.setForeground(Color.BLACK);
		BackButton.setBackground(Color.LIGHT_GRAY);
		BackButton.setBorderPainted(false);
		BackButton.setBorder(null);
		BackButton.setIcon(null);

		BackButton.setBounds(12, 279, 141, 23);
		frmListaCorsi.getContentPane().add(BackButton);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(315, 14, 240, 232);
		frmListaCorsi.getContentPane().add(scrollPane);

		// DA INSERIRE QUERY GET CORSO MATERIA BY LAUREA

		JList cl = new JList();
		DefaultListModel<String> modelcl = new DefaultListModel<>();
		scrollPane.setViewportView(cl);

		// SCELTA DIPARTIMENTO

		Vector<String> dipList = new Vector<>();
		for (Department dip : ClientConnection.getDipartimenti()) {
			dipList.add(dip.name);
		}

		JComboBox selectDip = new JComboBox(dipList);
		selectDip.setBounds(10, 37, 293, 20);
		frmListaCorsi.getContentPane().add(selectDip);
		
		selectCourseL.setBounds(12, 162, 293, 20);
		frmListaCorsi.getContentPane().add(selectCourseL);
		selectCourseL.setVisible(false);

		// CONFERMA CORSO LAUREA

		JButton confirmCourseL = new JButton("Conferma");
		confirmCourseL.setVisible(false);
		confirmCourseL.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				modelcl.clear();
				String selectedCourseL = (String) selectCourseL.getSelectedItem();

				for (DegreeCourse cl2 : ClientConnection.getCorsiLaureaByDip(iDip)) {
					if (cl2.name == selectedCourseL) {
						idL = cl2.Id;
						break;
					}
				}
				
				for (Course cm : ClientConnection.getCorsiMateriaByLaurea(idL)) 
				{

					modelcl.addElement(cm.name);
				}
				
				
				cl.setModel(modelcl);
				
			}
		});
		confirmCourseL.setBounds(62, 193, 210, 23);
		frmListaCorsi.getContentPane().add(confirmCourseL);

		// CONFERMA DIPARTIMENTO

		JButton confirmDip = new JButton("Conferma");
		confirmDip.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// SCELTA CORSO

				DefaultComboBoxModel<String> model = new DefaultComboBoxModel();
				String selectedDip = (String) selectDip.getSelectedItem();
				for (Department d : ClientConnection.getDipartimenti()) {
					if (selectedDip == d.name) {
						iDip = d.Id;
					}

				}
				
				for (DegreeCourse course : ClientConnection.getCorsiLaureaByDip(iDip)) {
					model.addElement(course.name);
				}

				selectCourseL.setModel(model);
				selectCourseL.setVisible(true);
				confirmCourseL.setVisible(true);
			}
		});
		
		JLabel label = new JLabel("Selezione Corso di Laurea");
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setFont(new Font("Tahoma", Font.PLAIN, 14));
		label.setBounds(12, 135, 258, 16);
		frmListaCorsi.getContentPane().add(label);
		confirmDip.setBounds(83, 68, 159, 23);
		frmListaCorsi.getContentPane().add(confirmDip);
		
		JButton btnNewButton = new JButton("Visualizza corso");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				String selectedc = (String) cl.getSelectedValue();
				for (Course cm : ClientConnection.getCorsiMateriaByLaurea(idL)) 
				{

					if ( selectedc == cm.name) {
						title = cm.name;
						description = cm.description;
						iddainviare = cm.Id;
						idLaurea = cm.Id;
					}
				}
				
				CourseManagment.Main();
				frmListaCorsi.dispose();
				
			}
		});
		
		JLabel label_1 = new JLabel("Selezione dipartimento");
		label_1.setHorizontalAlignment(SwingConstants.CENTER);
		label_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		label_1.setBounds(14, 11, 289, 20);
		frmListaCorsi.getContentPane().add(label_1);
		btnNewButton.setBounds(414, 279, 141, 23);
		frmListaCorsi.getContentPane().add(btnNewButton);

	}
}
