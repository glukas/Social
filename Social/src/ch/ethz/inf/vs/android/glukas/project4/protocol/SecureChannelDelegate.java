package ch.ethz.inf.vs.android.glukas.project4.protocol;

import java.util.List;

import ch.ethz.inf.vs.android.glukas.project4.Wall;
import ch.ethz.inf.vs.android.glukas.project4.exceptions.FailureReason;

/**
 * This class makes the link between the network calls back and above.
 * It provides method to the network layer to notify the application of
 * news received from network.
 */
public interface SecureChannelDelegate {
	
	/**
	 * A protocol message was received.
	 * @param message
	 */
	public void onMessageReceived(String message);
	
	/**
	 * The previous search for peers is successful
	 * @param peers
	 */
	public void onPeersDiscovered(List<String> peers);
	
	/**
	 * The previous search has failed
	 * @param reason
	 */
	public void onPeerDiscoveryFailure(FailureReason reason);
	
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

}
