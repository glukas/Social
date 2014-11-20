package ch.ethz.inf.vs.android.glukas.project4.security;

import java.util.List;
import ch.ethz.inf.vs.android.glukas.project4.exceptions.FailureReason;
import ch.ethz.inf.vs.android.glukas.project4.protocol.PublicHeader;

/**
 * This class makes the link between the network calls back and above.
 * The protocol layer implements this interface in order to discover and receive
 * updates from the other peers.
 */
public interface SecureChannelDelegate {
	
	/**
	 * A protocol message was received.
	 * @param message
	 * @param header the public header of the packet
	 */
	public void onMessageReceived(String message, PublicHeader header);
	
	/**
	 * The previous search for peers is successful
	 * @param peers
	 */
	//public void onPeersDiscovered(List<User> peers);
	
	/**
	 * The previous search has failed
	 * @param reason
	 */
	//public void onPeerDiscoveryFailed(FailureReason reason);
	
	/**
	 * The user accepted the friendship.
	 * @param user
	 */
	//public void onFriendshipAccepted(User user);

}
