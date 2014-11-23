package ch.ethz.inf.vs.android.glukas.project4.protocol;

/**
 * A Message used by the protocol send over the network. (Either by the user or received from outside world)
 * It's used to do the internal logic inside the protocol.
 */
public class Message {
	
	/**
	 * Create new simple Message 
	 * @param type of the message. (Should only be subset of all types)
	 */
	public Message(MessageType type) {
		this.requestType = type;
	}
	
	//header
	protected String usernameSender;
	protected String usernameReceiver;
	protected int postId;
	protected MessageType requestType;
	
	//content
	protected String message;
	protected String httpLink;
	protected String response;
	
	//setters
	public void setMessageType(MessageType requestType){
		this.requestType = requestType;
	}

	public void setUsernameSender(String usernameSender) {
		this.usernameSender = usernameSender;
	}

	public void setUsernameReceiver(String usernameReceiver) {
		this.usernameReceiver = usernameSender;
	}

	public void setPostId(int postId) {
		this.postId = postId;
	}

	public void setHttpLink(String httpLink) {
		this.httpLink = httpLink;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	public void setFriendshipResponse(String response){
		this.response= response;
	}
	
	
	//getters
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
	public String getFriendshipResponse(){
		return response;
	}
	
	/**
	 * Different types of messages
	 */
	public enum MessageType {
		
		//server
		CONNECT,
		DISCONNECT,
		
		//friends
		ACCEPT_FRIENDSHIP,
		REFUSE_FRIENDSHIP,
		ASK_FRIENDSHIP,
		SEARCH_USER,
		BROADCAST,
		
		//post new messages
		POST_PICTURE,
		POST_TEXT,
		ACK_POST,
		
		//retrieve data
		GET_POSTS,
		SHOW_IMAGE,
		SEND_PICTURE,
		SEND_TEXT,
		GET_STATE,
		SEND_STATE,
		
		//unknown
		UNKOWN;
	}
}