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
		userMapping = new HashMap<UserId, User>();
		refreshUserMapping();
	}
	
	private void refreshUserMapping() {
		List<User> users = database.getUserFriendsList();
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
		refreshUserMapping();
		//userMapping.put(user.getId(), user);
	}
	
	@Override
	public void putFriend(User friend) {
		database.putFriend(friend);
		refreshUserMapping();
		//userMapping.put(friend.getId(), friend);
		//Log.d(this.getClass().toString(), userMapping.toString());
	}
	
	@Override
	public User getFriend(UserId id) {
		return database.getFriend(id);
	}
	
	@Override
	public Map<UserId, User> getUserMapping() {
		refreshUserMapping();
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
		boolean success = postLocally(post);
		if (success) {
			Message msg = MessageFactory.newPostMessage(post, false);
			PublicHeader header = new PublicHeader(0, null, StatusByte.POST.getByte(), post.getId(), localUser.getId(), post.getWallOwner());
			secureChannel.sendMessage(new NetworkMessage(JSONObjectFactory.createJSONObject(msg).toString(), header));
		}
	}
	
	@Override
	public void post(String text, Bitmap image) {
		post(localUser.getId(), text, image);
	}
	
	private boolean postLocally(Post post) {
		boolean success = database.putPost(post);
		if (success) {
			Log.d(this.getClass().toString(), "postLocally : " + post.getText() + " , " + post.getPoster().getId() + " , " + post.getWallOwner().getId());
			int localNumberOfPosts = database.getFriendPostsCount(post.getWallOwner());
			int localTimestamp = database.getFriendMaxPostsId(post.getWallOwner());
			database.setFriendMaxPostsId(Math.max(post.getId(), localTimestamp), post.getWallOwner());
			database.setFriendPostsCount(localNumberOfPosts+1, post.getWallOwner());
			userHandler.onPostReceived(post);
		}
		return success;
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
		PublicHeader request = new PublicHeader(PublicHeader.BYTES_LENGTH_HEADER, null, StatusByte.DATA.getByte(), 0, localUser.getId(), userId);
		secureChannel.sendMessage(new NetworkMessage(JSONObjectFactory.createJSONObject(msg).toString(), request));
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
		//react to an incoming message
		if (message.text.length == 0) {
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
			} else {
				Log.e(this.getClass().toString(), this.unexpectedMsg);
			}
		}

	}
	
	private void onHeaderReceived(PublicHeader header) {
		if (header.getConsistency() == StatusByte.CONNECT.getByte()) {
		}
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
		if (!msg.sender.equals(localUser.getId())) {
			Post post = new Post(msg);
			Log.d(this.getClass().toString(), "post : " + post.toString());
			postLocally(post);
		}
	}
	
	private void onAckPostReceived(Message msg) {
		//for the moment, the model ensures that the post will be posted, thus
		//it can be silently ignored
	}
	
	//This gets called if some user wants to get the wall of the local user
	//the msg specifies what the last seen id is.
	private void onGetPostsReceived(Message msg) {
		List<Post> listPosts = database.getAllFriendPostsFrom(localUser.getId(), msg.getId());
		for (Post post : listPosts){
			String msgTxt = JSONObjectFactory.createJSONObject(MessageFactory.newPostMessage(post, true)).toString();
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
		throw new UnhandledFunctionnality();
		//onSendReceived(msg);
	}
	
	//This gets called when an request for posts on some distant wall has been answered
	private void onSendTextReceived(Message msg) {
		onPostReceived(msg);
	}
	
	private void onSendReceived(Message msg) {
		//A friend send a post to the local user
		Post post = new Post(msg);
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
		
		Log.d(this.getClass().toString(), "maxId:"+ oldMaxPostId + ", count:" + oldNumMsgs);
		
		if (oldNumMsgs < numMsgs) {
			//TODO think about if this really guarantees that no messages are lost
			Message msgToSend = MessageFactory.newGetPostsMessage(oldMaxPostId, localUser.getId(), msg.getSender());
			PublicHeader header = new PublicHeader(0, null, StatusByte.SEND.getByte(), 0, localUser.getId(), friendId);
			NetworkMessage networkMessage = new NetworkMessage(JSONObjectFactory.createJSONObject(msgToSend).toString(), header);
			secureChannel.sendMessage(networkMessage);
		}
		
	}
	
	private void onGetStateReceived(Message msg) {
		
		Log.d(this.getClass().toString(), "onGetStateReceived");
		
		//Someone wants user state, so retrieve data from database
		int maxPostId = database.getUserMaxPostsId();
		int maxNumMsgs = database.getUserPostsCount();
		
		//create message encapsulating all informations
		UserId userToSend = msg.getSender();
		String msgToSend = JSONObjectFactory.createJSONObject(MessageFactory.newSendStateMessage(localUser.getId(), userToSend, maxPostId, maxNumMsgs), maxNumMsgs).toString();
		PublicHeader header = new PublicHeader(0, null, StatusByte.SEND.getByte(), 0, localUser.getId(), userToSend);
		
		//send reply
		secureChannel.sendMessage(new NetworkMessage(msgToSend, header));
	}
}
