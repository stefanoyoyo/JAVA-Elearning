package Admin.GUI;

import java.awt.EventQueue;
import java.awt.Toolkit;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Vector;

import javax.swing.JFrame;
import Client.ClientConnection;
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
import javax.swing.JSeparator;
import java.awt.Font;


/**
 *  GUI
 * 
 * @author Amenta Stefano, Moroni Paolo
 *
 */
public class AdminMonitoring {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AdminMonitoring window = new AdminMonitoring();
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
	public AdminMonitoring() {
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
			}
		});
		frame.setBounds(100, 100, 398, 334);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel labeluseronline = new JLabel("Utenti online");
		labeluseronline.setHorizontalAlignment(SwingConstants.CENTER);
		labeluseronline.setBounds(230, 56, 152, 16);
		frame.getContentPane().add(labeluseronline);
		Vector<String> listayear = new Vector<String>();
		Calendar calendar = GregorianCalendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		for (; year > 1970; year--) {
			listayear.add(String.valueOf(year));
		}
		Vector<String> listamounth = new Vector<String>();
		int  mounth = 12;
		for (; mounth >0; mounth--) {
			listamounth.add(String.valueOf(mounth));
		}
		Vector<String> listaday = new Vector<String>();
		int day = 31;
		for (; day > 0; day--) {
			listaday.add(String.valueOf(day));
		} 
		
		
		
		
		
		JButton btntotaldownload = new JButton("Mostra download");
		btntotaldownload.setBounds(10, 220, 372, 25);
		frame.getContentPane().add(btntotaldownload);
		
		
		JLabel labelintervaltime = new JLabel("Numero accessi fascia temporale: data");
		labelintervaltime.setBounds(10, 159, 203, 16);
		frame.getContentPane().add(labelintervaltime);
		
		JLabel labelaveragetime = new JLabel("<html><center>Tempo medio connessione al corso<br></center></html>");
		labelaveragetime.setHorizontalAlignment(SwingConstants.CENTER);
		labelaveragetime.setBounds(204, 68, 178, 52);
		frame.getContentPane().add(labelaveragetime);
		
		JLabel lblDa = new JLabel("da:");
		lblDa.setBounds(233, 145, 21, 16);
		frame.getContentPane().add(lblDa);
		
		JLabel lblA = new JLabel("a:");
		lblA.setBounds(233, 177, 21, 16);
		frame.getContentPane().add(lblA);
		
		Vector<Long> idCorsi = new Vector();
		Vector<String> courseName = new Vector();
		int i = 0;
		ArrayList<Course> cm = ClientConnection.getAllCorsiMateria();
		System.out.println(cm);
		
		for (Course c : cm)
		{

			idCorsi.add(c.Id);
			courseName.add(c.name);
			i++;
		}
		
		
		JComboBox comoboxcourseresource = new JComboBox(courseName);
		comoboxcourseresource.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				double media = ClientConnection.getMediaCorso(idCorsi.get(comoboxcourseresource.getSelectedIndex()));
				String med = Double.toString(media);
				int index = 0;
				if (med.contains(".")) {
					index = med.indexOf(".");
				}
				if (index+3 < med.length()) {
				med = med.substring(0,index+3); 
				} else if (index+3 < med.length()) {
					med = med.substring(0,index+2); 
				}
				System.out.println("DEBUG: QueryMonitoring media con soli due valori dopo la virgola: " + med);
				labelaveragetime.setText("<html><center>Tempo medio connessione al corso :<br> " + Double.toString(media) + " secondi </center></html>");
			}
		});
		comoboxcourseresource.setBounds(10, 80, 184, 25);
		frame.getContentPane().add(comoboxcourseresource);
		
		JButton btnback = new JButton("indietro");
		btnback.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				frame.dispose();

			}
			
		});
		btnback.setBounds(10, 269, 372, 25);
		frame.getContentPane().add(btnback);
		
		
		SimpleDateFormat date = new SimpleDateFormat("dd - MM - yyyy");
		JSpinner spinnerto = new JSpinner(new SpinnerDateModel());
		spinnerto.setEditor(new JSpinner.DateEditor(spinnerto,date.toPattern()));
		spinnerto.setBounds(264, 175, 118, 20);
		frame.getContentPane().add(spinnerto);
		
		
		
		
		JSpinner spinnerfrom = new JSpinner(new SpinnerDateModel());
		spinnerfrom.setEditor(new JSpinner.DateEditor(spinnerfrom,date.toPattern()));
	    spinnerfrom.setBounds(264,143,118,20);
		frame.getContentPane().add(spinnerfrom);
		
		JComboBox comboBox = new JComboBox(courseName);
		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				long selectedIndex = comboBox.getSelectedIndex();
				System.out.println("DEBUG: TeacherMonitoring indice corso: " + selectedIndex);
				int useronline = ClientConnection.getNumberLoggedUserByCorso(idCorsi.get(comboBox.getSelectedIndex()));
				labeluseronline.setText("User online: " + Integer.toString(useronline));
			}
		});
		comboBox.setBounds(10, 52, 184, 25);
		frame.getContentPane().add(comboBox);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(0, 256, 392, 2);
		frame.getContentPane().add(separator);
		
		JLabel label = new JLabel("Monitoraggio livello utente");
		label.setFont(new Font("Tahoma", Font.BOLD, 17));
		label.setBounds(65, -1, 249, 25);
		frame.getContentPane().add(label);
		
		JLabel label_1 = new JLabel("Selezionare corso");
		label_1.setHorizontalAlignment(SwingConstants.CENTER);
		label_1.setBounds(10, 35, 178, 16);
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

