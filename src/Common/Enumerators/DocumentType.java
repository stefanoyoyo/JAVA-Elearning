package Common.Enumerators;

import java.io.Serializable;

/**
 *  @author Amenta Stefano, Moroni Paolo 
 *  
 * Descrive tutte le possibili richieste che possono essere inoltrate dal client
 * al server
 *
 */
public enum DocumentType implements Serializable {
	DEGREECOURSES, COURSES, BOTHFILES
}
