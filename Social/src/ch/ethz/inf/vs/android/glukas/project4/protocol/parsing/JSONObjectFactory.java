package ch.ethz.inf.vs.android.glukas.project4.protocol.parsing;

import org.json.JSONException;
import org.json.JSONObject;

import ch.ethz.inf.vs.android.glukas.project4.exceptions.UnknowRequestType;
import ch.ethz.inf.vs.android.glukas.project4.protocol.Message;
import ch.ethz.inf.vs.android.glukas.project4.protocol.Message.MessageType;

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
			obj.put(Cmds.CMD.getStr(), Args.BROADCAST.getStr());
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
	public static JSONObject createJSONObject(Message request) {
		
		JSONObject obj = new JSONObject();
		
		// TODO : rewrite that
		
		try {
			if (request.getMessageType().equals(MessageType.CONNECT)){
				setConnectObj(request, obj);
			} else if (request.getMessageType().equals(MessageType.DISCONNECT)){
				setDisconnectObj(request, obj);
			}  else if (request.getMessageType().equals(MessageType.GET_POSTS)){
				setGetWallObj(request, obj);
			} else if (request.getMessageType().equals(MessageType.SEARCH_USER)){
				setSearchUserObj(request, obj);
			} else if (request.getMessageType().equals(MessageType.SHOW_IMAGE)){
				setShowImageObj(request, obj);
			} else if (request.getMessageType().equals(MessageType.POST_PICTURE)){
				setPostPictureObj(request, obj);
			} else if (request.getMessageType().equals(MessageType.POST_TEXT)){
				setPostTextObj(request, obj);
			} else {
				try {
					throw new UnknowRequestType(request.getMessageType());
				} catch (UnknowRequestType e) {
					e.printStackTrace();
				}
			}
		} catch (JSONException e){
			e.printStackTrace();
		}
		return obj;
	}
	
	private static void setConnectObj(Message request, JSONObject obj) throws JSONException{
		obj.put(Cmds.CMD.getStr(), Args.CONNECT.getStr());
	}
	
	private static void setDisconnectObj(Message request, JSONObject obj) throws JSONException{
		obj.put(Cmds.CMD.getStr(), Args.DISCONNECT.getStr());
	}
	
	private static void setGetWallObj(Message request, JSONObject obj) throws JSONException{
		obj.put(Cmds.CMD.getStr(), Args.GET_POSTS.getStr());
	}
	
	private static void setSearchUserObj(Message request, JSONObject obj) throws JSONException{
		obj.put(Cmds.USER.getStr(), request.getUsernameSender());
	}
	
	private static void setShowImageObj(Message request, JSONObject obj){
		//not a JSON request, thus the object associated to this request is empty
	}
	
	private static void setPostPictureObj(Message request, JSONObject obj) throws JSONException{
		obj.put(Cmds.CMD.getStr(), Args.POST_PIC.getStr());
		obj.put(Cmds.ID.getStr(), request.getPostId());
		obj.put(Cmds.TEXT.getStr(), request.getMessage());
		obj.put(Cmds.PIC.getStr(), request.getHttpLink());
	}
	
	private static void setPostTextObj(Message request, JSONObject obj) throws JSONException{
		obj.put(Cmds.CMD.getStr(), Args.POST_TXT.getStr());
		obj.put(Cmds.ID.getStr(), request.getPostId());
		obj.put(Cmds.TEXT.getStr(), request.getMessage());
	}
}
