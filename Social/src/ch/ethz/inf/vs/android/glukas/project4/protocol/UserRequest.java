package ch.ethz.inf.vs.android.glukas.project4.protocol;

/**
 * A request from the GUI encapsulation
 *
 */
public class UserRequest {
	
	public UserRequest(RequestType type){
		this.requestType = type;
	}
	
	
	public enum RequestType{
		FRIENDSHIP,
		POST_TEXT,
		POST_PICTURE,
		GET_WALL,
		SHOW_IMAGE,
		CONNECT,
		DISCONNECT,
		SEARCH_USER;
	}
	
	private RequestType requestType;
	private String usernameSender;
	private String usernameReceiver;
	private int postId;
	private String httpLink;
	
	public RequestType getRequestType(){
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
}
