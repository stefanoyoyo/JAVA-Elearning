package Admin.GUI;
import java.awt.EventQueue;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;

import Client.ClientConnection;
import Common.DBType.Teacher;
import javax.swing.JScrollPane;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;



/**
 *  GUI
 * 
 * @author Amenta Stefano, Moroni Paolo
 *
 */
public class GetTeachersList 
{

	private JFrame frmSelezioneDocente;
	ArrayList<Teacher> listprof;
	static long idDoc;
	Teacher docentone;

	/**
	 * Launch the application.
	 */
	public static  void main() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GetTeachersList window = new GetTeachersList();
					window.frmSelezioneDocente.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GetTeachersList() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() 
	{
		frmSelezioneDocente = new JFrame();
		frmSelezioneDocente.setResizable(false);
		frmSelezioneDocente.setIconImage(Toolkit.getDefaultToolkit().getImage("media/f.png"));
		frmSelezioneDocente.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) 
			{
				frmSelezioneDocente.dispose();
				MainAdmin.main();
			}
		});
		frmSelezioneDocente.setTitle("Selezione docente");
		frmSelezioneDocente.setBounds(100, 100, 436, 300);
		frmSelezioneDocente.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frmSelezioneDocente.getContentPane().setLayout(null);
		
		
			listprof = new ArrayList<Teacher>();
			listprof = ClientConnection.getAllDocenti();
			DefaultListModel<String> model = new DefaultListModel<>();
		
		
			for(Teacher d : listprof)
			{
				String nome_cognome = "<html><p style=\"color:red; display:inline\">MATRICOLA: <span style='color:black'>" + d.userID + "</span></p>"
						+ "   <p style=\"color:red;\">NOME: <span style='color:black'>" + d.name + "</span> </p>"
						+ "   <p style=\"color:red;\">COGNOME: <span style='color:black'>" + d.surname + "</span> </p>"
						+ "   <p>&nbsp</p>"
						+ " </html>";
				nome_cognome = nome_cognome.concat("NOME: " + d.surname + " COGNOME: " + d.name);
				model.addElement(nome_cognome);
			}
			
			JScrollPane scrollPane = new JScrollPane();
			scrollPane.setBounds(10, 11, 410, 219);
			frmSelezioneDocente.getContentPane().add(scrollPane);
			JList<String> jl_Professors = new JList<String>(model);
			jl_Professors.setToolTipText("Lista docenti");
			scrollPane.setViewportView(jl_Professors);
//			jl_Professors.setPreferredSize(new Dimension(800, 200));
		
			JButton btn_addcourse = new JButton("aggiungi corso");
			btn_addcourse.setVerticalAlignment(SwingConstants.TOP);
			btn_addcourse.addActionListener(new ActionListener() 
			{
				public void actionPerformed(ActionEvent e) 
				{
					String s = jl_Professors.getSelectedValue();
					Pattern intsOnly = Pattern.compile("\\d+");
					Matcher makeMatch = intsOnly.matcher(s);
					makeMatch.find();
					String inputStr = makeMatch.group();
					long inputLong = Long.parseLong(inputStr);
					System.out.println(inputLong);
					
					try {
					for(Teacher d : listprof)
					{
						if (d.userID == inputLong)
						{
							idDoc = d.userID;
						}
					}
					} catch (Exception ex) {
						ex.printStackTrace();
					}
					//GetCourseList.main();
					
					ManageCourseList.main();
					frmSelezioneDocente.dispose();
					
					
				}
			
			});
			btn_addcourse.setBounds(10, 241, 162, 23);
			frmSelezioneDocente.getContentPane().add(btn_addcourse);
			
			JButton btn_returntomain = new JButton("indietro");
			btn_returntomain.addActionListener(new ActionListener() 
			{
				public void actionPerformed(ActionEvent e) 
				{
					MainAdmin.main();
					frmSelezioneDocente.dispose();
				}
			});
			btn_returntomain.setBounds(258, 241, 162, 23);
			frmSelezioneDocente.getContentPane().add(btn_returntomain);
			
			
		}
		
	public void set_result(Object course)
	{
	}
}
