package Client.GUI;

import java.awt.EventQueue;
import java.awt.Toolkit;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Vector;

import javax.swing.JFrame;
import Client.ClientConnection;
import Client.LoggedUser;
import Common.DBType.Course;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JComboBox;
import javax.swing.SwingConstants;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import java.util.Date;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.Font;



/**
 *  GUI
 * 
 * @author Amenta Stefano, Moroni Paolo
 *
 */
public class TeacherMonitoring {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TeacherMonitoring window = new TeacherMonitoring();
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
	public TeacherMonitoring() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize()
	{
		frame = new JFrame();
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage("media/f.png"));
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				frame.dispose();
				ClientCourseList.main();
			}
		});
		frame.setBounds(100, 100, 399, 364);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel labeluseronline = new JLabel("Utenti online");
		labeluseronline.setHorizontalAlignment(SwingConstants.CENTER);
		labeluseronline.setBounds(10, 82, 152, 16);
		frame.getContentPane().add(labeluseronline);
		
		//creazione vectro da inserire in combobox
		Vector<String> listayear = new Vector<String>();
		Calendar calendar = GregorianCalendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		
		for (; year > 1970; year--) {
			listayear.add(String.valueOf(year));
		}
		Vector<String> listamounth = new Vector<String>();
		GregorianCalendar.getInstance();
		int  mounth = 12;
		
		for (; mounth >0; mounth--) {
			listamounth.add(String.valueOf(mounth));
		}
		Vector<String> listaday = new Vector<String>();
		GregorianCalendar.getInstance();
		int day = 31;
		
		for (; day > 0; day--) {
			listaday.add(String.valueOf(day));
		} 
		
		
		
		
		
		JButton btntotaldownload = new JButton("Mostra download");
		//buttontimeinterval.setAction(  new Action());
		btntotaldownload.setBounds(10, 181, 363, 25);
		frame.getContentPane().add(btntotaldownload);
		
		
		JLabel labelintervaltime = new JLabel("<html><center>Numero di download nella fascia temporale specificata");
		labelintervaltime.setBounds(10, 217, 203, 48);
		frame.getContentPane().add(labelintervaltime);
		
		JLabel labelaveragetime = new JLabel("<html><center>Tempo medio connessione al corso<br></center></html>");
		labelaveragetime.setHorizontalAlignment(SwingConstants.CENTER);
		labelaveragetime.setBounds(0, 109, 178, 48);
		frame.getContentPane().add(labelaveragetime);
		
		JLabel lblDa = new JLabel("da:");
		lblDa.setBounds(224, 217, 21, 16);
		frame.getContentPane().add(lblDa);
		
		JLabel lblA = new JLabel("a:");
		lblA.setBounds(224, 249, 21, 16);
		frame.getContentPane().add(lblA);
		
		Vector<Long> idCorsi = new Vector<Long>();
		Vector<String> courseName = new Vector<String>();
		ArrayList<Course> cm = ClientConnection.getCorsiByDocente(LoggedUser.anagrafica.userID);
		System.out.println(cm);
		
		for (Course c : cm)
		{

			idCorsi.add(c.Id);
			courseName.add(c.name);
		}
		
		
		JComboBox<String> comoboxcourseresource = new JComboBox<String>(courseName);
		comoboxcourseresource.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				double media = ClientConnection.getMediaCorso(idCorsi.get(comoboxcourseresource.getSelectedIndex()));
				labelaveragetime.setText("<html><center>Tempo medio connessione al corso :<br> " + Double.toString(media) + "</center></html>");
			}
		});
		comoboxcourseresource.setBounds(189, 132, 184, 25);
		frame.getContentPane().add(comoboxcourseresource);
		
		JButton btnback = new JButton("indietro");
		btnback.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				frame.dispose();
				ClientCourseList.main();
			}
			
		});
		btnback.setBounds(10, 289, 362, 25);
		frame.getContentPane().add(btnback);
		
		
		Calendar.getInstance().get(Calendar.YEAR);
	
		
		SimpleDateFormat date = new SimpleDateFormat("dd - MM - yyyy");
		JSpinner spinnerto = new JSpinner(new SpinnerDateModel());
		spinnerto.setEditor(new JSpinner.DateEditor(spinnerto,date.toPattern()));
		spinnerto.setBounds(255, 247, 118, 20);
		frame.getContentPane().add(spinnerto);
		
		
		
		JSpinner spinnerfrom = new JSpinner(new SpinnerDateModel());
		spinnerfrom.setEditor(new JSpinner.DateEditor(spinnerfrom,date.toPattern()));
	    spinnerfrom.setBounds(255,215,118,20);
		frame.getContentPane().add(spinnerfrom);
		
		JComboBox<String> comboBox = new JComboBox<String>(courseName);
		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				int useronline = ClientConnection.getNumberLoggedUserByCorso(idCorsi.get(comboBox.getSelectedIndex()));
				labeluseronline.setText("User online: " + Integer.toString(useronline));
			}
		});
		comboBox.setBounds(189, 78, 184, 25);
		frame.getContentPane().add(comboBox);
		
		JLabel label = new JLabel("Selezione del corso");
		label.setBounds(189, 51, 118, 16);
		frame.getContentPane().add(label);
		
		JLabel label_1 = new JLabel("Monitoraggio");
		label_1.setHorizontalAlignment(SwingConstants.CENTER);
		label_1.setFont(new Font("Tahoma", Font.BOLD, 17));
		label_1.setBounds(11, 4, 362, 36);
		frame.getContentPane().add(label_1);
		
		btntotaldownload.addActionListener(new ActionListener() 
		{
	public void actionPerformed(ActionEvent e) 
	{
 		
		    Date datato = (Date)spinnerto.getValue();
		    Date datafrom = (Date)spinnerfrom.getValue();
		    
		    if(datato.compareTo(datafrom)>=0)
		    {
		    	int intervalTemp = ClientConnection.contUtentiDownload(datafrom, datato);
		    	labelintervaltime.setText("Numero accessi fascia temporale " + Integer.toString(intervalTemp));
		    }
		    else
		    {
		    	JOptionPane.showMessageDialog(null, "errore data fine minore della data di inizio");
		    }
		    
		    
		    	
	}
	});
		
		
		
	
	}	
}

