package Common.DBType;

/**
 *  @author Amenta Stefano, Moroni Paolo 
 * Classe che permette di memorizzare la guida
 *
 */
public class Help {
	public long id;
	public String title;
	public String description;
	
	public Help(long id, String title, String description) {
		this.title = title;
		this.description = description;
		this.id = id;
	}
}
