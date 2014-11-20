package ch.ethz.inf.vs.android.glukas.project4.protocol.parsing;

import org.json.JSONException;
import org.json.JSONObject;

import ch.ethz.inf.vs.android.glukas.project4.protocol.Message.MessageType;
import ch.ethz.inf.vs.android.glukas.project4.protocol.NetworkMessage;
import ch.ethz.inf.vs.android.glukas.project4.protocol.PublicHeader;

/**
 * Parse a message from the network into an abstraction usable by upper levels
 */
public class MessageParser {
	
	public static NetworkMessage parseMessage(String message, PublicHeader header) {
		
		JSONObject obj = null;
		NetworkMessage msg = null;
		try {
			 obj = new JSONObject(message);
			 msg = new NetworkMessage(parseMessageType(obj.getString(Cmds.CMD.getStr())));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return msg;
	}
	
	private static MessageType parseMessageType(String type){
		
		if (type.equals(Args.ACK.getStr())){
			return MessageType.ACK;
		} else if (type.equals(Args.BROADCAST.getStr())) {
			return MessageType.BROADCAST;
		} else if (type.equals(Args.CONNECT.getStr())) {
			return MessageType.CONNECT;
		} else if (type.equals(Args.DISCONNECT.getStr())) {
			return MessageType.DISCONNECT;
		} else if (type.equals(Args.DEM_FRIEND.getStr())) {
			return MessageType.FRIENDSHIP;
		} else if (type.equals(Args.GET_WALL.getStr())) {
			return MessageType.GET_WALL;
		} else if (type.equals(Args.POST_PIC.getStr())) {
			return MessageType.POST_PICTURE;
		} else if (type.equals(Args.POST_TXT.getStr())) {
			return MessageType.POST_TEXT;
		} else {
			return MessageType.UNKOWN;
		}
	}
}
