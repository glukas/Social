package ch.ethz.inf.vs.android.glukas.project4.protocol;

import org.json.JSONObject;

import ch.ethz.inf.vs.android.glukas.project4.exceptions.UnknowRequestType;
import ch.ethz.inf.vs.android.glukas.project4.protocol.UserRequest.RequestType;

/**
 * Factory to create JSONObject
 */
public class JSONObjectFactory {
	
	public static JSONObject createJSONObject(UserRequest request) {
		
		if (request.getRequestType().equals(RequestType.CONNECT)){
			return createConnectObj(request);
		} else if (request.getRequestType().equals(RequestType.DISCONNECT)){
			return createDisconnectObj(request);
		} else if (request.getRequestType().equals(RequestType.FRIENDSHIP)){
			return createFriendshipObj(request);
		} else if (request.getRequestType().equals(RequestType.GET_WALL)){
			return createGetWallObj(request);
		} else if (request.getRequestType().equals(RequestType.SEARCH_USER)){
			return createSearchUserObj(request);
		} else if (request.getRequestType().equals(RequestType.SHOW_IMAGE)){
			return createShowImageObj(request);
		} else if (request.getRequestType().equals(RequestType.POST_PICTURE)){
			return createPostPictureObj(request);
		} else if (request.getRequestType().equals(RequestType.POST_TEXT)){
			return createPostTextObj(request);
		} else {
			try {
				throw new UnknowRequestType(request.getRequestType());
			} catch (UnknowRequestType e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	private static JSONObject createConnectObj(UserRequest request){
		return null;
	}
	
	private static JSONObject createDisconnectObj(UserRequest request){
		return null;
	}
	
	private static JSONObject createFriendshipObj(UserRequest request){
		return null;
	}
	
	private static JSONObject createGetWallObj(UserRequest request){
		return null;
	}
	
	private static JSONObject createSearchUserObj(UserRequest request){
		return null;
	}
	
	private static JSONObject createShowImageObj(UserRequest request){
		return null;
	}
	
	private static JSONObject createPostPictureObj(UserRequest request){
		return null;
	}
	
	private static JSONObject createPostTextObj(UserRequest request){
		return null;
	}
}
