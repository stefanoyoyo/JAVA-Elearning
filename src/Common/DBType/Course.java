package Common.DBType;

import java.io.Serializable;


/**
 * @author Amenta Stefano, Moroni Paolo 
 * 
 * Classe modello di un Corso Materia.
 *
 */
public class Course implements Serializable {
	private static final long serialVersionUID = 1L;
	public long Id;
	public long degreeCourse;
	public String name;
	public int activationYear;
	public String description;
}
