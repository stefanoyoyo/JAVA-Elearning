package Common.DBType;

import java.util.ArrayList;

public class Teacher extends Userdatas implements java.io.Serializable {
	/**
	 *  @author Amenta Stefano, Moroni Paolo 
	 * Classe modello di un docente
	 */
	private static final long serialVersionUID = 1L;
	public long department;
	public ArrayList<Course> courses;
}
