package Admin.GUI;
import java.awt.EventQueue;
import java.awt.Image;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Vector;
import java.awt.Color;
import java.awt.SystemColor;
import java.awt.Toolkit;
import javax.swing.JTree;
import Client.ClientConnection;
import Client.CustomJTreeNode;
import Client.LoggedUser;
import Client.GUI.CourseFile;
import Common.DBType.Userdatas;
import Common.DBType.Folder;
import Common.DBType.Course;
import Common.DBType.Document;
import Common.DBType.CourseFileSystem;
import Common.Enumerators.FolderVisibility;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.JScrollPane;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;



/**
 *  GUI
 * 
 * @author Amenta Stefano, Moroni Paolo
 *
 */
public class CourseManagment {

	private JFrame frmAsd;
	private JTextField NameCourseField;
	private static String titleFolder = null;
	private static String nameSelected = null;
	private long indicecorso;
	File road = null;
	private long idDoc;
	private String path;
	private long rif_Sezione;
	private JDialog diag = new JDialog();
	private JDialog diag2 = new JDialog();
	CustomJTreeNode controlNode;
	private DefaultMutableTreeNode nameNode;
	private static CourseFileSystem fileSystem;
	private static String courseDescr;
	private static String estensione = null;
	private static String fileName = null;
	


	/**
	 * Launch the application.
	 */

	public void SottoNodiFs(DefaultMutableTreeNode father, ArrayList<Folder> subFolders) {
		CustomJTreeNode node = null;
		for (Folder folder : subFolders) {
			node = new CustomJTreeNode(folder.name, folder.idSez, true);
			SottoNodiFs(node, folder.sonFolders);
			father.add(node);
		}
	}

	public void ContenutiFs(DefaultMutableTreeNode father, ArrayList<Document> contents) {
		try {
			CustomJTreeNode node = null;
			for (Document folder : contents) {
				node = new CustomJTreeNode(folder.name + folder.docType, folder.idDoc, false);
				father.add(node);
			}
		} catch (Exception e) {
			//System.out.println(e.getMessage());
		}
	}

	public void creaNodiFileSystem(CourseFileSystem fs, JTree tree, CustomJTreeNode radice) {
		try {
			CustomJTreeNode node = null;

			if (fs.folders != null) {
				for (Folder folder : fs.folders) {
					node = new CustomJTreeNode(folder.name, folder.idSez, true);
//					SottoNodiFs(node, folder.SottoCartelle);
					ContenutiFs(node, folder.document);
					radice.add(node);
				}
			}

		} catch (Exception e) {
			System.out.println("Errore nella generazione del tree");
		}
	}

	public static void Main() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CourseManagment window = new CourseManagment();
					window.frmAsd.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	

	/**
	 * Create the application.
	 */
	public CourseManagment() {
		initialize();

	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		String title = CourseList.title;
		indicecorso = CourseList.iddainviare;
		frmAsd = new JFrame();
		frmAsd.setResizable(false);
		frmAsd.setTitle("Admin - " + title);
		frmAsd.getContentPane().setBackground(SystemColor.inactiveCaption);
		frmAsd.setIconImage(Toolkit.getDefaultToolkit().getImage("media/f.png"));
		frmAsd.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				CourseList.main();
				frmAsd.dispose();
			}
		});
		frmAsd.setBounds(100, 100, 493, 418);
		frmAsd.getContentPane().setLayout(null);

		
		
		NameCourseField = new JTextField(title);
		NameCourseField.setEditable(false);
		NameCourseField.setBounds(27, 13, 384, 25);
		frmAsd.getContentPane().add(NameCourseField);
		NameCourseField.setColumns(10);

		JTextArea textArea = new JTextArea();
		String descr = ClientConnection.courseDescription(CourseList.idLaurea);
		textArea.append(descr); 
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setEditable(false);
		textArea.setBounds(27, 66, 197, 233);
		frmAsd.getContentPane().add(textArea);

		JButton btn_save = new JButton("salva");
		btn_save.setBounds(27, 310, 98, 25);
		btn_save.setVisible(false);
		frmAsd.getContentPane().add(btn_save);
		Icon ic = new ImageIcon("Media/chiave.png");
		JButton btnNewButton = new JButton(ic);

		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				NameCourseField.setEditable(true);
				textArea.setEditable(true);
				btn_save.setVisible(true);

			}
		});
		btnNewButton.setBounds(428, 13, 34, 25);

		frmAsd.getContentPane().add(btnNewButton);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(248, 69, 214, 204);
		frmAsd.getContentPane().add(scrollPane);

		CustomJTreeNode radice = new CustomJTreeNode(title, -1, true);
		JTree tree = new JTree(radice);
		tree.setRootVisible(true);
		indicecorso = CourseList.iddainviare;
		CourseFileSystem fs = ClientConnection.getFileSystem(indicecorso);
		fileSystem = fs;
		creaNodiFileSystem(fs, tree, radice);
		tree.setForeground(SystemColor.inactiveCaption);
		tree.setBackground(SystemColor.inactiveCaption);
		tree.setVisible(true);
		scrollPane.setViewportView(tree);

		JProgressBar progressBar = new JProgressBar();
		progressBar.setForeground(Color.RED);
		progressBar.setVisible(false);
		progressBar.setBounds(27, 355, 212, 14);
		frmAsd.getContentPane().add(progressBar);
		
		JButton btnDownload_file = new JButton("download");
		btnDownload_file.setVisible(false);
		btnDownload_file.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				progressBar.setIndeterminate(true);
				progressBar.setVisible(true);
				
				JFileChooser jf = new JFileChooser();
				jf.setSelectedFile(new File(fileName + "." + estensione)); 
				int n = jf.showSaveDialog(null);
				if (n == 0) {
					road = jf.getSelectedFile();
					path = road.toString();
					Userdatas id = LoggedUser.anagrafica;
					progressBar.setVisible(false);
					ClientConnection.documentDownload(idDoc, id.userID, path, "");
				}
				
			}
		});
		btnDownload_file.setBounds(357, 346, 105, 23);
		frmAsd.getContentPane().add(btnDownload_file);

		JButton BTN_addfile = new JButton("aggiungi");
		BTN_addfile.setVisible(false);
		BTN_addfile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				JFileChooser jf = new JFileChooser();
				int n = jf.showOpenDialog(null);
				if (n == 0) {
					File f = jf.getSelectedFile();
					String str = f.getPath();

					
					ClientConnection.documentUpload(str, rif_Sezione);
					JOptionPane.showMessageDialog(null, "File aggiunto con successo");

				}
				Main();
				frmAsd.dispose();
				
			}
		});
		BTN_addfile.setBounds(249, 346, 98, 23);
		frmAsd.getContentPane().add(BTN_addfile);

		JButton btnaddcart = new JButton("Nuova cartella");
		btnaddcart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Folder NF = new Folder();
				NF.name = JOptionPane.showInputDialog(null, "inserisci il name della cartella");
				NF.description = JOptionPane.showInputDialog(null, "inserisci una description della cartella");
				Vector<FolderVisibility> v = new Vector<FolderVisibility>();
				v.addElement(FolderVisibility.PRIVATE);
				v.addElement(FolderVisibility.PUBLIC);
				JComboBox<FolderVisibility> jcd = new JComboBox<FolderVisibility>(v);

				Object[] options = new Object[] {};

				JOptionPane jop = new JOptionPane("Please Select", JOptionPane.QUESTION_MESSAGE,
						JOptionPane.DEFAULT_OPTION, null, options, null);
				

				JButton btn_visibilità = new JButton("conferma");
				btn_visibilità.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						// TODO Auto-generated method stub
						NF.visibility = (FolderVisibility) jcd.getSelectedItem();
						NF.parent = rif_Sezione;
						NF.sonFolders = null;
						NF.document = null;
						ClientConnection.creaSezione(NF, indicecorso);

						diag.dispose();
						frmAsd.dispose();
						Main();

					}
				});
				jop.add(jcd);
				jop.add(btn_visibilità);

				diag.getContentPane().add(jop);
				diag.pack();
				diag.setVisible(true);

			}
		});
		btnaddcart.setBounds(245, 276, 217, 23);
		frmAsd.getContentPane().add(btnaddcart);
		
		JButton btnCambiaVisibilita = new JButton("Modifica Visibilita");
		btnCambiaVisibilita.setVisible(false);
		btnCambiaVisibilita.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				Vector<FolderVisibility> v = new Vector<FolderVisibility>();
				v.addElement(FolderVisibility.PRIVATE);
				v.addElement(FolderVisibility.PUBLIC);
				JComboBox<FolderVisibility> jcd1 = new JComboBox<FolderVisibility>(v);

				Object[] options = new Object[] {};

				JOptionPane jop1 = new JOptionPane("Please Select", JOptionPane.QUESTION_MESSAGE,
						JOptionPane.DEFAULT_OPTION, null, options, null);
				
				JButton btn_visibilità = new JButton("conferma");
				btn_visibilità.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						// TODO Auto-generated method stub
						
						FolderVisibility vis = (FolderVisibility) jcd1.getSelectedItem();
						ClientConnection.cambiaVisibilita(rif_Sezione, vis);
						diag2.dispose();
						frmAsd.dispose();
						Main();

					}
				});
				jop1.add(jcd1);
				jop1.add(btn_visibilità);

				diag2.getContentPane().add(jop1);
				diag2.pack();
				diag2.setVisible(true);
				
				
			}
		});
		btnCambiaVisibilita.setBounds(248, 310, 214, 23);
		frmAsd.getContentPane().add(btnCambiaVisibilita);
		
		JButton btnElimina = new JButton("Elimina");
		btnElimina.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("DEBUG: CourseFile rifsezione value: " + rif_Sezione);
				ClientConnection.eliminaFile(rif_Sezione);
				ClientConnection.eliminaSezione(rif_Sezione);	
				frmAsd.dispose();
				CourseManagment.Main();	
			}
		});
		btnElimina.setVisible(false);
		btnElimina.setBounds(136, 311, 105, 23);
		frmAsd.getContentPane().add(btnElimina);

		tree.addTreeSelectionListener(new TreeSelectionListener() {
			@Override
			public void valueChanged(TreeSelectionEvent arg0) {
				try {
				progressBar.setVisible(false);
				btnDownload_file.setVisible(false);
				btnElimina.setVisible(false);
				BTN_addfile.setVisible(false);
				controlNode = (CustomJTreeNode) arg0.getPath().getLastPathComponent();			// recupero il name del nodo
				nameSelected = controlNode.getUserObject().toString();
				nameNode = (DefaultMutableTreeNode) arg0.getPath().getLastPathComponent();	
				

				try {
					if (!controlNode.isFolder) {
				String search = nameSelected.substring(0,nameSelected.length()-4);
				estensione = ClientConnection.getDocTypeByDocName(search);
				fileName =  nameSelected.substring(0,nameSelected.length() - estensione.length());
				System.out.println("DEBUG: fileName " + fileName);
					}
				} catch (NullPointerException ex) {
					
				}
				System.out.println("DEBUG: estensione " + estensione);
				
				System.out.println("DEBUG: " + "CourseManagment.[..]valueChanged(): controlNode.id = " + controlNode.id);
				System.out.println("DEBUG: " + "CourseManagment.[..]valueChanged(): nameNode = " + nameNode);
				
				if (!controlNode.isFolder) {
					btnDownload_file.setVisible(true);
					btnElimina.setVisible(true);
					idDoc = controlNode.id;
					rif_Sezione = controlNode.id;	
					ArrayList<Document> DocumentList = ClientConnection.getDocumentList();
					
					for(Document doc : DocumentList) {
						if(rif_Sezione == doc.idDoc) {
							textArea.setText(doc.description);
							System.out.println("DEBUG: doc.descrizione: " + doc);
						}
					}
					btnaddcart.setVisible(false);
					btnCambiaVisibilita.setVisible(false);
				} else {
					BTN_addfile.setVisible(true);
					btnDownload_file.setVisible(true);
					btnElimina.setVisible(true);
					idDoc = 0;
					rif_Sezione = controlNode.id;	
					
					for(Folder sez : fileSystem.folders) {
						if(rif_Sezione == sez.idSez) {
							textArea.setText(sez.description);
							System.out.println("DEBUG: sezione");
						}
					}
					
					System.out.println("DEBUG: " + "CourseManagment.[..]valueChanged(): controlNode.id = " + controlNode.id);
					btnCambiaVisibilita.setVisible(true);
					btnaddcart.setVisible(false);
					if (controlNode.id == -1) {
						btnaddcart.setVisible(true);
						btnCambiaVisibilita.setVisible(false);
						System.out.println("DEBUG: name del course: basi di dati! ");
						String descr = ClientConnection.courseDescription(CourseList.idLaurea);
						textArea.setText(descr); 
					}
				}
				
				rif_Sezione = controlNode.id;
				} catch (Exception ex ) {
					System.out.print("DEBUG: ex.printStackTrace() ");
					ex.printStackTrace();
				}
			}
			
		});

		btn_save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				courseDescr =  CourseList.description;
				String namecourse = NameCourseField.getText();
				String description = textArea.getText();
				ArrayList<Course> cm = ClientConnection.getAllCorsiMateria();
				
				for (Course c : cm) {

					if (c.Id == indicecorso) {
						c.description = description;
						c.name = namecourse;
						long idSezione = rif_Sezione;
						System.out.println("DEBUG: id sezione "+ rif_Sezione);
						System.out.println("DEBUG: name del course "+ c.name); 
						if (nameSelected.equals(c.name)) {
							// se ciò che viene selezionato corrisponde al name del corso
							 CourseList.description = description;
							 System.out.println("DEBUG: description del course "+ CourseList.description);
							ClientConnection.modificaCorsoMateria(c);
							
						} else  {
							ClientConnection.updateFileDescription(idSezione, description);				
						}
						
						
						for(Folder sez : fileSystem.folders) {
							if(rif_Sezione == sez.idSez) {
									ClientConnection.updateSezDescription(rif_Sezione, description);
							}
						}					

						
						CourseList.description = courseDescr;
						JOptionPane.showMessageDialog(null, "Update avvenuto con successo");
						break;
				}
				
				btn_save.setVisible(false);
				NameCourseField.setEditable(false);
				textArea.setEditable(false);
				CourseList.main();
				
				frmAsd.dispose();
			}
			}
		});
		

	}

	public static String getTitolo() {

		return titleFolder;
	}
}
