package Admin.GUI;
import java.awt.EventQueue;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;



/**
 *  GUI
 * 
 * @author Amenta Stefano, Moroni Paolo
 *
 */
public class GuideDescription {

	private JFrame frmGuide;
	private static ArrayList<String> userSelectedList = new ArrayList<String>();
	private JEditorPane txtGg;

	/**
	 * Launch the application.
	 */
	public static void main() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GuideDescription window = new GuideDescription();
					window.frmGuide.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GuideDescription() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmGuide = new JFrame();
		frmGuide.setResizable(false);
		frmGuide.setTitle("Guide");
		frmGuide.setBounds(100, 100, 573, 414);
		frmGuide.setIconImage(Toolkit.getDefaultToolkit().getImage("media/f.png"));
		frmGuide.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				frmGuide.dispose();
				MainAdmin.main();
				
			}
		});
		frmGuide.getContentPane().setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 11, 547, 318);
		frmGuide.getContentPane().add(scrollPane);
		
		txtGg = new JEditorPane();
		txtGg.setContentType("text/html");
		txtGg.setText("<html><center><p>"
				+ "<span style='color:red; '><b>"+  Guide.helpObj.title + "</b></span></p>"
				+ "<p><span>&nbsp</span></p>" 
				+ "<p>" + Guide.helpObj.description 
				+ "</p></center></html>");
		scrollPane.setViewportView(txtGg);

		
		JButton btnNewButton = new JButton("Indietro");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frmGuide.dispose();
				Guide.main();
			}
		});
		btnNewButton.setBounds(10, 351, 547, 23);
		frmGuide.getContentPane().add(btnNewButton);
		
		

	}
}
