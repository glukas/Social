package ch.ethz.inf.vs.android.glukas.project4.protocol;

/**
 * A message from the GUI
 */
public class UserMessage extends Message {
	
	/**
	 * Create new message from user, very simple : only containing a type
	 * @param type
	 */
	public UserMessage(MessageType type){
		this.requestType = type;
	}
}
