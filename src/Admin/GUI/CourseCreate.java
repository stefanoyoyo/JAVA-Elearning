package Admin.GUI;
import java.awt.EventQueue;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Calendar;
import java.awt.event.ActionEvent;
import Client.ClientConnection;
import Common.DBType.DegreeCourse;
import Common.DBType.Course;
import Common.DBType.Department;

import javax.swing.JComboBox;
import javax.swing.SwingConstants;
import java.awt.Font;



/**
 *  GUI
 * 
 * @author Amenta Stefano, Moroni Paolo
 *
 */
public class CourseCreate {

	private JFrame frame;
	private JTextField textField;
	JComboBox CB_claurea;
	long selezionato;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CourseCreate window = new CourseCreate();
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
	public CourseCreate() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setResizable(false);
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage("media/f.png"));
		
		frame.addWindowListener(new WindowAdapter() 
		{
			@Override
			public void windowClosing(WindowEvent arg0) 
			{
				MainAdmin.main();
				frame.dispose();
			}
		});
		
		textField = new JTextField();
		textField.setBounds(106, 31, 328, 20);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("Titolo");
		lblNewLabel.setBounds(10, 34, 86, 14);
		frame.getContentPane().add(lblNewLabel);
		
		JLabel lblInserisciLeSpecifiche = new JLabel("<html><center>inserisci le<br>specifiche</center></html>");
		lblInserisciLeSpecifiche.setHorizontalAlignment(SwingConstants.CENTER);
		lblInserisciLeSpecifiche.setBounds(10, 141, 86, 67);
		frame.getContentPane().add(lblInserisciLeSpecifiche);
		
		JTextArea textArea = new JTextArea();
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setBounds(106, 120, 328, 106);
		frame.getContentPane().add(textArea);
		
		JButton btnNewButton = new JButton("indietro");
		btnNewButton.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0)
			{
				MainAdmin.main();
				frame.dispose();
			}
		});
		btnNewButton.setBounds(322, 237, 89, 23);
		frame.getContentPane().add(btnNewButton);
		
		JButton btnSalva = new JButton("Ok");
		btnSalva.setBounds(23, 237, 89, 23);
		btnSalva.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Course newCorso = new Course();
				newCorso.activationYear = Calendar.getInstance().get(Calendar.YEAR);
				newCorso.name = textField.getText();
				newCorso.description = textArea.getText();
				String text = (String)CB_claurea.getSelectedItem();
				DegreeCourse c = null;
				for(DegreeCourse c1 : ClientConnection.getCorsiLaureaByDip(selezionato)) {
					if(c1.name == text) {
						c = c1;
					}
				}
				try {
				newCorso.degreeCourse = c.Id;
				ClientConnection.createCorsoMateria(newCorso);
				JOptionPane.showMessageDialog(null, "Corso creato con successo");
				MainAdmin.main();
				frame.dispose();
				} catch (Exception ex) {System.out.println(ex.getLocalizedMessage());}
				
			}
		});
		frame.getContentPane().add(btnSalva);
		
		ArrayList<DegreeCourse> ALCLaurea = ClientConnection.getCorsiLaureaByDip(selezionato);
		CB_claurea = new JComboBox();
		CB_claurea.setVisible(false);
		CB_claurea.setBounds(106, 93, 328, 20);
		frame.getContentPane().add(CB_claurea);
		
		ArrayList<Department> dip1 = ClientConnection.getDipartimenti();
		JComboBox CB_dipartimento = new JComboBox();
		for(Department d : dip1)
		{
					CB_dipartimento.addItem(d.name);
		}
		CB_dipartimento.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				DefaultComboBoxModel<String> cLLaurea = new DefaultComboBoxModel();
				String obj_dipartimento = (String)CB_dipartimento.getSelectedItem();
				selezionato = 0;
				for(Department d : dip1) 
				{
					if(d.name.equalsIgnoreCase(obj_dipartimento)) 
					{
						selezionato = d.Id;
						break;
					}
				}
				ArrayList<DegreeCourse> ALCLaurea = ClientConnection.getCorsiLaureaByDip(selezionato);
				for(DegreeCourse cl : ALCLaurea)
				{
					cLLaurea.addElement(cl.name);
				}
				CB_claurea.setModel(cLLaurea);
				
				CB_claurea.setVisible(true);
				
				
			}
		});
		CB_dipartimento.setBounds(106, 62, 328, 20);
		frame.getContentPane().add(CB_dipartimento);
		
		JLabel LBL_dipartimento = new JLabel("dipartimento");
		LBL_dipartimento.setBounds(10, 59, 86, 14);
		frame.getContentPane().add(LBL_dipartimento);
		
		JLabel LBL_claurea = new JLabel("corso di laurea");
		LBL_claurea.setBounds(10, 89, 86, 20);
		frame.getContentPane().add(LBL_claurea);
		
		JLabel label = new JLabel("Creazione nuovo corso");
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setFont(new Font("Tahoma", Font.BOLD, 15));
		label.setBounds(0, 0, 444, 28);
		frame.getContentPane().add(label);
		
//		frame.dispose();
//		MainAdmin.main();
	}
}
