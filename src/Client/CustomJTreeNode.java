package Client;

import javax.swing.tree.DefaultMutableTreeNode;

/**  
 * @author Amenta Stefano, Moroni Paolo 
 * 
 * Classe usata per visualizzare il file system graficamente
 *
 */
public class CustomJTreeNode extends DefaultMutableTreeNode {
	private static final long serialVersionUID = 1L;
	public long id;
	public boolean isFolder;
	
	public CustomJTreeNode(String nodeName, long refID, boolean isF) {
		super(nodeName);
		id = refID;
		isFolder = isF;
	}
	
}
