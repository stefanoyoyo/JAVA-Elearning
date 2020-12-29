package Admin.GUI;
import java.awt.EventQueue;

import javax.swing.JFrame;

import Common.DBType.Userdatas;
import Common.DBType.Department;
import Common.DBType.Teacher;

import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JOptionPane;
import Client.ClientConnection;

import java.awt.Toolkit;
import javax.swing.JSeparator;



/**
 *  GUI
 * 
 * @author Amenta Stefano, Moroni Paolo
 *
 */
public class NewTeacherData {

	private JFrame frmDocente;
	private Userdatas anagrafica;
	/**
	 * Launch the application.
	 */
	public static void main() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					NewTeacherData window = new NewTeacherData();
					window.frmDocente.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public NewTeacherData() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmDocente = new JFrame();
		frmDocente.setResizable(false);
		frmDocente.setTitle("docente");
		frmDocente.setBounds(100, 100, 328, 259);
		frmDocente.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frmDocente.getContentPane().setLayout(null);
		frmDocente.setIconImage(Toolkit.getDefaultToolkit().getImage("media/f.png"));
		
		anagrafica = GetUserData.getDati();

		JLabel LB_dipartimento = new JLabel("Department");
		LB_dipartimento.setBounds(123, 35, 179, 16);
		frmDocente.getContentPane().add(LB_dipartimento);

		JButton button_1 = new JButton("Indietro");
		button_1.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				GetUserData angfa = new GetUserData();
				frmDocente.dispose();
				angfa.ritorno();
			}
			
		});
		button_1.setBounds(12, 191, 97, 25);
		frmDocente.getContentPane().add(button_1);

		ArrayList<Department> dip1 = ClientConnection.getDipartimenti();
		JComboBox<String> comboBoxdip = new JComboBox<String>();
		
		for(Department d : dip1) {
			comboBoxdip.addItem(d.name);
		}
		comboBoxdip.setBounds(12, 62, 290, 22);
		frmDocente.getContentPane().add(comboBoxdip);

		JButton button = new JButton("Conferma registrazione");
		button.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e) 
			{	
//				invio dati a retta
				Department selected=null;
				String rif = (String)comboBoxdip.getSelectedItem();
				for(Department d : dip1) {
					if(d.name.equalsIgnoreCase(rif)) {
						selected = d;
						break;
					}
				}
				Teacher newDoc = new Teacher();
				newDoc.name = anagrafica.name;
				newDoc.surname = anagrafica.surname;
				newDoc.Email = anagrafica.Email;
				newDoc.Password = anagrafica.Password;
				newDoc.department = selected.Id;
				ClientConnection.creaDocente(newDoc);
				JOptionPane.showMessageDialog(null, "Teacher creato con successo");
				MainAdmin.main();
				frmDocente.dispose();
			}
		});
		button.setBounds(123, 191, 179, 25);
		frmDocente.getContentPane().add(button);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(0, 178, 322, 2);
		frmDocente.getContentPane().add(separator);
		
		

	}

	public void ricevi(ArrayList<?> courses, long [] idcmByDegree ) 
	{
		JList<?> list = new JList<Object>(courses.toArray());
		list.setBounds(145, 81, 138, 72);
		frmDocente.getContentPane().add(list);
		frmDocente.setVisible(true);
		
		
		
	}
}
