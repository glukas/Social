package ch.ethz.inf.vs.android.glukas.project4.protocol.parsing;

import org.json.JSONException;
import org.json.JSONObject;
import ch.ethz.inf.vs.android.glukas.project4.exceptions.UnhandledFunctionnality;
import ch.ethz.inf.vs.android.glukas.project4.exceptions.UnknowRequestType;
import ch.ethz.inf.vs.android.glukas.project4.protocol.Message;
import ch.ethz.inf.vs.android.glukas.project4.protocol.Message.MessageType;

/**
 * Factory to create JSONObject. See package-info for more informations about definitions of messages
 */
public class JSONObjectFactory {	
	
	////
	// UserRequest
	//
	// the package-info gives more informations about how JSONObjects should be
	// defined
	////

	/**
	 * Create a JSONObject from an UserRequest to be used to send messages over
	 * the network
	 */
	public static JSONObject createJSONObject(Message request) {

		JSONObject obj = new JSONObject();
		
		try {
			// Friends
			if (request.getRequestType().equals(MessageType.SEARCH_USER)) {
				setSearchUserObj(request, obj);
			} else if (request.getRequestType().equals(MessageType.ACCEPT_FRIENDSHIP)) {
				setAcceptFriendshipObj(request, obj);
			} else if (request.getRequestType().equals(MessageType.REFUSE_FRIENDSHIP)) {
				setRefuseFriendshipObj(request, obj);
			} else if (request.getRequestType().equals(MessageType.ASK_FRIENDSHIP)) {
				setAskFriendshipObj(request, obj); 
			
				// Post new messages
			} else if (request.getRequestType().equals(MessageType.POST_PICTURE)) {
				setPostPictureObj(request, obj);
			} else if (request.getRequestType().equals(MessageType.POST_TEXT)) {
				setPostTextObj(request, obj);
			} else if (request.getRequestType().equals(MessageType.ACK_POST)) {
				setAckPostObj(request, obj);
	
				// Retrieve data
			} else if (request.getRequestType().equals(MessageType.GET_POSTS)) {
				setGetPostsObj(request, obj);
			} else if (request.getRequestType().equals(MessageType.SHOW_IMAGE)) {
				setShowImageObj(request, obj);
			} else if (request.getRequestType().equals(MessageType.SEND_PICTURE)) {
				setSendPictureObj(request, obj);
			} else if (request.getRequestType().equals(MessageType.SEND_TEXT)) {
				setSendTextObj(request, obj);
			} else if (request.getRequestType().equals(MessageType.GET_STATE)) {
				setGetStateObj(request, obj);
	
				// In case of Unknown message
			} else {
					throw new UnknowRequestType(request.getRequestType());
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return obj;
	}
	
	/**
	 * Create a JSONObject from an UserRequest to be used to send messages over
	 * the network. Number of messages is the number of messages of the local user
	 */
	public static JSONObject createJSONObject(Message request, int numberOfMessages) {
		
		JSONObject obj = new JSONObject();
		
		try {
			if (request.getRequestType().equals(MessageType.SEND_STATE)) {
				setSendStateObj(request, obj, numberOfMessages);
			} else {
				throw new UnknowRequestType(request.getRequestType());
			}
		} catch (JSONException ex) {
			ex.printStackTrace();
		}
		return obj;
	}

	////
	// Friends Message Setters
	////

	private static void setSearchUserObj(Message request, JSONObject obj) throws JSONException {
		try {
			throw new UnhandledFunctionnality();
		} catch (UnhandledFunctionnality e) {
			e.printStackTrace();
		}
	}

	private static void setAcceptFriendshipObj(Message request, JSONObject obj) throws JSONException {
		obj.put(Cmds.CMD.getStr(), Args.REP_FRIEND.getStr());
		obj.put(Cmds.RESPONSE.getStr(), Args.ACCEPT.getStr());
	}

	private static void setRefuseFriendshipObj(Message request, JSONObject obj) throws JSONException {
		obj.put(Cmds.CMD.getStr(), Args.REP_FRIEND.getStr());
		obj.put(Cmds.RESPONSE.getStr(), Args.REJECT.getStr());
	}

	private static void setAskFriendshipObj(Message request, JSONObject obj) throws JSONException {
		obj.put(Cmds.CMD.getStr(), Args.ASK_FRIEND.getStr());
		obj.put(Cmds.FROM.getStr(), request.getSender().getUsername());
	}

	////
	// Post Message Setters
	////

	private static void setPostPictureObj(Message request, JSONObject obj) throws JSONException {
		obj.put(Cmds.CMD.getStr(), Args.POST_PIC.getStr());
		obj.put(Cmds.ID.getStr(), request.getPostId());
		obj.put(Cmds.TEXT.getStr(), request.getMessage());
		obj.put(Cmds.PIC.getStr(), request.getHttpLink());
	}

	private static void setPostTextObj(Message request, JSONObject obj) throws JSONException {
		obj.put(Cmds.CMD.getStr(), Args.POST_TXT.getStr());
		obj.put(Cmds.ID.getStr(), request.getPostId());
		obj.put(Cmds.TEXT.getStr(), request.getMessage());
	}

	private static void setAckPostObj(Message request, JSONObject obj) throws JSONException {
		obj.put(Cmds.CMD.getStr(), Args.ACK.getStr());
		obj.put(Cmds.ID.getStr(), request.getPostId());
	}
	
	////
	// Retrieve Data Message Setters
	////
	
	private static void setSendTextObj(Message request, JSONObject obj) throws JSONException {
		obj.put(Cmds.CMD.getStr(), Args.SEND_TXT.getStr());
		obj.put(Cmds.ID.getStr(), request.getPostId());
		obj.put(Cmds.TEXT.getStr(), request.getMessage());
	}

	private static void setSendPictureObj(Message request, JSONObject obj) throws JSONException {
		obj.put(Cmds.CMD.getStr(), Args.POST_PIC.getStr());
		obj.put(Cmds.ID.getStr(), request.getPostId());
		obj.put(Cmds.TEXT.getStr(), request.getMessage());
		obj.put(Cmds.PIC.getStr(), request.getHttpLink());
	}
	
	private static void setGetStateObj(Message request, JSONObject obj) throws JSONException {
		obj.put(Cmds.CMD.getStr(), Args.GET_STATE.getStr());
	}

	private static void setSendStateObj(Message request, JSONObject obj, int numberofmessages) throws JSONException {
		obj.put(Cmds.CMD.getStr(), Args.SEND_STATE.getStr());
		obj.put(Cmds.ID.getStr(), request.getId());
		obj.put(Cmds.NUM_M.getStr(), numberofmessages);
	}

	private static void setGetPostsObj(Message request, JSONObject obj) throws JSONException {
		obj.put(Cmds.CMD.getStr(), Args.GET_POSTS.getStr());
		obj.put(Cmds.ID.getStr(), request.getId());
	}

	private static void setShowImageObj(Message request, JSONObject obj) {
		// not a JSON request, thus the object associated to this request is empty
	}
}
