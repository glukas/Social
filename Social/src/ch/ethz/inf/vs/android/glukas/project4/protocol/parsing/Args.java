package ch.ethz.inf.vs.android.glukas.project4.protocol.parsing;

/**
 * Enumeration of possible arguments (values for JSONObject) used in internal protocol
 */
public enum Args{
	CONNECT("connect"),
	DEM_FRIEND("askFriendship"),
	DISCONNECT("disconnect"),
	GET_WALL("getWall"),
	POST_TXT("postText"),
	POST_PIC("postPicture"),
	ACK("ack"),
	BROADCAST("broadcast");
	
	private String name;
	
	Args(String s){
		this.name = s;
	}
	
	public String getStr(){
		return name;
	}
}