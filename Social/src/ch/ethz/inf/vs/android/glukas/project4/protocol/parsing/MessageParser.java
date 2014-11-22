package ch.ethz.inf.vs.android.glukas.project4.protocol.parsing;

import org.json.JSONException;
import org.json.JSONObject;

import ch.ethz.inf.vs.android.glukas.project4.protocol.Message.MessageType;
import ch.ethz.inf.vs.android.glukas.project4.protocol.Message;
import ch.ethz.inf.vs.android.glukas.project4.protocol.PublicHeader;

/**
 * Parse a message from the network into an abstraction usable by upper levels
 */
public class MessageParser {

	public static Message parseMessage(String message, PublicHeader header) {

		JSONObject obj = null;
		Message msg = null;
		try {
			obj = new JSONObject(message);
			msg = new Message(parseMessageType(
					obj.getString(Cmds.CMD.getStr()),
					obj.getString(Cmds.RESPONSE.getStr())));
			parseHeader(msg, header);
			fillMessage(msg, obj);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return msg;
	}

	private static MessageType parseMessageType(String type, String response) {

		// TODO : Check if its complete

		// Server
		if (type.equals(Args.CONNECT.getStr())) {
			return MessageType.CONNECT;
		} else if (type.equals(Args.DISCONNECT.getStr())) {
			return MessageType.DISCONNECT;

			// Friends
		} else if (type.equals(Args.BROADCAST.getStr())) {
			return MessageType.BROADCAST;
		} else if (type.equals(Args.ASK_FRIEND.getStr())) {
			return MessageType.ASK_FRIENDSHIP;

		} else if (type.equals(Args.REP_FRIEND.getStr())) {
			if (response.equals(Args.ACCEPT.getStr()))
				return MessageType.ACCEPT_FRIENDSHIP;
			else
				return MessageType.REFUSE_FRIENDSHIP;

		/*} else if (type.equals(Args.SEARCH.getStr())) {
			return MessageType.SEARCH_USER;
		*/
			// Post new message

		} else if (type.equals(Args.POST_PIC.getStr())) {
			return MessageType.POST_PICTURE;
		} else if (type.equals(Args.POST_TXT.getStr())) {
			return MessageType.POST_TEXT;
		} else if (type.equals(Args.ACK.getStr())) {
			return MessageType.ACK_POST;

			// Retrieve data
		} else if (type.equals(Args.GET_POSTS.getStr())) {
			return MessageType.GET_POSTS;
		} else if (type.equals(Args.GET_STATE.getStr())) {
			return MessageType.GET_STATE;
		} else if (type.equals(Args.SEND_STATE.getStr())) {
			return MessageType.SEND_STATE;
		} else if (type.equals(Args.SEND_PIC.getStr())) {
			return MessageType.SEND_PICTURE;

			// Unknown
		} else {
			return MessageType.UNKOWN;
		}
	}

	private static void parseHeader(Message msg, PublicHeader header) {
		msg.setUsernameSender(header.getSender().getId().toString());
		msg.setUsernameReceiver(header.getReceiver().getId().toString());
		msg.setPostId(header.getMessageId());

	}

	private static void fillMessage(Message msg, JSONObject obj) {
		MessageType type = msg.getMessageType();
		if (type.equals(MessageType.POST_TEXT)) {
			try {
				msg.setMessage(obj.getString(Cmds.TEXT.getStr()));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (type.equals(MessageType.POST_PICTURE)) {
			try {
				msg.setHttpLink(obj.getString(Cmds.PIC.getStr()));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (type.equals(MessageType.ACCEPT_FRIENDSHIP)) {

			msg.setFriendshipResponse(Args.ACCEPT.getStr());

		} else if (type.equals(MessageType.REFUSE_FRIENDSHIP)) {

			msg.setFriendshipResponse(Args.REJECT.getStr());
		}

	}
}
