package ch.ethz.inf.vs.android.glukas.project4.protocol;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.graphics.Bitmap;
import android.util.Log;
import ch.ethz.inf.vs.android.glukas.project4.Post;
import ch.ethz.inf.vs.android.glukas.project4.BasicUser;
import ch.ethz.inf.vs.android.glukas.project4.User;
import ch.ethz.inf.vs.android.glukas.project4.UserDelegate;
import ch.ethz.inf.vs.android.glukas.project4.UserId;
import ch.ethz.inf.vs.android.glukas.project4.Wall;
import ch.ethz.inf.vs.android.glukas.project4.database.DatabaseAccess;
import ch.ethz.inf.vs.android.glukas.project4.exceptions.DatabaseException;
import ch.ethz.inf.vs.android.glukas.project4.exceptions.UnhandledFunctionnality;
import ch.ethz.inf.vs.android.glukas.project4.networking.MessageRelay;
import ch.ethz.inf.vs.android.glukas.project4.protocol.Message.MessageType;
import ch.ethz.inf.vs.android.glukas.project4.protocol.Message;
import ch.ethz.inf.vs.android.glukas.project4.protocol.parsing.JSONObjectFactory;
import ch.ethz.inf.vs.android.glukas.project4.protocol.parsing.MessageParser;
import ch.ethz.inf.vs.android.glukas.project4.security.DBCredentialStorage;
import ch.ethz.inf.vs.android.glukas.project4.security.SecureChannel;
import ch.ethz.inf.vs.android.glukas.project4.security.SecureChannelDelegate;
import ch.ethz.inf.vs.android.glukas.project4.security.NetworkMessage;

/**
 * This is the main part of the package, where most of the protocol is
 * implemented.
 * 
 * It linked together request from the user, through implementing the
 * UserDelegate. On an other hand, it handles calls back from the network,
 * through implementing SecureChannelDelegate.
 */
public class Protocol implements ProtocolInterface, SecureChannelDelegate {

	////
	// Life cycle
	////

	private static Protocol instance;

	/**
	 * Get a instance of Protocol. Don't forget to call 'setProtocol' if it's the first time
	 */
	public static Protocol getInstance(DatabaseAccess db) {
		if (instance == null) {
			return new Protocol(db);
		} else {
			return instance;
		}
	}
	
	public Protocol(DatabaseAccess db) {
		database = db;
		localUser = database.getUser();
		secureChannel = new SecureChannel("winti.mooo.com", 9000, new DBCredentialStorage(db));
		secureChannel.setDelegate(this);
		messageRelay = new MessageRelay(secureChannel);
		List<User> users = database.getUserFriendsList();
		userMapping = new HashMap<UserId, User>();
		for (User user : users) {
			userMapping.put(user.getId(), user);
		}
		if (localUser != null) {
			userMapping.put(localUser.getId(), localUser);
		}
	}

	////
	// Members
	////

	// Communications with other components
	private SecureChannel secureChannel;
	private MessageRelay messageRelay;
	private UserDelegate userHandler;
	private DatabaseAccess database;

	//caching
	private User localUser;
	private Map<UserId, User> userMapping;
	
	//exceptional behaviors
	private final String unexpectedMsg = "Unexpected message arrived : ";

	////
	// ProtocolInterface
	////
	
	public int getNewPostId(UserId userId) {
		return database.getFriendMaxPostsId(userId)+1;
	}
	
	public Wall getUserWall() {
		return database.getUserWall();
	}
	
	@Override
	public User getUser() {
		return database.getUser();
	}

	@Override
	public void putUser(User user) {
		database.putUser(user);
		localUser = user;
		userMapping.put(user.getId(), user);
	}
	
	@Override
	public void putFriend(User friend) {
		database.putFriend(friend);
		userMapping.put(friend.getId(), friend);
	}
	
	@Override
	public User getFriend(UserId id) {
		return database.getFriend(id);
	}
	
	@Override
	public Map<UserId, User> getUserMapping() {
		return Collections.unmodifiableMap(userMapping);
	}

	@Override
	public void connect() {
		messageRelay.connect(localUser.getId());
	}

	@Override
	public void disconnect() {
		messageRelay.disconnect(localUser.getId());
	}
	
	@Override
	public void post(UserId wallOwner, String text, Bitmap image) {
		Post post = new Post(getNewPostId(wallOwner), localUser.getId(), wallOwner, text, image, new Date());
		postLocally(post);
		
		Message msg = MessageFactory.newPostMessage(post, localUser.getId(), post.getWallOwner(), false);
		PublicHeader header = new PublicHeader(0, null, StatusByte.POST.getByte(), post.getId(), localUser.getId(), post.getWallOwner());
		secureChannel.sendMessage(new NetworkMessage(JSONObjectFactory.createJSONObject(msg).toString(), header));
		
	}
	
	@Override
	public void post(String text, Bitmap image) {
		post(localUser.getId(), text, image);
	}
	
	private void postLocally(Post post) {
		Log.d(this.getClass().toString(), "postLocally : " + post.getText() + " , " + post.getPoster().getId() + " , " + post.getWallOwner().getId());
		database.putPost(post);
		int msgId = post.getId();
		int maxNumPosts = database.getFriendPostsCount(post.getWallOwner())+1;
		database.setFriendMaxPostsId(msgId, post.getWallOwner());
		database.setFriendPostsCount(maxNumPosts, post.getWallOwner());
		userHandler.onPostReceived(post);
	}

	@Override
	public void getUserWall(UserId userId) {
		getUserPosts(userId, 0);
	}
	
	@Override
	public void getUserPosts(UserId userId, int postId) {
		//ask distant user if already all messages are in database
		Message msg = MessageFactory.newTypeMessage(MessageType.GET_STATE);
		PublicHeader header = new PublicHeader(0, null, StatusByte.DATA.getByte(), 0, localUser.getId(), userId);
		secureChannel.sendMessage(new NetworkMessage(JSONObjectFactory.createJSONObject(msg, 0).toString(), header));
		//retrieve data already known from database
		database.getAllFriendPostsFrom(userId, postId);
	}
	
	@Override
	public void getSomeUserPosts(UserId userId, int numberPosts) {
		getSomeUserPosts(userId, numberPosts, Integer.MAX_VALUE);
	}
	
	@Override
	public void getSomeUserPosts(UserId userId, int numberPosts, int postId) {
		//retrieve posts already in the database
		Log.d(this.getClass().toString(), "getSomeUserPosts " + userId.toString());
		List<Post> listPosts = database.getSomeLatestPosts(userId, numberPosts, postId);
		
		for (Post p : listPosts) {
			userHandler.onPostReceived(p);
		}
		
		//ask for update
		Message msg = MessageFactory.newTypeMessage(MessageType.GET_STATE);
		PublicHeader header = new PublicHeader(0, null, StatusByte.DATA.getByte(), 0, localUser.getId(), userId);
		secureChannel.sendMessage(new NetworkMessage(JSONObjectFactory.createJSONObject(msg).toString(), header));
	}

	@Override
	public void setDelegate(UserDelegate delegate) {
		userHandler = delegate;
	}

	////
	// SecureChannelDelegate
	////

	@Override
	public void onMessageReceived(NetworkMessage message) {
		
		StatusByte status = StatusByte.constructStatusByte(message.header.getConsistency());
		Log.d(this.getClass().toString(), "header received : " + status.name());
		//react to an incoming message
		
		if (message.text.length == 0) {//
			onHeaderReceived(message.header);
		} else {

			Message msg = MessageParser.parseMessage(message.getText(), message.header);

			MessageType type = msg.getRequestType();

			if (type.equals(MessageType.POST_PICTURE)) {// Post new messages
				onPostPictureReceived(msg);
			} else if (type.equals(MessageType.POST_TEXT)) {
				onPostTextReceived(msg);
			} else if (type.equals(MessageType.ACK_POST)) {
				onAckPostReceived(msg);
				
			} else if (type.equals(MessageType.GET_POSTS)) {// Retrieve data
				onGetPostsReceived(msg);
			} else if (type.equals(MessageType.SHOW_IMAGE)) {
				onShowImageReceived(msg);
			} else if (type.equals(MessageType.SEND_PICTURE)) {
				onSendPictureReceived(msg);
			} else if (type.equals(MessageType.SEND_TEXT)) {
				onSendTextReceived(msg);
			} else if (type.equals(MessageType.SEND_STATE)) {
				onSendStateReceived(msg);
			} else if (type.equals(MessageType.GET_STATE)) {
				onGetStateReceived(msg);
			}
		}

		/*	// Friends
		if (type.equals(MessageType.SEARCH_USER)) {
			onSearchUserReceived(msg);
		} else if (type.equals(MessageType.ACCEPT_FRIENDSHIP)) {
			onAcceptFriendshipReceived(msg);
		} else if (type.equals(MessageType.REFUSE_FRIENDSHIP)) {
			onRefuseFriendshipReceived(msg);
		} else if (type.equals(MessageType.ASK_FRIENDSHIP)) {
			onAskFriendshipReceived(msg);
		}*/
	

	}
	
	private void onHeaderReceived(PublicHeader header) {

	}

	private void onPostPictureReceived(Message msg) {
		//for the moment, we react exactly the same as on text received
		onPostReceived(msg);
	}
	
	private void onPostTextReceived(Message msg) {
		//for the moment, we react exactly the same as on picture received
		onPostReceived(msg);
	}
	
	private void onPostReceived(Message msg) {
		//A friend posted a post onto the wall's local user
		if (!msg.sender.equals(localUser.getId()) && msg.receiver.equals(localUser.getId())) {
			int msgId = database.getFriendMaxPostsId(localUser.getId());
			Post post = new Post(msg, msgId);
			database.putPost(post);
			database.setUserMaxPostsId(post.getId());
			userHandler.onPostReceived(post);
		}
	}
	
	private void onAckPostReceived(Message msg) {
		//for the moment, the model ensures that the post will be posted, thus
		//it can be silently ignored
	}
	
	private void onGetPostsReceived(Message msg) {
		List<Post> listPosts = database.getAllFriendPostsFrom(localUser.getId(), msg.getId());
		for (Post post : listPosts){
			String msgTxt = JSONObjectFactory.createJSONObject(MessageFactory.newPostMessage(post, localUser.getId(), msg.getSender(), true)).toString();
			PublicHeader header = new PublicHeader(0, null, StatusByte.SEND.getByte(), post.getId(), localUser.getId(), msg.getSender());
			NetworkMessage networkMsg = new NetworkMessage(msgTxt, header);
			secureChannel.sendMessage(networkMsg);
		}
	}
	
	private void onShowImageReceived(Message msg) {
		//TODO : future use
	}
	
	private void onSendPictureReceived(Message msg) {
		//for the moment, we react exactly the same as on text received
		onSendReceived(msg);
	}
	
	private void onSendTextReceived(Message msg) {
		//for the moment, we react exactly the same as on picture received
		onSendReceived(msg);
	}
	
	private void onSendReceived(Message msg) {
		//A friend send a post to the local user
		Post post = new Post(msg, msg.getPostId());
		database.putPost(post);
		userHandler.onPostReceived(post);
	}
	
	private void onSendStateReceived(Message msg) {
		//Someone send his / her state, so retrieve data from the message
		UserId friendId = msg.getSender();
		int numMsgs = msg.getNumM();
		int maxPostId = msg.getId();
		
		//ask to have posts that local user doesn't have in database
		int oldMaxPostId = database.getFriendMaxPostsId(friendId);
		int oldNumMsgs = database.getFriendPostsCount(friendId);
		if (oldNumMsgs < numMsgs) {
			Message msgToSend = MessageFactory.newGetPostsMessage(oldMaxPostId, localUser.getId(), msg.getSender());
			PublicHeader header = new PublicHeader(0, null, StatusByte.SEND.getByte(), 0, localUser.getId(), friendId);
			NetworkMessage networkMessage = new NetworkMessage(JSONObjectFactory.createJSONObject(msgToSend).toString(), header);
			secureChannel.sendMessage(networkMessage);
		}
		
		//update database
		database.setFriendMaxPostsId(maxPostId, friendId);
		database.setFriendPostsCount(numMsgs, friendId);
	}
	
	private void onGetStateReceived(Message msg) {
		//Someone wants user state, so retrieve data from database
		int maxPostId = database.getFriendMaxPostsId(localUser.getId());
		int maxNumMsgs = database.getFriendMaxPostsId(localUser.getId());
		
		//create message encapsulating all informations
		UserId userToSend = msg.getSender();
		String msgToSend = JSONObjectFactory.createJSONObject(MessageFactory.newSendStateMessage(localUser.getId(), userToSend, maxPostId, maxNumMsgs)).toString();
		PublicHeader header = new PublicHeader(0, null, StatusByte.SEND.getByte(), 0, localUser.getId(), userToSend);
		
		//send reply
		secureChannel.sendMessage(new NetworkMessage(msgToSend, header));
	}
	
	//unexpected messages
	
	private void onAcceptFriendshipReceived(Message msg) {
		//We use NFC for the moment
		Log.d(getClass().toString(), unexpectedMsg+msg.toString());
	}
	
	private void onRefuseFriendshipReceived(Message msg) {
		//We use NFC for the moment
		Log.d(getClass().toString(), unexpectedMsg+msg.toString());
	}
	
	private void onAskFriendshipReceived(Message msg) {
		//We use NFC for the moment
		Log.d(getClass().toString(), unexpectedMsg+msg.toString());
	}
	
	private void onSearchUserReceived(Message msg) {
		//User should not be targeted by these kind of messages
		Log.d(getClass().toString(), unexpectedMsg+msg.toString());
	}

}
