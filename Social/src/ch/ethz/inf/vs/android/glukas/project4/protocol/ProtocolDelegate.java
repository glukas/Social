package ch.ethz.inf.vs.android.glukas.project4.protocol;

import ch.ethz.inf.vs.android.glukas.project4.Post;
import ch.ethz.inf.vs.android.glukas.project4.exceptions.DatabaseException;
import ch.ethz.inf.vs.android.glukas.project4.exceptions.NetworkException;

/**
 * This class makes the link between the user interface and the outside world.
 * It provides method to post new data on the user's wall, to ask for another user's wall
 * to connect and disconnect from the connected list on server.
 */
public interface ProtocolDelegate {
	
	/**
	 * Connect the user to the network.
	 * It has to be done before any other methods call
	 * @throws NetworkException, if network is not accessible
	 */
	public void connect() throws NetworkException;
	
	/**
	 * Disconnect the user from the network.
	 * To regular quit of the application, this method should be called
	 * @throws NetworkException, if network is not accessible
	 */
	public void disconnect() throws NetworkException;
	
	/**
	 * Post a post of the user on his / her own wall
	 * @param post, to post 
	 * @throws DatabaseException, if database access is impossible
	 */
	public void postPost(Post post) throws DatabaseException;
	
	/**
	 * Get a wall from a distant user
	 * @param DistUsername, the user to get the wall
	 * @throws NetworkException, if network is not accessible
	 */
	public void getUserWall(String distUsername) throws NetworkException;
	
	/**
	 * Ask an user to be friend
	 * @param DistUsername
	 * @throws NetworkException, if network is not accessible
	 */
	public void askFriendship(String distUsername) throws NetworkException;

	/**
	 * Search for accessible persons on the local bluetooth network
	 */
	public void discoverFriends() throws NetworkException;
	
}
