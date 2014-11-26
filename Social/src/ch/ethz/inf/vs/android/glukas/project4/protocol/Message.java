package ch.ethz.inf.vs.android.glukas.project4.protocol;

import ch.ethz.inf.vs.android.glukas.project4.Post;
import ch.ethz.inf.vs.android.glukas.project4.User;
import ch.ethz.inf.vs.android.glukas.project4.Post.PostType;

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
	 * Create a new Message of type SEND_STATE
	 * @param sender
	 * @param receiver
	 * @param type
	 * @param id
	 * @param numM
	 */
	public Message(User sender, User receiver, int id, int numM) {
		this.sender = sender;
		this.receiver = receiver;
		this.requestType = MessageType.SEND_STATE;
		this.id = id;
		this.numM = numM;
	}
	
	
	public Message(Post post, User receiver, User sender, boolean isSend){
		this.sender = sender;
		this.receiver = receiver;
		postId = post.getId();
		message = post.getText();
		id = post.getId();
		if (isSend) {
			if (post.getType().equals(PostType.PICTURE)){
				requestType = MessageType.SEND_PICTURE;
				httpLink = post.getImageLink();
			} else {
				requestType = MessageType.SEND_TEXT;
			}
		} else {
			if (post.getType().equals(PostType.PICTURE)){
				requestType = MessageType.POST_PICTURE;
				httpLink = post.getImageLink();
			} else {
				requestType = MessageType.POST_TEXT;
			}
		}
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
	protected int postId = 0;
	protected MessageType requestType;
	
	//content
	protected String httpLink = "";
	protected String message = "";
	protected String username = "";
	protected int id = 0;
	protected int numM = 0;
	
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