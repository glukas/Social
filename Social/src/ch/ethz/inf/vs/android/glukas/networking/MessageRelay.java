package ch.ethz.inf.vs.android.glukas.networking;

import ch.ethz.inf.vs.android.glukas.project4.protocol.PublicHeader;
import ch.ethz.inf.vs.android.glukas.project4.protocol.UserId;

/**
 * Used to coordinate the reception of messages through a server that relays messages between peers.
 * Peers can register, and receive messages intended for them
 * @author glukas
 */
public interface MessageRelay {

	/**
	 * Further delegate calls will be made to the delegate
	 * @param delegate
	 */
	void setDelegate(MessageRelayDelegate delegate);
	
	/**
	 * Connect an user to the server
	 * @param message
	 * @param header
	 */
	void connect(String message, PublicHeader header);
	
	/**
	 * Disconnect an user from the server
	 * @param message
	 * @param header
	 */
	void disconnect(String message, PublicHeader header);
	
	/**
 	 * Asks the server to deliver all new messages for the given sender
	 * @param sender
	 */
	void pollForNewMessages(UserId self, UserId other);
}
