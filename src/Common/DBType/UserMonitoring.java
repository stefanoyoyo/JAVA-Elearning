package Common.DBType;


import java.util.Date;

/**
 *  @author Amenta Stefano, Moroni Paolo 
 * Classe che modellizza l'utente loggato
 *
 */
public class UserMonitoring extends Userdatas{
	private static final long serialVersionUID = 1L;
	
	private Date start;
	private Date end;
	public long IdCorsoAttuale;
	
	public UserMonitoring(Userdatas anag){
		this.name = anag.name;
		this.surname = anag.surname;
		this.Email = anag.Email;
		this.Password = anag.Password;
		this.userID = anag.userID;
		this.t = anag.t;
		start = null;
		end = null;
		IdCorsoAttuale = -1;
	}
	
	/**
	 * Dato in input un corso aggiorna la posizione di uno studente 
	 * connesso alla piattaforma, restituisce il numero di secondi 
	 * per cui l'utente resta connesso alla pagina del corso
	 * 
	 * @param newCorso
	 * @return secondInSection
	 */
	public long updateLocation(long newCorso) {
		try {
		long secondsInSection = 0;
		  IdCorsoAttuale = newCorso;
		if(start == null) {
			//inizializziamo
			start = new Date();
		}
		else {
			end = new Date();
			secondsInSection = end.getTime() - start.getTime();
			secondsInSection= secondsInSection / 1000 % 60;
            if(newCorso == -1) {
            	start = null;
            	end = null;
            }
            else {
    			start = new Date();		
            }
		}
		return secondsInSection;
		}
		catch(Exception e) {
			System.out.println("Errore locationUpdate");
			System.out.println("e.printStackTrace()");
			e.printStackTrace();
			return 0;
		}
	}

}
