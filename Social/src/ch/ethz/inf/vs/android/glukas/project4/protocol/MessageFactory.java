package ch.ethz.inf.vs.android.glukas.project4.protocol;

import ch.ethz.inf.vs.android.glukas.project4.Post;
import ch.ethz.inf.vs.android.glukas.project4.UserId;
import ch.ethz.inf.vs.android.glukas.project4.database.Utility;
import ch.ethz.inf.vs.android.glukas.project4.protocol.Message.MessageType;

/**
 * Helper to create Messages
 */
public class MessageFactory {
	
	/**
	 * New post Message
	 * @param post
	 * @param sender
	 * @param receiver
	 * @param isSend
	 * @return
	 */
	public static Message newPostMessage(Post post, boolean isSend) {
		Message msg;
		if (isSend) {
			msg = new Message(post.getPoster(), post.getWallOwner(), post.getId(), MessageType.SEND_TEXT, post.getText(), "", post.getId(), 0);
		} else {
			msg = new Message(post.getPoster(), post.getWallOwner(), post.getId(), MessageType.POST_TEXT, post.getText(), "", post.getId(), 0);
		}
		if (post.getImage() != null) {
			msg.setPayload(Utility.toByteArray(post.getImage()));
		} else {
			msg.setPayload(new byte[0]);
		}
		return msg;
	}
	
	/**
	 * New send state Message
	 * @param sender
	 * @param receiver
	 * @param id
	 * @param numM
	 * @return
	 */
	public static Message newSendStateMessage(UserId sender, UserId receiver, int id, int numM) {
		return new Message(sender, receiver, 0, MessageType.SEND_STATE, "", "", id, numM);
	}
	
	/**
	 * New get state Message
	 * @param sender
	 * @param receiver
	 * @return
	 */
	public static Message newGetStateMessage(UserId sender, UserId receiver) {
		return new Message(sender, receiver, 0, MessageType.GET_STATE, "", "", 0, 0);
	}
	
	/**
	 * New ack Message
	 * @param sender
	 * @param receiver
	 * @return
	 */
	public static Message newAckMessage(UserId sender, UserId receiver) {
		return new Message(sender, receiver, 0, MessageType.ACK_POST, "", "", 0, 0);
	}
	
	/**
	 * New get posts Message
	 * @param oldCountPost
	 * @param sender
	 * @param receiver
	 * @return
	 */
	public static Message newGetPostsMessage(int oldCountPost, UserId sender, UserId receiver) {
		return new Message(sender, receiver, 0, MessageType.GET_POSTS, "", "", oldCountPost, 0);
	}
	
	/**
	 * New empty Message
	 * @return
	 */
	public static Message newEmptyMessage() {
		return new Message(null, null, 0, MessageType.UNKOWN, "", "", 0, 0);
	}
	
	/**
	 * New only typed message
	 * @param type
	 * @return
	 */
	public static Message newTypeMessage(MessageType type) {
		return new Message(null, null, 0, type, "", "", 0, 0);
	}
}
