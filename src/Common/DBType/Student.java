package Common.DBType;

import java.util.ArrayList;

import Common.Enumerators.CareerStatus;

/**
 *  @author Amenta Stefano, Moroni Paolo 
 * Classe modello di uno studente
 *
 */
public class Student extends Userdatas implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public long cLaurea;
	public int startYear;
	public CareerStatus status;
}
