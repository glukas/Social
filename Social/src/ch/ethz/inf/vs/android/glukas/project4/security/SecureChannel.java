package ch.ethz.inf.vs.android.glukas.project4.security;

import ch.ethz.inf.vs.android.glukas.project4.protocol.PublicHeader;

public interface SecureChannel {

	/**
	 * Further delegate calls will be made to the delegate
	 * @param delegate
	 */
	void setDelegate(SecureChannelDelegate delegate);
	
	/**
	 * Broadcast a message to all friends
	 * @param message
	 * @param header
	 */
	void broadcastMessage(String message, PublicHeader header);
	
	/**
	 * Precondition: user was discovered by onPeersDiscovered
	 * @param user
	 */
	//void requestFriendship(User user);
	
	/**
	 * Start looking for peers.
	 * Will asynchronously trigger onPeersDiscovered(List<String> peers)
	 * or onPeerDiscoveryFailed(FailureReason reason) to be called on the delegate
	 */
	//void startPeerDiscovery();
	
}
