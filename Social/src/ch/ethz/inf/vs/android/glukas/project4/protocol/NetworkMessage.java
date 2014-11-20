package ch.ethz.inf.vs.android.glukas.project4.protocol;

/**
 * A message received from the network
 */
public class NetworkMessage extends Message {
	
	/**
	 * New simple NetworkMessage
	 * @param header
	 * @param type
	 */
	public NetworkMessage(MessageType type){
		this.requestType = type;
	}
	
}
