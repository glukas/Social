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
	 */
	public void onMessageReceived(NetworkMessage message);
	
	/**
	 * The message failed to be sent to the server.
	 * @param message
	 */
	public void onSendFailed(NetworkMessage message);
	
	/**
	 * The message succeeded to be sent to the server.
	 * This does not mean that the receiver of the message has received the message yet.
	 * @param message
	 */
	public void onSendSucceeded(NetworkMessage message);
	

}
