package ch.ethz.inf.vs.android.glukas.project4.security;

import ch.ethz.inf.vs.android.glukas.project4.networking.MessageRelay;
import ch.ethz.inf.vs.android.glukas.project4.protocol.PublicHeader;

public class SecureChannel {

	private SecureChannelDelegate secureChannelDelegate;

	/**
	 * @return the relay used to send and receive message over the net
	 */
	public MessageRelay getMessageRelay() {
		return messageRelay;
	}
	
	/**
	 * Further delegate calls will be made to the delegate
	 * Delegate calls are made on the thread that instantiated this object
	 * @param delegate
	 */
	public void setDelegate(SecureChannelDelegate delegate) {
		this.secureChannelDelegate = delegate;
	}
	
	/**
	 * Asynchronously sends a message over the message relay to the recipients specified by the header
	 * @param message
	 * @param header
	 */
	public void sendMessage(String message, PublicHeader header) {
		
		//For broadcasts:
		//TODO encrypt & authenticate using symmetric broadcast key of receiver

		//For direct messages:
		//TODO encrypt under the public key of the recipients

		//In any case:
		//TODO sign using private key of sender
		
		//TODO prepend the length of the message
		
		//TODO send over the network
	}
	
	MessageRelay messageRelay;//relay used to send/receive messages
	
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
