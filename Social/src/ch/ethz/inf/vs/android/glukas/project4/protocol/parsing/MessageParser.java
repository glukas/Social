package ch.ethz.inf.vs.android.glukas.project4.protocol.parsing;

import org.json.JSONException;
import org.json.JSONObject;
import ch.ethz.inf.vs.android.glukas.project4.database.DatabaseAccess;
import ch.ethz.inf.vs.android.glukas.project4.protocol.Message;
import ch.ethz.inf.vs.android.glukas.project4.protocol.PublicHeader;
import ch.ethz.inf.vs.android.glukas.project4.protocol.StatusByte;

/**
 * Parse a message from the network into an abstraction usable by upper levels
 */
public class MessageParser {

	public static Message parseMessage(String message, PublicHeader header, DatabaseAccess db) {

		// Parse the header
		Message msg = new Message();
		msg.setSender(db.getFriend(header.getSender()));
		msg.setReceiver(db.getFriend(header.getReceiver()));
		msg.setPostId(header.getMessageId());
		
		// Get status of the message
		StatusByte statusByte = StatusByte.constructStatusByte(header.getConsistency());
		
		// Parse the content
		try {
			parseMessage(msg, message, statusByte);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return msg;
	}

	private static void parseMessage(Message msg, String messageTxt, StatusByte statusByte) throws JSONException{
		JSONObject obj = new JSONObject(messageTxt);
		String command = obj.getString(Cmds.CMD.getStr());
		
		// Server
		if (command.equals(Args.CONNECT.getStr())){
			
		} else if (command.equals(Args.DISCONNECT.getStr())){
			
		}
		
		// Friendship
		else if (command.equals(Args.REP_FRIEND.getStr())) {
			
		} else if (command.equals(Args.ASK_FRIEND.getStr())) {
			
		}
		
		// State
		else if (command.equals(Args.GET_STATE.getStr())) {
			
		} else if (command.equals(Args.SEND_STATE.getStr())) {
			
		} 
		
		// Data retrieving
		else if (command.equals(Args.GET_POSTS.getStr())) {
			
		} else if (command.equals(Args.SEND_PIC.getStr())) {
			
		} else if (command.equals(Args.SEND_TXT.getStr())) {
			
		}
		
		// Post
		else if (command.equals(Args.POST_PIC.getStr())) {
			
		} else if (command.equals(Args.POST_TXT.getStr())) {
			
		}
		
		// Broadcast & ACK
		else if (command.equals(Args.BROADCAST.getStr())) {
			
		} else if (command.equals(Args.ACK.getStr())) {
			
		}
		
		//Unknown
		else {
			
		}
	}
}
