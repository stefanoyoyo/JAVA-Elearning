package Common.Pacchetti;

import java.io.Serializable;

import Common.Enumerators.RequestType;
import Common.Enumerators.UserType;

/**
 *  @author Amenta Stefano, Moroni Paolo 
 * Contiene i paramentri richiesti dall'utente
 *
 */
public class RequestContent implements Serializable  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RequestType type;
	public UserType userType;
	public Object[] parameters;

}
