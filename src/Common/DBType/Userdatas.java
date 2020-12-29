package Common.DBType;

import java.io.Serializable;

import Common.Enumerators.UserType;

public class Userdatas implements Serializable {
/**  @author Amenta Stefano, Moroni Paolo 
 *  Classe modello delle anagrafiche
 */
	private static final long serialVersionUID = 1L;
	public long userID;

	public long getUserID() {
		return userID;
	}

	public void setUserID(long matricola) {
		userID = matricola;
	}

	public String getName() {
		return name;
	}

	public void setName(String nome) {
		name = nome;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String cognome) {
		surname = cognome;
	}

	public String getEmail() {
		return Email;
	}

	public void setEmail(String email) {
		Email = email;
	}

	public String getActivationCode() {
		return activationCode;
	}

	public void setActivationCode(String codiceAttivazione) {
		activationCode = codiceAttivazione;
	}

	public String getPassword() {
		return Password;
	}

	public void setPassword(String password) {
		Password = password;
	}

	public UserType getT() {
		return t;
	}

	public void setT(UserType t) {
		this.t = t;
	}
	
	public boolean getTrusted()
	{
		return trusted;
	}

	public String name;
	public String surname;
	public String Email;
	public String activationCode;
	public String Password;
	public UserType t;
	public boolean trusted;
	public boolean isLocked;
	public long loginattempts;

	public Userdatas() {

	}

	public Userdatas(String nome, String cognome, String Email, String password, boolean Verificato, boolean isLocked, long tentativiLogin  ) {
		this.name = nome;
		this.surname = cognome;
		this.Email = Email;
		this.Password = password;
		this.trusted = Verificato;
		this.isLocked = isLocked;
		this.loginattempts = tentativiLogin;
	}
}
