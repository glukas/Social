package ch.ethz.inf.vs.android.glukas.project4.database;

import ch.ethz.inf.vs.android.glukas.project4.Wall;
import ch.ethz.inf.vs.android.glukas.project4.protocol.UserId;

/**
 * List of all functionalities offered by the database
 */
public interface DatabaseDelegate {
	
	//TODO @Vincent : talk with Alessio about all that stuff 

	public String getUsername(UserId id);
	
	public Wall getWall(UserId id);
	
	public UserId getUserid(String username);
}
