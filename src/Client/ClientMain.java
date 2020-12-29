package Client;

/**
 *  @author Amenta Stefano, Moroni Paolo 
 *  Classe che dice se la connessione è avviata con successo
 *
 */
public class ClientMain {

	public static void main(String[] args) {

		if (ClientConnection.connect2Server()) {
			System.out.println("Successo nell'apertura della connessione");

		} else {
			System.out.println("Errore nell'apertura della connessione");
		}

	}

}
