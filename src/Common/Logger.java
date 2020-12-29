package Common;

import java.io.FileWriter;
import java.util.Date;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Date;
import Common.Logger;
import Common.DBType.*;
import Common.Enumerators.DocumentType;
import Common.Enumerators.RequestType;
import Common.Enumerators.UserType;
import Common.Pacchetti.RequestContent;
import Common.Pacchetti.ReceiveContent;
import Server.Database.Corsi.CorsiLaureaController;


/**
 * @author Amenta Stefano, Moroni Paolo 
 * 
 * Classe che permette all'utente di richiedere delle funzionalità al server
 *
 */

/**
 * Classe che scrive il file loggher csv, contenente tutti gli errori, MOLTO ULTILE IN FASE DI DEBUG!
 *
 */
public class Logger {
	public static void WriteError(Exception e, String Class, String Function) {
		try {
			FileWriter pw = new FileWriter("Logger.csv", true);
			Date now = new Date();
			StringBuilder builder = new StringBuilder();
			builder.append(now.toString()+",");
			builder.append(Class+",");
			builder.append(Function+",");
			builder.append(e.getMessage());
			pw.write(builder.toString()+";\n");
			pw.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
