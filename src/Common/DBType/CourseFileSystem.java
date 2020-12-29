package Common.DBType;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *  @author Amenta Stefano, Moroni Paolo 
 *  
 * Classe modello del file sistem
 *
 */
public class CourseFileSystem implements Serializable {
	private static final long serialVersionUID = 1L;
	public Course course;
	public ArrayList<Folder> folders;

}
