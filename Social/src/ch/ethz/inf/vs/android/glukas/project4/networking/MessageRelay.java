package ch.ethz.inf.vs.android.glukas.project4.networking;

import ch.ethz.inf.vs.android.glukas.project4.UserId;
import ch.ethz.inf.vs.android.glukas.project4.protocol.PublicHeader;
import ch.ethz.inf.vs.android.glukas.project4.protocol.StatusByte;
import ch.ethz.inf.vs.android.glukas.project4.security.SecureChannel;

/**
 * Used to coordinate the reception of messages through a server that relays messages between peers.
 * Peers can register, and receive messages intended for them
 * @author glukas
 */
public class MessageRelay {

	private final SecureChannel channel;
	
	public MessageRelay(SecureChannel channel) {
		this.channel = channel;
	}
	
	/**
	 * Connect an user to the server
	 * @param message
	 * @param header
	 */
	public void connect(UserId userId){
		PublicHeader header = new PublicHeader(0, null, StatusByte.CONNECT.getByte(), 0, userId, null);
		channel.sendHeader(header);
	}
	
	/**
	 * Disconnect an user from the server
	 * @param message
	 * @param header
	 */
	public void disconnect(UserId userId){
		PublicHeader header = new PublicHeader(0, null, StatusByte.DISCONNECT.getByte(), 0, userId, null);
		channel.sendHeader(header);
	}
	
	/**
 	 * Asks the server to deliver all new messages for the given sender
	 * @param sender
	 */
	public void pollForNewMessages(UserId self, UserId other){
		
	}
}
