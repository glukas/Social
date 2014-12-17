package ch.ethz.inf.vs.android.glukas.project4.protocol;

import android.graphics.Bitmap;
import ch.ethz.inf.vs.android.glukas.project4.BasicUser;
import ch.ethz.inf.vs.android.glukas.project4.UserId;

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
	public Message(UserId sender, UserId receiver, int postId, MessageType type, String msg, String username, int id, int numM){
		this.sender = sender;
		this.receiver = receiver;
		this.postId = postId;
		this.requestType = type;
		this.message = msg;
		this.username = username;
		this.id = id;
		this.numM = numM;
	}
	
	//header
	protected UserId sender;
	protected UserId receiver;
	protected int postId;
	protected MessageType requestType;
	
	
	//content
	protected String message;
	protected String username;
	protected int id;
	protected int numM;
	protected Bitmap image;
	protected byte[] payload;
	
	////
	//Getters and Setters
	////
	
	public UserId getSender() {
		return sender;
	}

	public void setSender(UserId sender) {
		this.sender = sender;
	}

	public UserId getReceiver() {
		return receiver;
	}

	public void setReceiver(UserId receiver) {
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
	
	public void setPayload(byte[] byteArray) {
		this.payload = byteArray;
	}
	
	public byte[] getPayload() {
		return payload;
	}
	
	////
	//Public Methods
	////
	
	@Override
	public String toString() {
		if (sender != null) {
			if (receiver != null){
				return "Message of "+" ("+sender.getId()+") received by "+" ("+receiver.getId()+
						" )"+" with content : "+" (MESSAGE) "+message+" (USERNAME)"+username+" (ID)"+id+
						" (NUM_M)"+numM;
			} else {
				return "Message of "+" ("+sender.getId()+") received by UNKNOWN with content : "
						+ "(MESSAGE) "+message+" (USERNAME)"+username+" (ID)"+id+
						" (NUM_M)"+numM;
			}
		} else {
			if (receiver != null){
				return "Message of UNKNOWN received by "+" ("+receiver.getId()+
						" )"+" with content : "+" (MESSAGE) "+message+" (USERNAME)"+username+" (ID)"+id+
						" (NUM_M)"+numM;
			} else {
				return "Message of UNKNOWN received by UNKNOWN"+
						" with content : "+" (MESSAGE) "+message+" (USERNAME)"+username+" (ID)"+id+
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