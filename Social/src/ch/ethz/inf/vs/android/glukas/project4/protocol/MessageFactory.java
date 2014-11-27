package ch.ethz.inf.vs.android.glukas.project4.protocol;

import ch.ethz.inf.vs.android.glukas.project4.BasicUser;
import ch.ethz.inf.vs.android.glukas.project4.Post;
import ch.ethz.inf.vs.android.glukas.project4.Post.PostType;
import ch.ethz.inf.vs.android.glukas.project4.protocol.Message.MessageType;

/**
 * Helper to create Messages
 */
public class MessageFactory {
	
	/*
	 * Fields in Message (in order of creation)
	protected User sender;
	protected User receiver;
	protected int postId;
	protected MessageType requestType;
	
	protected String httpLink;
	protected String message;
	protected String username;
	protected int id;
	protected int numM;
	 */
	
	/**
	 * New post Message
	 * @param post
	 * @param sender
	 * @param receiver
	 * @param isSend
	 * @return
	 */
	public static Message newPostMessage(Post post, BasicUser sender, BasicUser receiver, boolean isSend) {
		if (isSend) {
			if (post.getType().equals(PostType.PICTURE)){
				return new Message(sender, receiver, post.getId(), MessageType.SEND_PICTURE, post.getImageLink(), post.getText(), "", post.getId(), 0);
			} else {
				return new Message(sender, receiver, post.getId(), MessageType.SEND_TEXT, "", "", "", post.getId(), 0);
			}
		} else {
			if (post.getType().equals(PostType.PICTURE)){
				return new Message(sender, receiver, post.getId(), MessageType.POST_PICTURE, post.getImageLink(), post.getText(), "", post.getId(), 0);
			} else {
				return new Message(sender, receiver, post.getId(), MessageType.POST_TEXT, "", "", "", post.getId(), 0);
			}
		}
	}
	
	/**
	 * New send state Message
	 * @param sender
	 * @param receiver
	 * @param id
	 * @param numM
	 * @return
	 */
	public static Message newSendStateMessage(BasicUser sender, BasicUser receiver, int id, int numM) {
		return new Message(sender, receiver, 0, MessageType.SEND_STATE, "", "", "", id, numM);
	}
	
	/**
	 * New get posts Message
	 * @param oldCountPost
	 * @param sender
	 * @param receiver
	 * @return
	 */
	public static Message newGetPostsMessage(int oldCountPost, BasicUser sender, BasicUser receiver) {
		return new Message(sender, receiver, 0, MessageType.GET_POSTS, "", "", "", oldCountPost, 0);
	}
	
	/**
	 * New empty Message
	 * @return
	 */
	public static Message newEmptyMessage() {
		return new Message(null, null, 0, MessageType.UNKOWN, "", "", "", 0, 0);
	}
	
	/**
	 * New only typed message
	 * @param type
	 * @return
	 */
	public static Message newTypeMessage(MessageType type) {
		return new Message(null, null, 0, type, "", "", "", 0, 0);
	}
}
