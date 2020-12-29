package Client.GUI;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

import Admin.GUI.CourseList;
import Client.ClientConnection;
import Client.CustomJTreeNode;
import Client.LoggedUser;
import Common.DBType.Userdatas;
import Common.DBType.Folder;
import Common.DBType.Course;
import Common.DBType.Document;
import Common.DBType.CourseFileSystem;
import Common.Enumerators.UserType;
import Common.Enumerators.FolderVisibility;



/**
 *  GUI
 * 
 * @author Amenta Stefano, Moroni Paolo
 *
 */
public class CourseFile {

	private JFrame frmAsd;
	private JTextField NameCourseField;
	private static String titleFolder = null;
	static long indicecorso;
	File road = null;
	private long idDoc;
	private String path;
	private long rif_Sezione;
	private JDialog diag = new JDialog();
	private JDialog diag2 = new JDialog();
	private static String nameSelected;
	CustomJTreeNode controlNode;
	private DefaultMutableTreeNode nameNode;
	private ArrayList<Course> courseList = new ArrayList<Course>();
	private static CourseFileSystem fileSystem;
	public static String nomeCorso = null;
	private static String courseDescr;
	private static String estensione = null;
	private static String fileName = null;
	/**
	 * Launch the application.
	 */

//	public void SottoNodiFs(DefaultMutableTreeNode father, ArrayList<Folder> subFolders) {
//		CustomJTreeNode node = null;
//
//		if (LoggedUserController.userType == UserType.Docente) {
//			for (Folder folder : subFolders) {
//				node = new CustomJTreeNode(folder.NomeSezione, folder.idSez, true);
//				SottoNodiFs(node, folder.SottoCartelle);
//				father.add(node);
//			}
//		} else {
//			for (Folder folder : subFolders) {
//				if (folder.visibilità == FolderVisibility.PUBLIC || folder.visibilità != FolderVisibility.PRIVATE) {
//					node = new CustomJTreeNode(folder.NomeSezione, folder.idSez, true);
//					SottoNodiFs(node, folder.SottoCartelle);
//					father.add(node);
//				}
//			}
//		}
//	}
	
	public void removeFolder(DefaultMutableTreeNode nameNode) {
		  System.out.println(nameNode.getParent().getIndex(nameNode));
		  int nodeId = nameNode.getParent().getIndex(nameNode);
		  nameNode.remove(nodeId);	  
	}

	public void ContenutiFs(DefaultMutableTreeNode father, ArrayList<Document> contents) {
		try {
			CustomJTreeNode node = null;
			for (Document folder : contents) {
				node = new CustomJTreeNode(folder.name + folder.docType, folder.idDoc, false);
				father.add(node);
			}
		} catch (Exception e) {
			
		}
	}
	

	public void creaNodiFileSystem(CourseFileSystem fs, JTree tree, CustomJTreeNode radice) {
		try {
			CustomJTreeNode node = null;

			if (fs.folders != null) {
				
				if (LoggedUser.userType == UserType.TEACHER) {
					for (Folder folder : fs.folders) {
						node = new CustomJTreeNode(folder.name, folder.idSez, true);
//						SottoNodiFs(node, folder.SottoCartelle);
						ContenutiFs(node, folder.document);
						radice.add(node);
					}
				} else {
					for (Folder folder : fs.folders) {
						if (folder.visibility == FolderVisibility.PUBLIC) {
							node = new CustomJTreeNode(folder.name, folder.idSez, true);
//							SottoNodiFs(node, folder.SottoCartelle);
							ContenutiFs(node, folder.document);
							radice.add(node);
						}
					}
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
					CourseFile window = new CourseFile();
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
	public CourseFile() {
		initialize();

	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {

		new ClientCourseList();

		if (LoggedUser.userType == UserType.TEACHER) {
			courseList = ClientCourseList.LcorsiregisteredDocente;
		} else {
			courseList = ClientCourseList.LcorsiregisteredStudent; 
			
		}

		String title = ClientCourseList.titolodainviare;
		indicecorso = ClientCourseList.idsend;
		frmAsd = new JFrame();
		frmAsd.setIconImage(Toolkit.getDefaultToolkit().getImage("media/f.png"));
		frmAsd.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		if (LoggedUser.userType == UserType.TEACHER) {
			frmAsd.setTitle("Docente - " + title);
		} else {
			frmAsd.setTitle("Studente - " + title);
		}
		frmAsd.getContentPane().setBackground(SystemColor.inactiveCaption);
		frmAsd.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				if (LoggedUser.userType.toString().equals("STUDENT"))
					ClientConnection.updateLoc(LoggedUser.anagrafica.userID, -1);
				frmAsd.dispose();
				ClientCourseList.main();
			}
		});
		frmAsd.setBounds(100, 100, 494, 451);
		frmAsd.getContentPane().setLayout(null);

		NameCourseField = new JTextField(title);
		NameCourseField.setEditable(false);
		NameCourseField.setBounds(27, 13, 384, 25);
		frmAsd.getContentPane().add(NameCourseField);
		NameCourseField.setColumns(10);
		
		
		nomeCorso = ClientCourseList.descrizionedasend;
		
		JTextArea textArea = new JTextArea("");
		textArea.append(nomeCorso);
		textArea.setEditable(false);
		textArea.setBounds(27, 83, 197, 256);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		frmAsd.getContentPane().add(textArea);
		new ImageIcon("media/chiave.png");

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(248, 86, 214, 219);
		frmAsd.getContentPane().add(scrollPane);

		CustomJTreeNode radice = new CustomJTreeNode(title, -1, true);
		JTree tree = new JTree(radice);
		tree.setRootVisible(true);
		indicecorso = ClientCourseList.idsend;
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
		progressBar.setBounds(13, 387, 449, 14);
		frmAsd.getContentPane().add(progressBar);

		JButton btnDownload_file = new JButton("Download");
		btnDownload_file.setVisible(false);
		btnDownload_file.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				progressBar.setIndeterminate(true);
				progressBar.setVisible(true);
				new File("nameDoc");
				
				JFileChooser jf = new JFileChooser();
				jf.setSelectedFile(new File(fileName + "." + estensione));// devo prendere il name del file dal filesistem
				int n = jf.showSaveDialog(null);
				if (n == 0) {
					road = jf.getSelectedFile();
					road.getPath();
					path = road.toString();
					nameNode.toString();
					Userdatas id = LoggedUser.anagrafica;
					progressBar.setVisible(false);
					ClientConnection.documentDownload(idDoc, id.userID, path, "");
					ClientConnection.updateDownloadCont(id.userID, idDoc);
				}				
			}
		});
		btnDownload_file.setBounds(357, 316, 105, 23);
		frmAsd.getContentPane().add(btnDownload_file);

		JButton BTN_addfile = new JButton("Aggiungi");
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

				frmAsd.dispose();
				CourseFile.Main();
			}
		});
		BTN_addfile.setBounds(248, 316, 105, 23);
		frmAsd.getContentPane().add(BTN_addfile);

		
		JButton buttonSave = new JButton("Salva");
		JButton btnaddcart = new JButton("Aggiungi cartella");
		if (LoggedUser.userType.toString().equals("STUDENT")) {
			btnaddcart.setVisible(false);
			buttonSave.setVisible(false);
		}
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
						diag.dispose();
						frmAsd.dispose();
						ClientConnection.creaSezione(NF, indicecorso);
						CourseFile.Main();

					}
				});
				jop.add(jcd);
				jop.add(btn_visibilità);

				diag.getContentPane().add(jop);
				diag.pack();
				diag.setVisible(true);

			}
		});
		btnaddcart.setBounds(248, 49, 217, 23);
		frmAsd.getContentPane().add(btnaddcart);

		JButton btnCambiaVisibilita = new JButton("Visibilita");
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
		btnCambiaVisibilita.setBounds(361, 353, 101, 23);
		frmAsd.getContentPane().add(btnCambiaVisibilita);

		JButton btnInviaMail = new JButton("Invia e-mail");
		if (LoggedUser.userType.toString().equals("STUDENT")) {
			btnInviaMail.setText("Mail docente");
			btnInviaMail.setBounds(265, 310, 89, 23);
			btnInviaMail.setVisible(true);
		}
		btnInviaMail.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new NewsLetter();
				frmAsd.dispose();
				NewsLetter.main();
			}
		});
		btnInviaMail.setBounds(27, 49, 208, 23);
		frmAsd.getContentPane().add(btnInviaMail);
		
		JButton btnElimina = new JButton("Elimina");
		btnElimina.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("DEBUG: CourseFile rifsezione value: " + rif_Sezione);
				ClientConnection.eliminaFile(rif_Sezione);
				ClientConnection.eliminaSezione(rif_Sezione);	
				frmAsd.dispose();
				CourseFile.Main();	
			}
		});
		btnElimina.setVisible(false);
		btnElimina.setBounds(248, 353, 105, 23);
		frmAsd.getContentPane().add(btnElimina);
		
		
		
		if (LoggedUser.userType.toString().equals("STUDENT")) {
			buttonSave.setVisible(false);
			btnaddcart.setVisible(false);
		}
		buttonSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				
				
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
							// se ciò che viene selezionato corrisponde al name  del course
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
				
				buttonSave.setVisible(false);
				NameCourseField.setEditable(false);
				textArea.setEditable(false);
				new CourseList();
				CourseList.main();
				
				frmAsd.dispose();
				}
				
				buttonSave.setVisible(false);
				NameCourseField.setEditable(false);
				textArea.setEditable(false);
				buttonSave.setVisible(false);
				
			}
		});
		buttonSave.setBounds(13, 353, 211, 23);
		frmAsd.getContentPane().add(buttonSave);
		
		
		JButton changeDescr = new JButton((Icon) null);
		changeDescr.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				NameCourseField.setEditable(true);
				textArea.setEditable(true);
			}
		});
		changeDescr.setBounds(428, 14, 34, 25);
		frmAsd.getContentPane().add(changeDescr);
		

		if (LoggedUser.userType.toString().equals("STUDENT")) {
			changeDescr.setVisible(false);
			buttonSave.setVisible(false);
			buttonSave.setVisible(false);
		}
		

		tree.addTreeSelectionListener(new TreeSelectionListener() {
			@Override
			public void valueChanged(TreeSelectionEvent arg0) {
				progressBar.setVisible(false);
				btnDownload_file.setVisible(false);
				if (LoggedUser.userType.toString().equals("STUDENT")) {
					buttonSave.setVisible(false);
					buttonSave.setVisible(false);
				} else {
				buttonSave.setVisible(true);
				}
				BTN_addfile.setVisible(false);
				controlNode = (CustomJTreeNode) arg0.getPath().getLastPathComponent();
				nameNode = (DefaultMutableTreeNode) arg0.getPath().getLastPathComponent();;
				
				nameSelected = controlNode.getUserObject().toString();

				
				if (!controlNode.isFolder) {

					String search = nameSelected.substring(0,nameSelected.length()-4);
					estensione = ClientConnection.getDocTypeByDocName(search);
					fileName =  nameSelected.substring(0,nameSelected.length() - estensione.length());
					System.out.println("DEBUG: fileName " + fileName);
					
					btnDownload_file.setVisible(true);
						if (LoggedUser.userType == UserType.TEACHER) {
							btnCambiaVisibilita.setVisible(true);
							btnElimina.setVisible(true);
						}
					idDoc = controlNode.id;
					rif_Sezione = controlNode.id;	
					ArrayList<Document> DocumentList = ClientConnection.getDocumentList();	
					
					for(Document doc : DocumentList) {
						if(rif_Sezione == doc.idDoc) {
							textArea.setText(doc.description);
							System.out.println("DEBUG: name: " + doc);
						}
					}
					btnaddcart.setVisible(false);
					if (LoggedUser.userType != UserType.TEACHER) {
						btnCambiaVisibilita.setVisible(false);
					}
				} else {
					if (LoggedUser.userType == UserType.TEACHER) {
						BTN_addfile.setVisible(true); 
						btnCambiaVisibilita.setVisible(true);
						btnElimina.setVisible(true);
					}
					btnDownload_file.setVisible(true);
					idDoc = 0;
					rif_Sezione = controlNode.id;	
					for(Folder sez : fileSystem.folders) {
						if(rif_Sezione == sez.idSez) {
							textArea.setText(sez.description);
							System.out.println("DEBUG: sezione");
							break;
						}
					}
					
					System.out.println("DEBUG: " + "CourseManagment.[..]valueChanged(): controlNode.id = " + controlNode.id);
					if (LoggedUser.userType == UserType.TEACHER) {
						btnCambiaVisibilita.setVisible(true);
					}
					btnaddcart.setVisible(false);
					if (controlNode.id == -1) {
						if (LoggedUser.userType == UserType.TEACHER) {
							btnaddcart.setVisible(true);
						}
						btnCambiaVisibilita.setVisible(false);
						System.out.println("DEBUG: name del course: " + nomeCorso);
						textArea.setText(nomeCorso);
					}
				}

				rif_Sezione = controlNode.id;
			}
		});

	}

	public static String getTitolo() {

		return titleFolder;
	}
}
