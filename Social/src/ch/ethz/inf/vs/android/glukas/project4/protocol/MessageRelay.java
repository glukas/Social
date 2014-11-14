package ch.ethz.inf.vs.android.glukas.project4.protocol;

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
	 * Will asynchronously trigger onRegistrationSucceeded();
	 * or onRegistrationFailed(FailureReason reason) to be called on the delegate
	 * Registering will trigger future 
	 */
	void registerForReception(User self, User other);
	
	/**
	 * Will asynchronously trigger onDeregistrationSucceeded();
	 * or onDeregistrationFailed(FailureReason reason) to be called on the delegate
	 */
	void deregisterForReception(User self);
	
	/**
 	 * Asks the server to deliver all new messages for the given sender
	 * @param sender
	 */
	void pollForNewMessages(User self, User other);
}
