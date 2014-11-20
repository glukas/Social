package ch.ethz.inf.vs.android.glukas.project4.protocol;

import org.json.JSONException;
import org.json.JSONObject;

import ch.ethz.inf.vs.android.glukas.project4.exceptions.UnknowRequestType;
import ch.ethz.inf.vs.android.glukas.project4.protocol.UserRequest.RequestType;

/**
 * Factory to create JSONObject
 */
public class JSONObjectFactory {
	
	////
	//Friendship
	////
	
	public static JSONObject getFriendshipBroadcastMessage() {
		JSONObject obj = new JSONObject();
		try{
			obj.put(Cmds.USER.getStr(), "USER-STATIC");
		} catch (JSONException ex) {
			obj = null;
			ex.printStackTrace();
		}
		return obj;
	}
	
	
	////
	//UserRequest
	//
	//the package-info gives more informations about how JSONObjects should be defined
	////
	
	/**
	 * Create a JSONObject from an UserRequest to be used to send messages over the network
	 */
	public static JSONObject createJSONObject(UserRequest request) {
		
		JSONObject obj = new JSONObject();
		
		try {
			if (request.getRequestType().equals(RequestType.CONNECT)){
				setConnectObj(request, obj);
			} else if (request.getRequestType().equals(RequestType.DISCONNECT)){
				setDisconnectObj(request, obj);
			} else if (request.getRequestType().equals(RequestType.FRIENDSHIP)){
				setFriendshipObj(request, obj);
			} else if (request.getRequestType().equals(RequestType.GET_WALL)){
				setGetWallObj(request, obj);
			} else if (request.getRequestType().equals(RequestType.SEARCH_USER)){
				setSearchUserObj(request, obj);
			} else if (request.getRequestType().equals(RequestType.SHOW_IMAGE)){
				setShowImageObj(request, obj);
			} else if (request.getRequestType().equals(RequestType.POST_PICTURE)){
				setPostPictureObj(request, obj);
			} else if (request.getRequestType().equals(RequestType.POST_TEXT)){
				setPostTextObj(request, obj);
			} else {
				try {
					throw new UnknowRequestType(request.getRequestType());
				} catch (UnknowRequestType e) {
					e.printStackTrace();
				}
			}
		} catch (JSONException e){
			e.printStackTrace();
		}
		return obj;
	}
	
	private static void setConnectObj(UserRequest request, JSONObject obj) throws JSONException{
		obj.put(Cmds.CMD.getStr(), Args.CONNECT.getStr());
	}
	
	private static void setDisconnectObj(UserRequest request, JSONObject obj) throws JSONException{
		obj.put(Cmds.CMD.getStr(), Args.DISCONNECT.getStr());
	}
	
	private static void setFriendshipObj(UserRequest request, JSONObject obj) throws JSONException{
		obj.put(Cmds.CMD.getStr(), Args.DEM_FRIEND.getStr());
		obj.put(Cmds.FROM.getStr(), request.getUsernameSender());
	}
	
	private static void setGetWallObj(UserRequest request, JSONObject obj) throws JSONException{
		obj.put(Cmds.CMD.getStr(), Args.GET_WALL.getStr());
	}
	
	private static void setSearchUserObj(UserRequest request, JSONObject obj) throws JSONException{
		obj.put(Cmds.USER.getStr(), request.getUsernameSender());
	}
	
	private static void setShowImageObj(UserRequest request, JSONObject obj){
		//not a JSON request, thus the object associated to this request is empty
	}
	
	private static void setPostPictureObj(UserRequest request, JSONObject obj) throws JSONException{
		obj.put(Cmds.CMD.getStr(), Args.POST_PIC.getStr());
		obj.put(Cmds.ID.getStr(), request.getPostId());
		obj.put(Cmds.TEXT.getStr(), request.getMessage());
		obj.put(Cmds.PIC.getStr(), request.getHttpLink());
	}
	
	private static void setPostTextObj(UserRequest request, JSONObject obj) throws JSONException{
		obj.put(Cmds.CMD.getStr(), Args.POST_TXT.getStr());
		obj.put(Cmds.ID.getStr(), request.getPostId());
		obj.put(Cmds.TEXT.getStr(), request.getMessage());
	}
	
	////
	//Definitions
	////
	
	/**
	 * Enumeration of possible commands (names for JSONObject) used in internal protocol
	 */
	private enum Cmds{
		CMD("cmd"),
		FROM("from"),
		USER("user"),
		ID("id"),
		TEXT("text"),
		PIC("picture");
		
		private String name;
		
		Cmds(String s){
			this.name = s;
		}
		
		public String getStr(){
			return name;
		}
	}
	
	/**
	 * Enumeration of possible arguments (values for JSONObject) used in internal protocol
	 */
	private enum Args{
		CONNECT("connect"),
		DEM_FRIEND("askFriendship"),
		DISCONNECT("disconnect"),
		GET_WALL("getWall"),
		POST_TXT("postText"),
		POST_PIC("postPicture");
		
		private String name;
		
		Args(String s){
			this.name = s;
		}
		
		public String getStr(){
			return name;
		}
	}
}
