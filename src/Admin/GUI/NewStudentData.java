package Admin.GUI;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

import Common.DBType.Userdatas;
import Common.DBType.DegreeCourse;
import Common.DBType.Course;
import Common.DBType.Department;
import Common.DBType.Student;
import Common.Enumerators.RequestType;
import Common.Enumerators.CareerStatus;
import Common.Pacchetti.RequestContent;
import Common.Pacchetti.ReceiveContent;
import Server.Cache;
import Server.Database.DatabaseManager;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.awt.event.ActionEvent;
import Client.ClientConnection;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Calendar;
import java.util.Collections;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.Serializable;
import javax.swing.JSeparator;



/**
 *  GUI
 * 
 * @author Amenta Stefano, Moroni Paolo
 *
 */
public class NewStudentData {

	private JFrame frmStudente;
	static Date current;
	Userdatas anagrafica;
	ArrayList<DegreeCourse> listCL;
	JComboBox<Serializable> comboBox;
	
	public static Department sel;
	public static String dipartimentoSel;

	/**
	 * Launch the application.
	 */
	public static void main() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					NewStudentData window = new NewStudentData();
					window.frmStudente.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public void showList(ArrayList<DegreeCourse> list) {
		for(DegreeCourse c: list) {
			System.out.println("DEBUG: corso di laurea : " + c.name);
		}
	}

	/**
	 * Create the application.
	 */
	public NewStudentData() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void initialize() {
		frmStudente = new JFrame();
		frmStudente.setResizable(false);
		frmStudente.setTitle("studente");
		frmStudente.setBounds(100, 100, 455, 265);
		frmStudente.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frmStudente.getContentPane().setLayout(null);
		frmStudente.setIconImage(Toolkit.getDefaultToolkit().getImage("media/f.png"));

		anagrafica = GetUserData.getDati();
		
		JComboBox CB_StatoC = new JComboBox();
		CB_StatoC.setModel(new DefaultComboBoxModel(CareerStatus.values()));
		CB_StatoC.setBounds(169, 24, 181, 22);
		frmStudente.getContentPane().add(CB_StatoC);

		JLabel lblAnnoImmatricolazione = new JLabel("anno immatricolazione");
     	lblAnnoImmatricolazione.setVisible(false);
		lblAnnoImmatricolazione.setBounds(12, 140, 134, 16);
		frmStudente.getContentPane().add(lblAnnoImmatricolazione);

		JLabel lblStatoCariera = new JLabel("Stato carriera");
		lblStatoCariera.setBounds(12, 24, 129, 22);
		frmStudente.getContentPane().add(lblStatoCariera);

		JComboBox CB_corsiDipartimenti = new JComboBox();
//		CB_corsiDipartimenti.addMouseListener(new MouseAdapter() {
//			@Override
//			public void mouseClicked(MouseEvent arg0) {
//				dipartimentoSel = (String) CB_corsiDipartimenti.getSelectedItem();
//			}
//		});
		ArrayList<Department> dip = ClientConnection.getDipartimenti();
		for(Department d : dip) {
			CB_corsiDipartimenti.addItem(d.name);
		}
		System.out.println("DEBUG: nw_student getDipatimenti() visualizzo i dipartimenti ricevuti dal server"
				+ "\nDIPARTIMENTI: " + dip);
		CB_corsiDipartimenti.setBounds(169, 59, 181, 22);
		frmStudente.getContentPane().add(CB_corsiDipartimenti);

		JLabel lblCorsoDiLaurea = new JLabel("corso di laurea");
		lblCorsoDiLaurea.setVisible(false);
		lblCorsoDiLaurea.setBounds(12, 101, 134, 25);
		frmStudente.getContentPane().add(lblCorsoDiLaurea);

		JButton btn_errore = new JButton("Indietro");
		btn_errore.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GetUserData angfa = new GetUserData();
				frmStudente.dispose();
				angfa.ritorno();

			}
		});
		btn_errore.setBounds(23, 200, 97, 25);
		frmStudente.getContentPane().add(btn_errore);

		Vector<String> lista = new Vector<String>();
		Calendar calendar = GregorianCalendar.getInstance();
		int i = calendar.get(Calendar.YEAR);
		for (; i > 1970; i--) {
			lista.add(String.valueOf(i));
		}
		Collections.sort(lista, Collections.reverseOrder());// metodo per invertire la lista
		JComboBox CB_annoIm = new JComboBox(lista);
		CB_annoIm.setVisible(false);
		CB_annoIm.setBounds(169, 134, 89, 22);
		frmStudente.getContentPane().add(CB_annoIm);

		JButton btn_registra = new JButton("Conferma registrazione");
		btn_registra.setVisible(false);
		btn_registra.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{

				CareerStatus st_cariera = (CareerStatus) CB_StatoC.getSelectedItem();
				String anno_mat = (String) CB_annoIm.getSelectedItem();
				DegreeCourse Csel = null;
				for(DegreeCourse c:listCL ) {
					if(c.name.equals(comboBox.getSelectedItem())){
						Csel=c;
					}
				}
				Student newStud = new Student();
				newStud.name = anagrafica.name;
				newStud.surname = anagrafica.surname;
				newStud.Email = anagrafica.Email;
				newStud.Password = anagrafica.Password;
				newStud.cLaurea = Csel.Id;
				newStud.startYear = Integer.parseInt(anno_mat);
				newStud.status = st_cariera;
				ClientConnection.creaStudente(newStud);
				JOptionPane.showMessageDialog(null, "Student creato con successo");
				MainAdmin.main();
				frmStudente.dispose();

			}
		});
		btn_registra.setBounds(169, 200, 251, 25);
		frmStudente.getContentPane().add(btn_registra);
		
		
		
		JLabel lblNewLabel = new JLabel("dipartimento");
		lblNewLabel.setBounds(12, 63, 89, 18);
		frmStudente.getContentPane().add(lblNewLabel);
		
		JButton btnNewButton = new JButton("OK");
		
		
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				comboBox=new JComboBox();
				btnNewButton.setText("modifica");
				lblCorsoDiLaurea.setVisible(true);
				lblAnnoImmatricolazione.setVisible(true);
				CB_annoIm.setVisible(true);
				btn_registra.setVisible(true);
				//richiesta a rettani lista corsi di laurea determinato dipartimento
				//metterli da lista a vectro sotto creato
				sel = null;
				CB_corsiDipartimenti.getSelectedItem().toString();
				for(Department d:ClientConnection.getDipartimenti()) {
					if(d.name.equals(CB_corsiDipartimenti.getSelectedItem())) {
						sel = d;
					}
				}
				System.out.println();
				System.out.println("DEBUG: nw_student dipartimento selezionato: " 
				+ "\n NOME: " + sel.name
				+ "\n ID: " + sel.Id );
				listCL = ClientConnection.getCorsiLaureaByDip(sel.Id);
				for(DegreeCourse c : listCL) {
					comboBox.addItem(c.name);
				}
				System.out.println("DEBUG: nw_student modifica lista corsi: listCL==null? " + (listCL==null? true: false) + "\n lista corsi: ");
				showList(listCL);
				comboBox.setBounds(169, 103, 181, 20);
				frmStudente.getContentPane().add(comboBox);
				comboBox.removeAll();
//				comboBox.addItem(listCL);
//				CB_corsiDipartimenti.setVisible(false);
				comboBox.setVisible(true);
				
//				for(DegreeCourse item : listCL)
//				{
//					CB_corsiDipartimenti.addItem(item.Nome);
//					
//				}
			}
		});
		btnNewButton.setBounds(355, 59, 89, 23);
		frmStudente.getContentPane().add(btnNewButton);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(0, 187, 444, 2);
		frmStudente.getContentPane().add(separator);

		new ArrayList<DegreeCourse>();
		 
		 try {
			 
			 RequestContent rp = new RequestContent();
			 rp.type = RequestType.UPDATE_DEGREE_COURSE;
			 ReceiveContent rpp = ClientConnection.sendReceive(rp);
			 
			 
		} catch (Exception e1) {
			e1.printStackTrace();
		}

	}
}
