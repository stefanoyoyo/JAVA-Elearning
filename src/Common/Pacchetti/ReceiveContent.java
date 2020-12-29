package Common.Pacchetti;

import java.io.Serializable;

/**
 *  @author Amenta Stefano, Moroni Paolo 
 * Un pacchetto inviato dal server in risposta al client contenenti i parametri richiesti dall'utente.
 *
 */
public class ReceiveContent implements Serializable  {


	private static final long serialVersionUID = 1;
	
	public Object[] parameters;

}
