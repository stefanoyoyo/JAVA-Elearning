package Admin.GUI;
import java.awt.EventQueue;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JSpinner;

import Client.ClientConnection;
import Common.DBType.Course;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;
import java.awt.event.ActionEvent;
import javax.swing.SpinnerDateModel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JComboBox;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.Font;
import javax.swing.SwingConstants;



/**
 *  GUI
 * 
 * @author Amenta Stefano, Moroni Paolo
 *
 */
public class Monitoryng {

	private int lu;
	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Monitoryng window = new Monitoryng();
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
	public Monitoryng() {
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
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				frame.dispose();
				MainAdmin.main();
			}
		});
		frame.setBounds(100, 100, 411, 300);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		

		SimpleDateFormat date = new SimpleDateFormat("dd - MM - yyyy");
		JSpinner spinnerfrom = new JSpinner(new SpinnerDateModel());
		spinnerfrom.setEditor(new JSpinner.DateEditor(spinnerfrom,date.toPattern()));
	    spinnerfrom.setBounds(215,96,180,23);
		frame.getContentPane().add(spinnerfrom);
		
		JSpinner spinnerto = new JSpinner(new SpinnerDateModel());
		spinnerto.setEditor(new JSpinner.DateEditor(spinnerto,date.toPattern()));
	    spinnerto.setBounds(215,130,180,23);
		frame.getContentPane().add(spinnerto);
		
		JLabel lblDa = new JLabel("Da :");
		lblDa.setBounds(184, 100, 46, 14);
		frame.getContentPane().add(lblDa);
		
		JLabel lblA = new JLabel("A :");
		lblA.setBounds(184, 134, 46, 14);
		frame.getContentPane().add(lblA);
		
		JLabel lblAccessiPeriodo = new JLabel("Accessi nel periodo");
		lblAccessiPeriodo.setBounds(10, 115, 164, 14);
		frame.getContentPane().add(lblAccessiPeriodo);
		
		JLabel lblNumeroUtenti = new JLabel("Numero utenti");
		lblNumeroUtenti.setBounds(10, 37, 102, 14);
		frame.getContentPane().add(lblNumeroUtenti);
		
		JLabel lblTempoMedio = new JLabel("<html><center>Tempo medio connessione: </center></html>");
		lblTempoMedio.setBounds(10, 190, 201, 45);
		frame.getContentPane().add(lblTempoMedio);
		
		JLabel lblNumeroDownloadComplessivo = new JLabel("N. download totale");
		lblNumeroDownloadComplessivo.setBounds(10, 246, 173, 14);
		frame.getContentPane().add(lblNumeroDownloadComplessivo);
		
		JButton btnNewButton = new JButton("Mostra utenti connessi");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				lu = ClientConnection.getNumberLoggedUser();
				lblNumeroUtenti.setText("Numero utenti:  " + lu);
			}
		});
		btnNewButton.setBounds(203, 33, 192, 23);
		frame.getContentPane().add(btnNewButton);
		
		Vector<Long> idCorsi = new Vector<Long>();
		Vector<String> courseName = new Vector<String>();
		ArrayList<Course> cm = ClientConnection.getAllCorsiMateria();
		
		for (Course c : cm)
		{

			idCorsi.add(c.Id);
			courseName.add(c.name);
		}
		
		JComboBox<String> courseList1 = new JComboBox<String>(courseName);
		courseList1.setBounds(10, 65, 385, 20);
		frame.getContentPane().add(courseList1);
		
		JButton btnNewButton_1 = new JButton("Mostra accessi nel periodo");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				Date datato = (Date)spinnerto.getValue();
			    Date datafrom = (Date)spinnerfrom.getValue();
			    
			    if(datato.compareTo(datafrom)>=0)
			    {
			    	int intervalTemp = ClientConnection.getNumberAccessByPeriod(idCorsi.get(courseList1.getSelectedIndex()), datafrom, datato);
			    	lblAccessiPeriodo.setText("Numero accessi: " + Integer.toString(intervalTemp));
			    }
			    else
			    {
			    	JOptionPane.showMessageDialog(null, "errore data fine minore della data di inizio");
			    }
				
			}
		});
		btnNewButton_1.setBounds(10, 165, 385, 23);
		frame.getContentPane().add(btnNewButton_1);
		
		JComboBox<String> connectionTime = new JComboBox<String>(courseName);
		connectionTime.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				double media = ClientConnection.getMediaCorso(idCorsi.get(courseList1.getSelectedIndex()));
				lblTempoMedio.setText("<html><center>Tempo medio connessione:  " + media + "</center></html>" );
				
			}
		});

		connectionTime.setBounds(221, 199, 174, 20);
		frame.getContentPane().add(connectionTime);
		
		JComboBox<String> courseList3 = new JComboBox<String>(courseName);
		courseList3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int media = ClientConnection.documentContByCorso(idCorsi.get(courseList1.getSelectedIndex()));
				lblNumeroDownloadComplessivo.setText("Download complessivi : " + media);
				
			}
		});
		courseList3.setBounds(221, 240, 174, 20);
		frame.getContentPane().add(courseList3);
		
		JLabel label = new JLabel("Monitoraggio Admin");
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setFont(new Font("Tahoma", Font.BOLD, 17));
		label.setBounds(10, 0, 357, 23);
		frame.getContentPane().add(label);
		
	}
	
}
