package ch.ethz.inf.vs.android.glukas.project4.protocol;

import ch.ethz.inf.vs.android.glukas.project4.User;

/**
 * A Message used by the protocol send over the network. (Either by the user or received from outside world)
 * It's used to do the internal logic inside the protocol.
 */
public class Message {
	
	/**
	 * Create a new empty Message
	 */
	public Message() {
		
	}
	
	/**
	 * Create new simple Message 
	 * @param type of the message. (Should only be subset of all types)
	 */
	public Message(MessageType type) {
		this.requestType = type;
	}
	
	//header
	protected User sender;
	protected User receiver;
	protected int postId;
	protected MessageType requestType;
	
	//content
	protected String httpLink;
	protected String message;
	protected String username;
	protected int id;
	protected int numM;
	
	////
	//Getters and Setters
	////
	
	public User getSender() {
		return sender;
	}

	public void setSender(User sender) {
		this.sender = sender;
	}

	public User getReceiver() {
		return receiver;
	}

	public void setReceiver(User receiver) {
		this.receiver = receiver;
	}

	public int getPostId() {
		return postId;
	}

	public void setPostId(int postId) {
		this.postId = postId;
	}

	public MessageType getRequestType() {
		return requestType;
	}

	public void setRequestType(MessageType requestType) {
		this.requestType = requestType;
	}

	public String getHttpLink() {
		return httpLink;
	}

	public void setHttpLink(String httpLink) {
		this.httpLink = httpLink;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getNumM() {
		return numM;
	}

	public void setNumM(int numM) {
		this.numM = numM;
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