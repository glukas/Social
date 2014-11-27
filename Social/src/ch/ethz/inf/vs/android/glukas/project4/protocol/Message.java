package ch.ethz.inf.vs.android.glukas.project4.protocol;

import ch.ethz.inf.vs.android.glukas.project4.BasicUser;

/**
 * A Message used by the protocol send over the network. (Either by the user or received from outside world)
 * It's used to do the internal logic inside the protocol.
 */
public class Message {
	
	/**
	 * New Message
	 * @param sender
	 * @param receiver
	 * @param postId
	 * @param type
	 * @param link
	 * @param msg
	 * @param username
	 * @param id
	 * @param numM
	 */
	public Message(BasicUser sender, BasicUser receiver, int postId, MessageType type, String link, String msg, String username, int id, int numM){
		this.sender = sender;
		this.receiver = receiver;
		this.postId = postId;
		this.requestType = type;
		this.httpLink = link;
		this.message = msg;
		this.username = username;
		this.id = id;
		this.numM = numM;
	}
	
	//header
	protected BasicUser sender;
	protected BasicUser receiver;
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
	
	public BasicUser getSender() {
		return sender;
	}

	public void setSender(BasicUser sender) {
		this.sender = sender;
	}

	public BasicUser getReceiver() {
		return receiver;
	}

	public void setReceiver(BasicUser receiver) {
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
	
	////
	//Public Methods
	////
	
	@Override
	public String toString() {
		if (sender != null) {
			if (receiver != null){
				return "Message of "+sender.getUsername()+" ("+sender.getId()+") received by "+receiver.getUsername()+" ("+receiver.getId()+
						" )"+" with content : "+"(LINK)"+httpLink+" (MESSAGE) "+message+" (USERNAME)"+username+" (ID)"+id+
						" (NUM_M)"+numM;
			} else {
				return "Message of "+sender.getUsername()+" ("+sender.getId()+") received by UNKNOWN with content : "
						+"(LINK)"+httpLink+" (MESSAGE) "+message+" (USERNAME)"+username+" (ID)"+id+
						" (NUM_M)"+numM;
			}
		} else {
			if (receiver != null){
				return "Message of UNKNOWN received by "+receiver.getUsername()+" ("+receiver.getId()+
						" )"+" with content : "+"(LINK)"+httpLink+" (MESSAGE) "+message+" (USERNAME)"+username+" (ID)"+id+
						" (NUM_M)"+numM;
			} else {
				return "Message of UNKNOWN received by UNKNOWN"+
						" with content : "+"(LINK)"+httpLink+" (MESSAGE) "+message+" (USERNAME)"+username+" (ID)"+id+
						" (NUM_M)"+numM;
			}
		}
	}

	/**
	 * Different types of messages
	 */
	public enum MessageType {
		
		//friends
		ACCEPT_FRIENDSHIP,
		REFUSE_FRIENDSHIP,
		ASK_FRIENDSHIP,
		SEARCH_USER,
		
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