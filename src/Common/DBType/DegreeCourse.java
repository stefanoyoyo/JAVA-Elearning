package Common.DBType;

import java.io.Serializable;

public class DegreeCourse implements Serializable {
	/** 
	 * @author Amenta Stefano, Moroni Paolo 
	 * Classe modello di un Corso Materia.
	 */
	private static final long serialVersionUID = 1L;
	public long Id;
	public String name;
	public long department;
}
