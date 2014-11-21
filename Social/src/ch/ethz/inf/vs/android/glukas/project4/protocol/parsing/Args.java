package ch.ethz.inf.vs.android.glukas.project4.protocol.parsing;

/**
 * Enumeration of possible arguments (values for JSONObject) used in internal protocol
 */
public enum Args{
	CONNECT("connect"),
	DEM_FRIEND("askFriendship"),
	DISCONNECT("disconnect"),
	GET_POSTS("getPosts"),
	POST_TXT("postText"),
	POST_PIC("postPicture"),
	BROADCAST("broadcast"),
	REP_FRIEND("replyFriendship"),
	ACCEPT("accept"),
	REJECT("reject"),
	GET_STATE("getState"),
	SEND_STATE("sendState"),
	POST("post"),
	SEND_TXT("sendText"),
	SEND_PIC("sendPicture"),
	UNKNOWN("unknown");
	
	private String name;
	
	Args(String s){
		this.name = s;
	}
	
	public String getStr(){
		return name;
	}
}