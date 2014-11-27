package ch.ethz.inf.vs.android.glukas.project4.protocol.parsing;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import ch.ethz.inf.vs.android.glukas.project4.database.DatabaseAccess;
import ch.ethz.inf.vs.android.glukas.project4.exceptions.ProtocolException;
import ch.ethz.inf.vs.android.glukas.project4.protocol.Message;
import ch.ethz.inf.vs.android.glukas.project4.protocol.Message.MessageType;
import ch.ethz.inf.vs.android.glukas.project4.protocol.MessageFactory;
import ch.ethz.inf.vs.android.glukas.project4.protocol.PublicHeader;
import ch.ethz.inf.vs.android.glukas.project4.protocol.StatusByte;

/**
 * Parse a message from the network into an abstraction usable by upper levels
 */
public class MessageParser {

	public static Message parseMessage(String message, PublicHeader header, DatabaseAccess db) {

		// Parse the header
		Message msg = MessageFactory.newEmptyMessage();
		msg.setSender(db.getFriend(header.getSender()));
		msg.setReceiver(db.getFriend(header.getReceiver()));
		msg.setPostId(header.getMessageId());
		
		Log.i("DEBUG", "###"+"Header parsed");
		
		// Get status of the message
		StatusByte statusByte = StatusByte.constructStatusByte(header.getConsistency());
		
		// Parse the content
		try {
			parseMessage(msg, message, statusByte);
		} catch (JSONException e) {
			Log.i("DEBUG", "###"+"JSON exception");
			e.printStackTrace();
		}

		return msg;
	}

	private static void parseMessage(Message msg, String messageTxt, StatusByte statusByte) throws JSONException{
		JSONObject obj = new JSONObject(messageTxt);
		String command = obj.getString(Cmds.CMD.getStr());
		
		// Server
		if (command.equals(Args.CONNECT.getStr())){
			//empty message, only header is important
		} else if (command.equals(Args.DISCONNECT.getStr())){
			//empty message, only header is important
		}
		
		// Friendship
		else if (command.equals(Args.REP_FRIEND.getStr())) {
			parseFriendReply(msg, obj);
		} else if (command.equals(Args.ASK_FRIEND.getStr())) {
			parseFriendDemand(msg, obj);
		}
		
		// State
		else if (command.equals(Args.GET_STATE.getStr())) {
			//empty message, only header is important
		} else if (command.equals(Args.SEND_STATE.getStr())) {
			parseSendState(msg, obj);
		} 
		
		// Data retrieving
		else if (command.equals(Args.GET_POSTS.getStr())) {
			Log.i("DEBUG", "###"+"GET_POST ENTER");
			parseGetPosts(msg, obj);
		} else if (command.equals(Args.SEND_PIC.getStr())) {
			parseSendPic(msg, obj);
		} else if (command.equals(Args.SEND_TXT.getStr())) {
			parseSendText(msg, obj);
		}
		
		// Post
		else if (command.equals(Args.POST_PIC.getStr())) {
			parsePostPic(msg, obj);
		} else if (command.equals(Args.POST_TXT.getStr())) {
			parsePostText(msg, obj);
		}
		
		// Broadcast & ACK
		else if (command.equals(Args.ACK.getStr())) {
			parseAck(msg, obj);
		}
		
		//Unknown
		else {
			parseUnknown(msg, obj);
		}
	}
	
	private static void parsePostPic(Message msg, JSONObject obj) throws JSONException {
		int id = obj.getInt(Cmds.ID.getStr());
		String text = obj.getString(Cmds.TEXT.getStr());
		String link = obj.getString(Cmds.PIC.getStr());
		msg.setRequestType(MessageType.POST_PICTURE);
		msg.setId(id);
		msg.setMessage(text);
		msg.setHttpLink(link);
	}
	
	private static void parsePostText(Message msg, JSONObject obj) throws JSONException {
		int id = obj.getInt(Cmds.ID.getStr());
		String text = obj.getString(Cmds.TEXT.getStr());
		msg.setRequestType(MessageType.POST_TEXT);
		msg.setId(id);
		msg.setMessage(text);
	}
	
	private static void parseAck(Message msg, JSONObject obj) throws JSONException {
		int id = obj.getInt(Cmds.ID.getStr());
		msg.setRequestType(MessageType.ACK_POST);
		msg.setId(id);
	}
	
	private static void parseUnknown(Message msg, JSONObject obj) throws JSONException {
		msg.setRequestType(MessageType.UNKOWN);
	}
	
	private static void parseGetPosts(Message msg, JSONObject obj) throws JSONException {
		int id = obj.getInt(Cmds.ID.getStr());
		msg.setRequestType(MessageType.GET_POSTS);
		msg.setId(id);
	}
	
	private static void parseSendPic(Message msg, JSONObject obj) throws JSONException {
		int id = obj.getInt(Cmds.ID.getStr());
		String text = obj.getString(Cmds.TEXT.getStr());
		String link = obj.getString(Cmds.PIC.getStr());
		msg.setRequestType(MessageType.SEND_PICTURE);
		msg.setId(id);
		msg.setMessage(text);
		msg.setHttpLink(link);
	}
	
	private static void parseSendText(Message msg, JSONObject obj) throws JSONException {
		int id = obj.getInt(Cmds.ID.getStr());
		String text = obj.getString(Cmds.TEXT.getStr());
		msg.setRequestType(MessageType.SEND_TEXT);
		msg.setId(id);
		msg.setMessage(text);
	}
	
	
	private static void parseFriendReply(Message msg, JSONObject obj) throws JSONException {
		String response = obj.getString(Cmds.RESPONSE.getStr());
		if (response.equals(Args.ACCEPT.getStr())){
			msg.setRequestType(MessageType.ACCEPT_FRIENDSHIP);
		} else if (response.equals(Args.REJECT.getStr())) {
			msg.setRequestType(MessageType.REFUSE_FRIENDSHIP);
		} else {
			msg = null;
			try {
				throw new ProtocolException();
			} catch (ProtocolException e) {
				e.printStackTrace();
			}
		}
	}
	
	private static void parseFriendDemand(Message msg, JSONObject obj) throws JSONException {
		String username = obj.getString(Cmds.FROM.getStr());
		msg.setRequestType(MessageType.ASK_FRIENDSHIP);
		msg.setMessage(username);
	}
	
	private static void parseSendState(Message msg, JSONObject obj) throws JSONException {
		int maxId = obj.getInt(Cmds.ID.getStr());
		int numMsgs = obj.getInt(Cmds.NUM_M.getStr());
		msg.setRequestType(MessageType.SEND_STATE);
		msg.setId(maxId);
		msg.setNumM(numMsgs);
	}
}
