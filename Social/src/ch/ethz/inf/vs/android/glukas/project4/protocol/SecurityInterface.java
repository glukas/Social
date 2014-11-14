package ch.ethz.inf.vs.android.glukas.project4.protocol;

import ch.ethz.inf.vs.android.glukas.project4.Wall;
import ch.ethz.inf.vs.android.glukas.project4.exceptions.FailureReason;

/**
 * This class makes the link between the network calls back and above.
 * It provides method to the network layer to notify the application of
 * news received from network.
 */
public interface SecurityInterface {
	
	/**
	 * A wall has been received from the network
	 * @param wall
	 */
	public void onWallReceived(Wall wall);
	
	/**
	 * A wall asked by the user is not accessible
	 * @param username, the name of the user of the wall asked
	 * @param reason, the reason why the wall is not accessible
	 */
	public void onWallFailedToAccess(String username, FailureReason reason);
	
	
	/**
	 * Someone asked the user to be friends
	 * @param username 
	 * @param comment String linked with the demand 
	 */
	public void onFriendshipAsked(String username, String comment);
	
	/**
	 * Someone declined invitation of the user to be friend
	 * @param username 
	 * @param comment String that explains the reject
	 */
	public void onFriendshipDeclined(String username, String comment);
	
	/**
	 * Someone accepted invitation of the user to be friend
	 * @param username 
	 * @param comment
	 */
	public void onFriendshipAccepted(String username, String comment);
	
	/**
	 * Something went wrong during the process of demand
	 * @param username 
	 * @param reason the reason of the failure
	 */
	public void onFriendshipFailure(String username, FailureReason reason);
	
	
	/**
	 * The previous search for user is successful
	 * @param usernames
	 */
	public void onUsersFound(String[] usernames);
	
	/**
	 * The previous search has failed
	 * @param reason
	 */
	public void onUsersSearchFailure(FailureReason reason);
	
	
	/**
	 * Connection of the user to the server succeeded
	 */
	public void onConnectionSuccess();
	
	/**
	 * Connection of the user to the server failed
	 * @param reason
	 */
	public void onConnectionFailure(FailureReason reason);
	
	/**
	 * Disconnection of the user from the server succeeded
	 */
	public void onDisconnectionSuccess();
	
	/**
	 * Disconnection of the user from the server failed
	 * @param reason
	 */
	public void onDisconnectionFailure(FailureReason reason);
	
	/**
	 * The user is asked to store some data
	 * @param data
	 */
	public void onDataToStoreReceived(byte[] data);

}
