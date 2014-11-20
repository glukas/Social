package ch.ethz.inf.vs.android.glukas.project4.protocol;

/**
 * A Message used by the protocol send over the network. (Either by the user or received from outside world)
 */
public abstract class Message {
	
	protected MessageType requestType;
	protected String usernameSender;
	protected String usernameReceiver;
	protected String message;
	protected int postId;
	protected String httpLink;
	
	public MessageType getMessageType(){
		return requestType;
	}

	public String getUsernameSender() {
		return usernameSender;
	}

	public String getUsernameReceiver() {
		return usernameReceiver;
	}

	public int getPostId() {
		return postId;
	}

	public String getHttpLink() {
		return httpLink;
	}
	
	public String getMessage() {
		return message;
	}
	
	/**
	 * Different types of messages
	 */
	public enum MessageType{
		FRIENDSHIP,
		POST_TEXT,
		POST_PICTURE,
		GET_WALL,
		SHOW_IMAGE,
		CONNECT,
		DISCONNECT,
		SEARCH_USER,
		ACK,
		BROADCAST,
		UNKOWN;
	}
}


