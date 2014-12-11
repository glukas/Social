package ch.ethz.inf.vs.android.glukas.project4;

import java.util.List;

import ch.ethz.inf.vs.android.glukas.project4.exceptions.FailureReason;

/**
 * Interface to the network for calls back from the network
 */
public interface UserDelegate {
	
	/**
	 * The network received a post
	 * @param post
	 */
	public void onPostReceived(Post post);
	
	/**
	 * The connection to the server failed
	 * @param reason
	 */
	public void onConnectionFailed(FailureReason reason);
	
	/**
	 * Peers in the local bluetooth network have been discovered
	 * @param peers
	 */
	public void onPeersDiscoverySuccess(List<User> peers);
	
	/**
	 * The friendship demand has been accepted
	 */
	public void onFriendshipAccepted();
	
	/**
	 * The friendship demand has been declined
	 */
	public void onFriendshipDeclined();
}
 