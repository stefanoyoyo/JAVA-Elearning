package Common.DBType;

import java.io.Serializable;

public class Document implements Serializable {

	/**
	 *  @author Amenta Stefano, Moroni Paolo 
	 * Classe modello dei documenti salvati su server.
	 */
	private static final long serialVersionUID = 1L;
	public long idDoc;
	public long idSez;
	public String name;
	public String docType;
	public long ndownload;
	public byte[] data;
	public String description;
}
