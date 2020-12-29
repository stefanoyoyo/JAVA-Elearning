package Common.DBType;

import java.io.Serializable;
import java.util.ArrayList;

import Common.Enumerators.FolderVisibility;

public class Folder implements Serializable {

	/**
	 *  @author Amenta Stefano, Moroni Paolo 
	 * Classe modello di una cartella del file sistem che rappresenta una sezione.
	 */
	private static final long serialVersionUID = 1L;
	public long idSez;
	public String name;
	public String description;
	public long parent;
	public ArrayList<Folder> sonFolders;
	public ArrayList<Document> document;
	public FolderVisibility visibility;
}
